package com.trusticket.trusticketbooking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BOOKING")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Booking extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKIND_ID")
    private Long bookingId;

    @Column(name = "EVENT_ID", nullable = false)
    private String eventId;

    @Column(name = "MEMBER_ID", nullable = false)
    private String memberId;

    @Column(name = "STATUS", nullable = false)
    private String status;
}

