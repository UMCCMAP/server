package com.umc.cmap.Domain.Cafe.Entity;
import com.umc.cmap.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "cafe")
@RequiredArgsConstructor
public class CafeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="cafe_idx")
    private Long cafeIdx;

    @Column(name="cafe_name")
    private String cafeName;

    private String location;

    @Column(name="cafe_info")
    private String cafeInfo;


    @Builder
    public CafeEntity(Long cafeIdx, String cafeName, String location, String cafeInfo) {
        this.cafeIdx = cafeIdx;
        this.cafeName = cafeName;
        this.location = location;
        this.cafeInfo = cafeInfo;
    }

    public Long getCafeIdx() {
        return cafeIdx;
    }

    public String getCafeName() {
        return cafeName;
    }

    public String getLocation() {
        return location;
    }

    public String getCafeInfo() {
        return cafeInfo;
    }


}

