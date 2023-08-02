package com.umc.cmap.domain.cafe.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.umc.cmap.config.BaseTimeEntity;
import com.umc.cmap.domain.theme.entity.CafeTheme;
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

    //true: 방문 , flase: 미방문
    @Column(name = "visited")
    private Boolean visited ;

    @Column(name = "want_to_visit")
    private Boolean wantToVisit ;

    @JsonManagedReference
    @OneToOne(mappedBy = "cafe",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private CafeTheme cafeTheme;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private CafeTheme theme;

    public String getThemeName() {
        return this.cafeTheme != null && this.cafeTheme.getTheme() != null ? this.cafeTheme.getTheme().getName() : null;
    }

    @Builder
    public Cafe(Long idx, String name, String location,
                String info,CafeTheme cafeTheme,Boolean visited,Boolean wantToVisit) {
        this.idx = idx;
        this.name = name;
        this.location = location;
        this.info = info;
        this.cafeTheme=cafeTheme;

        this.visited = visited != null ? visited : false;
        this.wantToVisit = wantToVisit != null ? wantToVisit : false;
    }
    public CafeTheme getCafeTheme() { return this.cafeTheme; }

    public Boolean getVisited() {return this.visited;}

}
