package kr.hhplus.be.server.common.exception;

import kr.hhplus.be.server.coupon.exception.CouponOutOfStockException;
import kr.hhplus.be.server.coupon.exception.InvalidCouponException;
import kr.hhplus.be.server.coupon.exception.UserCouponNotFoundException;
import kr.hhplus.be.server.order.exception.OrderItemNotFoundException;
import kr.hhplus.be.server.order.exception.OrderNotFoundException;
import kr.hhplus.be.server.payment.exception.PaymentNotFoundException;
import kr.hhplus.be.server.product.exception.InsufficientStockException;
import kr.hhplus.be.server.product.exception.ProductNotFoundException;
import kr.hhplus.be.server.user.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("UserNotFound", e.getMessage()));
    }

    @ExceptionHandler(UserCouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserCouponNotFound(UserCouponNotFoundException e){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("UserCouponNotFound", e.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException e){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("OrderNotFound", e.getMessage()));
    }

    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderItemNotFound(OrderItemNotFoundException e){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("OrderItemNotFound", e.getMessage()));
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFoundException e){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("PaymentNotFound", e.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException e){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("ProductNotFound", e.getMessage()));
    }

    @ExceptionHandler(InvalidCouponException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCoupon(InvalidCouponException e){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("InvalidCoupon", e.getMessage()));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStock(InsufficientStockException e){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("InsufficientStock", e.getMessage()));
    }

    @ExceptionHandler(InsufficientPointBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientPointBalance(InsufficientPointBalanceException e){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("InsufficientPointBalance", e.getMessage()));
    }

    @ExceptionHandler(InvalidPointAmountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPointAmount(InvalidPointAmountException e){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("InvalidPointAmount", e.getMessage()));
    }

    @ExceptionHandler(PointLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handlePointLimitExceeded(PointLimitExceededException e){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("PointLimitExceeded", e.getMessage()));
    }

    @ExceptionHandler(CouponOutOfStockException.class)
    public ResponseEntity<ErrorResponse> handleCouponOutOfStock(CouponOutOfStockException e){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("CouponOutOfStock", e.getMessage()));
    }

    @ExceptionHandler(UserCouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserCouponNotFoundException(UserCouponNotFoundException e){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("UserCouponNotFoundException", e.getMessage()));
    }




}