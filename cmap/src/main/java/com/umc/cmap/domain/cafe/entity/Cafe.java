package com.umc.cmap.domain.cafe.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.umc.cmap.config.BaseTimeEntity;
import com.umc.cmap.domain.theme.entity.CafeTheme;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
        public Cafe(Long idx, String name, String location, String info, CafeTheme cafeTheme) {
            this.idx = idx;
            this.name = name;
            this.location = location;
            this.info = info;
            this.cafeTheme=cafeTheme;
        }



/*
    @Builder
    public Cafe(Long idx, String name, String location, String info,CafeTheme cafeTheme) {
        this.idx = idx;
        this.name = name;
        this.location = location;
        this.info = info;
        this.cafeTheme=cafeTheme;
    }
*/
    //@JsonIgnore
    @JsonManagedReference
    @OneToOne(mappedBy = "cafe",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private CafeTheme cafeTheme;

    public CafeTheme getCafeTheme() {
        return this.cafeTheme;
    }

}
