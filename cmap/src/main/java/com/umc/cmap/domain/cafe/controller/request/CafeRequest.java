package com.umc.cmap.domain.cafe.controller.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data

public class CafeRequest {
    private String name;
    private String city;
    private String district;
    private String info;
    private Long locationIdx;
    private String image;


}
