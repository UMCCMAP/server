package com.umc.cmap.domain.cmap.dto;

import com.umc.cmap.domain.cmap.entity.Cmap;
import lombok.Data;

@Data
public class CmapResponse {
    private Long idx;
    //private String userNickname;
    private Long cafeIdx;
    private String type;


    private String cafeName;
    private String cafeCity;
    private String cafeDistrict;
    private Double cafeLatitude;
    private Double cafeLongitude;


    public CmapResponse(Cmap cmap) {
        this.idx = cmap.getIdx();
        //this.userId = cmap.getUser().getIdx();
        //this.userNickname = cmap.getUser().getNickname();
        this.cafeIdx = cmap.getCafe().getIdx();
        this.type = cmap.getType().toString();

        this.cafeName = cmap.getCafe().getName();
        this.cafeCity = cmap.getCafe().getCity();
        this.cafeDistrict = cmap.getCafe().getDistrict();
        this.cafeLatitude = cmap.getCafe().getLocation().getLatitude();
        this.cafeLongitude = cmap.getCafe().getLocation().getLongitude();
    }


}
