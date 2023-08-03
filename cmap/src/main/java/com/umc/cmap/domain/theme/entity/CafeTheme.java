package com.umc.cmap.domain.theme.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.theme.entity.Theme;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "cafetheme")
@RequiredArgsConstructor
public class CafeTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    //@JsonIgnore
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_idx")
    private Cafe cafe;
    // 이거 일대일 맞나요?
    // 카페 테마랑 테마를 다대일매칭하고 카페랑 카페테마를 일대일 매칭하면 괜찮을거 같긴한데.. 의견 주세요

    //@JsonIgnore
    //@JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theme_idx")
    private Theme theme;



    @Builder
    public CafeTheme(Long idx, Theme theme, Cafe cafe) {
        this.idx = idx;
        this.theme = theme;
        this.cafe = cafe;
    }


}
