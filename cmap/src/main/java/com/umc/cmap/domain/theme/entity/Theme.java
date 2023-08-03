package com.umc.cmap.domain.theme.entity;

import com.umc.cmap.config.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "theme")
public class Theme extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_idx")
    private Long idx;

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
