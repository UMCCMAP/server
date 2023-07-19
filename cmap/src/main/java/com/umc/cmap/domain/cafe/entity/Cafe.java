package com.umc.cmap.domain.cafe.entity;

import com.umc.cmap.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "cafe")
//@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="cafe_idx")
    private Long idx;

    @Column(name="cafe_name")
    private String name;

    private String location;

    @Column(name="cafe_info")
    private String info;


    @Builder
    public Cafe(Long idx, String name, String location, String info) {
        this.idx = idx;
        this.name = name;
        this.location = location;
        this.info = info;
    }

}
