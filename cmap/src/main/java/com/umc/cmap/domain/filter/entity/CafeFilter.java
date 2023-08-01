package com.umc.cmap.domain.filter.entity;

import com.umc.cmap.config.BaseTimeEntity;
import com.umc.cmap.domain.cafe.entity.Cafe;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "cafe_filter")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CafeFilter extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_filter_idx")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_idx", referencedColumnName = "cafe_idx")
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filter_idx", referencedColumnName = "filter_idx")
    private Filter filter;


}
