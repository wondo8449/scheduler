package com.nbcamp.app.service;

import com.nbcamp.app.dto.LoginRequestDto;
import com.nbcamp.app.dto.SignupRequestDto;
import com.nbcamp.app.entity.UserRefreshToken;
import com.nbcamp.app.entity.User;
import com.nbcamp.app.entity.UserRoleEnum;
import com.nbcamp.app.jwt.JwtUtil;
import com.nbcamp.app.repository.UserRefreshTokenRepository;
import com.nbcamp.app.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final UserRefreshTokenRepository userRefreshTokenRepository;

    // 회원가입시 ADMIN으로 가입하기 위해 입력해야하는 토큰 (원래는 이렇게 있으면 안된다.. 배포할거 아니니깐..)
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원가입 메서드
    public ResponseEntity signup(SignupRequestDto requestDto) {
        // requestDto에서 username과 password 받아오기
        String userName = requestDto.getUserName();
        String password = requestDto.getUserPassword();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUserName(userName);
        // Optional의 isPresent를 활용하여 username이 존재하는지 확인
        if (checkUsername.isPresent()) {
            // 중복된 이름이라면 예외 처리
            throw new IllegalArgumentException("중복된 userName 입니다.");
        }

        // 중복된 username이 아니라면 사용자 ROLE 확인
        // 일단 일반 사용자 권한을 default값으로 넣어둔다
        UserRoleEnum role = UserRoleEnum.USER;
        // isAdmin을 활용하면 admin의 boolean 값을 가져와준다(getter setter와 비슷한 느낌)
        if (requestDto.isAdmin()) {
            // 값이 true이면 admin token값이 맞게 입력되었는지 확인
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                // 암호가 틀렸을 경우 예외처리
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            // 성공하면 권한을 admin으로 바꿔줌
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록을 하기 위해 받아온 requestDto를 통해 User 객체 생성
        User user = new User(requestDto.getUserNickname(), userName, password, role);

        // 생성한 User를 DB에 저장
        userRepository.save(user);

        // 클라이언트에게 보여줄 성공 메세지
        String message = "회원가입 성공!";

        // 메세지와 상태코드를 담아서 리턴
        return new ResponseEntity(message, HttpStatus.CREATED);
    }

    // 로그인 메서드
    public ResponseEntity login(LoginRequestDto loginRequestDto) {
        // requestDto에서 username과 password 받아오기
        String username = loginRequestDto.getUserName();
        String password = loginRequestDto.getUserPassword();

        // 입력받은 user 정보와 DB에 저장된 user정보를 비교
        User user = userRepository.findByUserName(username)
                .filter(u -> password.equals(u.getUserPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));


        // JWT 토큰 생성
        String accesstoken = jwtUtil.createAccessToken(user.getUserName(), user.getRole());
        String refreshtoken = jwtUtil.createRefreshToken(user.getUserName(), user.getRole());

        // DB에 저장되어 있던 user의 refresh 토큰의 정보를 업데이트
        userRefreshTokenRepository.findById(user.getUserNumber())
                // 현재 저장된 refresh 토큰이 있는지 확인
                .ifPresentOrElse(
                        // 이미 있다면 update를 진행
                        t -> t.updateRefreshToken(refreshtoken),
                        // 없다면 새로운 토큰을 저장
                        () -> userRefreshTokenRepository.save(new UserRefreshToken(user, refreshtoken))
                );

        // 토큰을 담을 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        // 헤더에 Key 값은 AUTHORIZATION_HEADER, Value에 토큰을 담아서 저장
        headers.set(jwtUtil.ACCESS_TOKEN, accesstoken);
        headers.set(jwtUtil.REFRESH_TOKEN, refreshtoken);
        // 클라이언트에게 보여줄 성공 메세지
        String message = "로그인 성공!";

        // 메세지와 토큰이 담긴 헤더와 상태코드를 담아서 리턴
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    // requestheader에서 받아온 토큰 유효성 검사 후 유저 정보를 찾아 리턴해주는 메서드
    public User checktokenAndFindUser(HttpServletRequest request) {

        // JwtUtil에 만들어 놓은 순수한 토큰만 가져오는 메서드에 request 전달
        String accseetoken = jwtUtil.getAccessTokenFromHeader(request);
        String refreshtoken = jwtUtil.getRefreshTokenFromHeader(request);

        // 토큰을 검증하고 토큰 정보에 user를 리턴해주는 메서드 실행
        User user = jwtUtil.validateAndFindUserAccessToken(accseetoken, refreshtoken);

        // 만약 user가 null이 아니라면 토큰이 정상이고 user를 찾아온 것
        if(!(user == null)){
            // 찾아온 user를 리턴
            return user;
        }
        // user가 null이라면 refresh 토큰까지 만료된 상황이므로 재로그인 유도
        throw new IllegalArgumentException("토큰이 만료되었습니다. 다시 로그인해주세요.");
    }
}
