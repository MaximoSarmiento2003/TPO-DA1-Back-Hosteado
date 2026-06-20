package com.example.tpoDA.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subastadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auctioneer {

    @Id
    @Column(name = "identificador")
    private Integer id;

    // 🔗 Person (PK compartida)
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "identificador")
    private Person person;

    @Column(name = "matricula", length = 15)
    private String license;

    @Column(name = "region", length = 50)
    private String region;
}
