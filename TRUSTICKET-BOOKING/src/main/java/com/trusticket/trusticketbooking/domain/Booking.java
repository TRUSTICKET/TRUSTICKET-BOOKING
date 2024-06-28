package com.trusticket.trusticketbooking.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @Setter
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    @Setter
    @Column(name = "STATUS", nullable = false)
    private String status;
}

