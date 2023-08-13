package com.umc.cmap.domain.theme.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.umc.cmap.domain.cafe.entity.Cafe;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "cafe_theme")
@RequiredArgsConstructor
public class CafeTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_idx")
    private Cafe cafe;

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
