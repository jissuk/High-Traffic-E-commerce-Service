package kr.hhplus.be.server.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.common.response.CommonResponse;
import kr.hhplus.be.server.user.usecase.ChargePointUseCase;
import kr.hhplus.be.server.user.usecase.command.UserCommand;
import kr.hhplus.be.server.user.usecase.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<CommonResponse> chargeUserPoint(@RequestBody @Valid UserRequestDTO request) {

        UserCommand command = UserCommand.from(request);

        return ResponseEntity
                        .ok()
                        .body(new CommonResponse(HttpStatus.CREATED, "success", chargePointUseCase.execute(command)));
    }
}
