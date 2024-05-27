package com.nbcamp.app.controller;

import com.nbcamp.app.dto.ReplyRequestDto;
import com.nbcamp.app.service.ReplyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controller 클래스를 Bean 컨테이너에 등록해주는 어노테이션 특별히 Rest는 html주소가 아닌 데이터를 반환할 수 있다
@RestController
// 모든 메서드에 공용으로 쓸 url주소를 설정해주는 어노테이션
@RequestMapping("/reply")
// 생성자 주입을 가능하게 해주는 어노테이션
@RequiredArgsConstructor
public class ReplyController {

    // 실제 로직을 담고 있는 Service단으로 받아온 정보를 전송하기 위해 Service를 생성자 주입
    private final ReplyService replyService;

    // 내용이 많은 댓글 정보를 넘겨야 할 수도 있기 때문에 Post로 설정한다
    @PostMapping("/{boardNumber}")
    // PathVariable로 url 주소를 통해 댓글을 입력할 일정 번호를 받고 RequestBody에 입력할 댓글 정보를 받고 토큰 정보가 담겨있을 HttpServletRequest를 받아온다
    // 이 때 Valid를 통해서 replyRequestDto의 내용이 비어있지 않은지 검증을 실행한다
    public ResponseEntity write(@PathVariable Long boardNumber, @RequestBody @Valid ReplyRequestDto replyRequestDto, HttpServletRequest request) {
        // Service단으로 받아온 값 전달하고 리턴될 값을 ResponseBody 쪽에 담아서 전송한다
        return ResponseEntity.ok().body(replyService.register(replyRequestDto, boardNumber, request));
    }

    // 수정을 할 것이기 때문에 Put으로 설정한다
    @PutMapping("/{boardNumber}")
    // 댓글 입력할 때와 동일한 정보들을 받아온다
    public ResponseEntity modify(@PathVariable Long boardNumber, @RequestBody @Valid ReplyRequestDto replyRequestDto, HttpServletRequest request) {
        // Service단으로 받아온 값 전달하고 리턴될 값을 ResponseBody 쪽에 담아서 전송한다
        return ResponseEntity.ok().body(replyService.modify(replyRequestDto, boardNumber, request));
    }

    // 삭제를 할 것이기 때문에 Delete로 설정한다
    @DeleteMapping("/{boardNumber}/{replyNumber}")
    // 삭제는 일정 ID와 댓글 ID만 있으면 됨으로 둘다 PathVariable로 받아와주고 값이 2개이기 때문에 정확히 명시를 해준다. 그리고 토큰 정보 또한 필요함으로 HttpServletRequest도 받아와준다
    public ResponseEntity delete(@PathVariable("boardNumber") Long boardNumber, @PathVariable("replyNumber") Long replyNumber, HttpServletRequest request) {
        // Service단으로 받아온 값을 전달한다 (리턴 없음)
        replyService.delete(boardNumber, replyNumber, request);

        // 잘 전달이 되고 삭제가 되었다면 상태코드와 성공 메세지를 전달해준다
        return ResponseEntity.status(200).body("삭제를 성공하였습니다!");
    }
}
