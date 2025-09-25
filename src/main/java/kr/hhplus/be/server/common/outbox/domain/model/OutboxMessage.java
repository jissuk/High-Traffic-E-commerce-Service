//package kr.hhplus.be.server.common.outbox.domain.model;
//
//import jakarta.persistence.*;
//import kr.hhplus.be.server.common.outbox.domain.OutboxStatus;
//import lombok.*;
//
//import java.time.LocalDateTime;
//
//@Getter
//@Entity
//@Table(name = "outbox_messages")
//@Builder
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@ToString
//public class OutboxMessage {
//
//    @Id
//    @Column
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String topic;
//    private String payload;
//    @Enumerated(EnumType.STRING)
//    private OutboxStatus status; // PENDING, PUBLISHED
//    private LocalDateTime createdAt;
//
//    public void published(){
//        this.status = OutboxStatus.PUBLISHED;
//    }
//}