package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendee_sessions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AttendeeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    // true mientras esté conectado
    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;
}
