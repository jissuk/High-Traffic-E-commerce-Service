package kr.hhplus.be.server.users.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Users")
@Tag(name = "user", description = "유저 관련 API")
public class UserController {

    @GetMapping("/{userId}/point")
    @Operation(summary = "포인트 조회", description = "유저는 자신이 보유한 포인트를 조회합니다")
    public ResponseEntity<?> getUserPoints() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/pointCharge")
    @Operation(summary = "포인트 충전", description = "유저는 상품 구매를 위한 포인트를 충전합니다")
    public ResponseEntity<?> chargeUserPoint() {
        return ResponseEntity.ok().build();
    }
}
