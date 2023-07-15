package com.umc.cmap.cafe.entity;

import com.umc.cmap.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeTheme extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_theme_idx")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_idx")
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_idx")
    private Theme theme;

    @Builder
    public CafeTheme(Cafe cafe) {
        this.cafe = cafe;
    }

}
