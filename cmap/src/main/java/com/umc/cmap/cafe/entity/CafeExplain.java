package com.umc.cmap.cafe.entity;

import com.umc.cmap.config.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafeExplain extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_explain_idx")
    private Long idx;

    @Size(min = 1, max = 15)
    private String explain;

    @ManyToOne
    @JoinColumn(name = "cafe_basic_explain_idx")
    CafeBasicExplain basicExplain;

    @ManyToOne
    @JoinColumn(name = "cafe_idx")
    private Cafe cafe;

}
