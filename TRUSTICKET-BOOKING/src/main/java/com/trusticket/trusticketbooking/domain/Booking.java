package com.trusticket.trusticketbooking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BOOKING")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Booking extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_ID")
    private Long bookingId;

    @Column(name = "EVENT_ID", nullable = false)
    private String eventId;

    @Column(name = "MEMBER_ID", nullable = false)
    private Long memberId;

    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Column(name = "STATUS", nullable = false)
    private String status;
}

