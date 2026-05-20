package kr.hhplus.be.server.user.presentation;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.user.application.usecase.ChargePointUseCase;
import kr.hhplus.be.server.user.application.usecase.command.UserCommand;
import kr.hhplus.be.server.user.presentation.dto.UserRequest;
import kr.hhplus.be.server.user.presentation.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "user", description = "유저 관련 API")
public class UserController {

    private final ChargePointUseCase chargePointUseCase;

    @PostMapping("/chargePoint")
    @Operation(summary = "포인트 충전", description = "유저는 상품 구매를 위한 포인트를 충전합니다")
    public ResponseEntity<UserResponse> chargeUserPoint(@RequestBody @Valid UserRequest request) {

        UserCommand command = UserCommand.from(request);

        return ResponseEntity
                        .ok()
                        .body(chargePointUseCase.execute(command));
    }
}
