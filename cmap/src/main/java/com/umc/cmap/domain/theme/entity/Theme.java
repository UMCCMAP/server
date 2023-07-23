package com.umc.cmap.domain.theme.entity;


import com.umc.cmap.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "theme")
public class Theme extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_idx")
    private Long idx;

    @Column(name = "name")
    private String name;

    public Theme() {}

    /*public Theme(String name) {
        this.name = name;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    /*    @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
   */

}
