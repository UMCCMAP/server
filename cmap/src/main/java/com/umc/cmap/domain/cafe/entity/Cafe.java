package com.umc.cmap.domain.cafe.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.umc.cmap.config.BaseTimeEntity;
import com.umc.cmap.domain.theme.entity.CafeTheme;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    //private String location;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;


    @Column(name="cafe_info")
    private String info;

    //true: 방문 , flase: 미방문
    @Column(name = "visited")
    private Boolean visited ;

    @Column(name = "want_to_visit")
    private Boolean wantToVisit ;

    @JsonManagedReference
    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CafeTheme> cafeThemes = new ArrayList<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theme_id")
    private CafeTheme theme;

    public String getThemeName() {
        return this.cafeThemes != null && !this.cafeThemes.isEmpty() && this.cafeThemes.get(0).getTheme() != null
                ? this.cafeThemes.get(0).getTheme().getName() : null;
    }

    @Builder
    public Cafe(Long idx, String name, String city, String district, String info,
                CafeTheme cafeTheme, Boolean visited, Boolean wantToVisit) {
        this.idx = idx;
        this.name = name;
        this.city = city;
        this.district = district;
        this.info = info;
        this.visited = visited != null ? visited : false;
        this.wantToVisit = wantToVisit != null ? wantToVisit : false;
    }

    public List<CafeTheme> getCafeThemes() {
        return cafeThemes;
    }
    public Boolean getVisited() {return this.visited;}

}
