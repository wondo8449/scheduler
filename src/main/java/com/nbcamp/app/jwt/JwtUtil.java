package com.nbcamp.app.jwt;

import com.nbcamp.app.entity.User;
import com.nbcamp.app.entity.UserRoleEnum;
import com.nbcamp.app.repository.UserRefreshTokenRepository;
import com.nbcamp.app.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    // Header에 Key에 넣어줄 상수
    public static final String ACCESS_TOKEN = "Access_token";
    public static final String REFRESH_TOKEN = "Refresh_token";
    // 토큰 내용 중 권한을 넣을 부분에 이름을 설정하기 위한 상수
    public static final String AUTHORIZATION_KEY = "auth";
    // JWT 토큰 앞쪽에 넣기로 한 약속
    public static final String BEARER_PREFIX = "Bearer ";
    // Access Token 만료 시간 설정 (테스트를 위해 1분으로 설정)
    private final long TOKEN_TIME = 60 * 1000L;
    // Refresh Token 만료 시간 설정(테스트를 위해 5분으로 설정)
    private final long REFRESH_TOKEN_TIME = 5 * 60 * 1000L;

    // @Value 어노테이션을 이용하여 application.properties의 정보를 가져와서 String에 담아준다
    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;

    // JWT를 관리하는 최신의 방법, secretKey를 담을 객체
    private Key key;

    // 토큰을 암호화하는데 사용할 알고리즘 보통 JWT에선 HS256을 많이 사용한다, ENUM으로 되어있어서 골라서 사용 가능
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // DB 조작을 위해 필요한 Repository들 생성자 주입
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserRepository userRepository;


    public JwtUtil(UserRefreshTokenRepository userRefreshTokenRepository, UserRepository userRepository) {
        this.userRefreshTokenRepository = userRefreshTokenRepository;
        this.userRepository = userRepository;
    }

    // 딱 한번만 받아오면 되는 값을 사용할 때마다 요청을 새로 호출하는 실수를 방지하기 위해 사용하는 어노테이션
    @PostConstruct
    public void init() {
        // Base64를 통하여 인코딩된 secretKey 값을 디코딩하여 byte배열에 담아준다
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        // 디코딩된 배열을 만들어둔 key에 담아준다
        key = Keys.hmacShaKeyFor(bytes);
    }

    // JWT 토큰 생성
    public String createAccessToken(String userName, UserRoleEnum role) {

        return BEARER_PREFIX + // 앞에 붙이기로 약속된 상수
                Jwts.builder() // 빌더 메서드
                        .setSubject(userName) // 사용자 아이디
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setIssuedAt(Timestamp.valueOf(LocalDateTime.now())) // 만들어진 시간
                        .setExpiration(new Date(System.currentTimeMillis() + TOKEN_TIME)) // 만료 시간
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // refresh 토큰 생성 Access Token과 비슷하나 구분을 위해 BEARER_PREFIX를 제외하고 생성
    public String createRefreshToken(String userName, UserRoleEnum role) {
        return Jwts.builder()
                .setSubject(userName)
                .claim(AUTHORIZATION_KEY, role)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // request header 에서 JWT 가져오기, HttpServletRequest를 입력받아야 한다
    public String getAccessTokenFromHeader(HttpServletRequest request) {

        // getHeader 메서드를 통해 Key 부분이 미리 설정해둔 상수인 AUTHORIZATION_HEADER 부분의 값을 가져온다
        String accessToken = request.getHeader(ACCESS_TOKEN);

        // 받아온 토큰이 null이 아닌지와 설정해 둔 상수인 BEARER_PREFIX로 시작하는지 확인
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
            // 순수한 토큰만을 가져오기 위해 앞에 7글자은 "Bearer "을 잘라내고 리턴한다
            return accessToken.substring(7);
        }
        // 만약 조건이 맞지 않는다면 null을 리턴해서 토큰을 못 가져오게 한다
        return null;
    }

    // Access 토큰을 가져오는 로직과 동일하나 Header Key 부분을 REFRESH_TOKEN으로 설정하고
    // BEARER_PREFIX를 붙이지 않았으므로 바로 리턴해준다
    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_TOKEN);

        if(StringUtils.hasText(refreshToken)) {
            return refreshToken;
        }
        return null;
    }

    // JWT 토큰 검증 메서드, 순수한 토큰을 매개변수로 넘겨준다
    public User validateAndFindUserAccessToken(String accseetoken, String refreshtoken) {

        // try catch로 토큰에 관한 예외 처리
        try {
            // 토큰 검증 메서드
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accseetoken);

            // 예외가 없이 통과되면 토큰 정보에서 유저정보를 추출
            Claims userInfo = getUserInfoFromToken(accseetoken);

            // 로그인 성공했다면 유저는 존재할테니 따로 예외처리는 하지 않고 유저 정보만 가져와준다.
            User user = userRepository.findByUserName(userInfo.getSubject()).orElseThrow();

            // 검증이 완료되면 찾은 user 리턴
            return user;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            // Access 토큰이 만료된 경우 여기로 들어오게 된다
        } catch (ExpiredJwtException e) {
            // Access 토큰이 만료되었음을 알려준다
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            // refresh 토큰에 대한 검증을 실시, 검증에 실패하면 Access 토큰을 재생성하지 않고 null로 리턴
            if (validateRefreshToken(refreshtoken)) {

                // 검증 성공하였으면 refresh토큰에 담긴 user정보로 새로운 Accsee 토큰을 발급
                String newAccessToken = recreateAccessToken(refreshtoken);
                // 잘 생성되었는지 확인하기 위한 log
                log.info("newAccessToken : " + newAccessToken);

                // 새로 생성한 토큰에서 다시 user 정보를 가져온다
                Claims userInfo = getUserInfoFromToken(newAccessToken.substring(7));
                // 가져온 user 정보로 DB에서 user 찾기
                User user = userRepository.findByUserName(userInfo.getSubject()).orElseThrow();

                // 찾은 user를 리턴해준다
                return user;
            }
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        // refresh 토큰 검증 실패 시 null을 리턴하며 Service단에서 예외처리를 할 수 있도록 해준다
        return null;
    }

    // Refresh 토큰 유효성 검사
    @Transactional(readOnly = true)
    public boolean validateRefreshToken(String refreshToken) {
        try {
            // refresh 토큰 검증
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);

            // 검증에 성공하면 true 리턴
            return true;

            // refresh 토큰 만료 시 예외처리
        } catch (ExpiredJwtException e) {
            log.error("Refresh 토큰이 만료되었습니다. 다시 로그인해주세요.");
        }
        return false;
    }

    // 토큰에서 유저 정보를 가져오는 메서드
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 만료된 엑세스 토큰 재생성
    public String recreateAccessToken(String refreshtoken) {
        // refresh 토큰에서 유저 정보를 받아온다
        Claims info = getUserInfoFromToken(refreshtoken);
        // 찾아온 정보로 DB에서 user 가져오기
        User user = userRepository.findByUserName(info.getSubject()).orElseThrow();

        // 새로 만든 Access 토큰을 리턴해준다
        return createAccessToken(user.getUserName(), user.getRole());
    }
}
