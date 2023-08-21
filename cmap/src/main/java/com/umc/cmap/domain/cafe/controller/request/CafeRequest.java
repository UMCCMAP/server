package com.umc.cmap.domain.cafe.controller.request;


import lombok.Data;

@Data

public class CafeRequest {
    private String name;
    private String city;
    private String district;
    private String info;
    private Long locationIdx;
    private String image;


}
