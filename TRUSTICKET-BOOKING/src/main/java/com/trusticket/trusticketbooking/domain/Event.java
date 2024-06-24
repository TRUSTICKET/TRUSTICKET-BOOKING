package com.trusticket.trusticketbooking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "EVENT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Event extends BaseEntity{
    @Id
    @Column(name = "EVENT_ID")
    private String eventId;

    @Column(name = "PRICE", nullable = false)
    private Integer price;

    @Column(name = "STOCK", nullable = false)
    private Integer stock; // 판매 티켓량

    @Column(name = "MAX_STOCK", nullable = false)
    private Integer maxStock; // 판매 티켓량

    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate; // 예매 시작시간

    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate; // 예매 종료시간
}

