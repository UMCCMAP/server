package com.umc.cmap.domain.cafe.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_idx")
    private Long idx;

    private String name;

    @Embedded
    private Coordinate coordinate;


    @Builder
    public Cafe(String name, Coordinate coordinate, String information) {
        this.name = name;
        this.coordinate = coordinate;
    }

}
