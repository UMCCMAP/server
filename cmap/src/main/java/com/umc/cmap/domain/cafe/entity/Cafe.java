package com.umc.cmap.domain.cafe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.umc.cmap.config.BaseTimeEntity;
import com.umc.cmap.domain.board.entity.Board;
import com.umc.cmap.domain.cmap.entity.Cmap;
import com.umc.cmap.domain.review.entity.Review;
import com.umc.cmap.domain.theme.entity.CafeTheme;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter

@Table(name = "cafe")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="cafe_idx")
    private Long idx;

    @Column(name="cafe_name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "cafe_image")
    private String image;


    @Column(name="cafe_info")
    private String info;

    @JsonManagedReference
    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CafeTheme> cafeThemes = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "location_idx")
    private Location location;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Cmap> cmaps = new ArrayList<>();


    @JsonIgnore
    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt();
    }


    @Builder
    public Cafe(Long idx, String name, String city, String district, String info,
                CafeTheme cafeTheme,String image,Location location) {
        this.idx = idx;
        this.name = name;
        this.city = city;
        this.district = district;
        this.info = info;
        this.image=image;
        this.location = location;
    }

    public List<CafeTheme> getCafeThemes() {
        return cafeThemes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
