package com.umc.cmap.domain.filter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "filter_idx")
    private Long idx;

    @Column(name = "name")
    private String name;

    @Builder
    public Filter(String name) {
        this.name = name;
    }

}
