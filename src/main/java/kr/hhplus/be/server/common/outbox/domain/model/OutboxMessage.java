package kr.hhplus.be.server.common.outbox.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.outbox.domain.OutboxStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "outbox_messages")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class OutboxMessage {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String topic;
    @Column
    private String payload;
    @Column
    @Enumerated(EnumType.STRING)
    private OutboxStatus status; // PENDING, PUBLISHED
    @Column
    private LocalDateTime createdAt;

    public void processing(){
        this.status = OutboxStatus.PROCESSING;
    }

    public void published(){
        this.status = OutboxStatus.PUBLISHED;
    }

    public void failed(){
        this.status = OutboxStatus.FAILED;
    }

    public static OutboxMessage of(String topic, String payload){
        return OutboxMessage.builder()
                            .topic(topic)
                            .payload(payload)
                            .status(OutboxStatus.PENDING)
                            .createdAt(LocalDateTime.now())
                            .build();
    }
}