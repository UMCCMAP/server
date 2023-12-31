package com.umc.cmap.config;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    CAFE_NOT_FOUND(false, 3100, "카페를 찾을수 없습니다."),
    CAFE_THEME_NOT_FOUND(false,3101,"카페 테마를 찾을 수 없습니다"),
    THEME_NOT_FOUND(false,3102,"존재하지 않는 테마 이름"),
    FILTER_NOT_FOUND(false,3103,"조건에 맞는 카페가 없습니다"),
    VISITED_CAFES_NOT_FOUND(false,3104,"방문한 카페가 없습니다"),
    WANT_TO_VISIT_CAFES_NOT_FOUND(false,3105,"원하는 카페가 없습니다."),
    THEME_CAFES_NOT_FOUND(false,3106,"조건에 맞는 카페가 없습니다"),
    CAFE_THEME_ALREADY_EXISTS(false,3107,"중복되는 테마이름"),
    CAFE_IMAGE_NOT_FOUND(false,3108,"카페이미지가 없음"),
    CAFE_IMAGE_NOT_UPLOADED(false,3109,"이미지를 업로드하세요"),
    CAFE_IMAGE_NOT_UPLOADED2(false,3110,"올바르지 않은 파일값"),
    LOCATION_NOT_FOUND(false,3111,"존재하지 않는 위치좌표"),
    LOCATION_NOT_INPUT(false,3112,"위치좌표를 입력하세요"),
    THEME_NOT_INPUT(false,3113,"검색 할 테마를 입력하세요"),




    USER_NOT_FOUND(false, 3200, "유저를 찾을 수 없습니다."),
    POST_NOT_FOUND(false, 3201, "게시글을 찾을 수 없습니다."),
    POST_DELETED(false, 3202, "삭제된 게시글입니다."),
    TAG_NOT_FOUND(false, 3203, "태그를 찾을 수 없습니다."),
    DONT_HAVE_ACCESS(false, 3204, "접근 권한이 없습니다."),
    LIKE_BOARD_EXIST_DATA(false, 3205, "이미 처리된 요청입니다."),

    /**
     * 3500 : Cmap 오류
     */
    CMAP_WANT_NOT_FOUND(false, 3500, "Cmap - WANT 를 찾을 수 없습니다."),
    CMAP_WENT_NOT_FOUND(false, 3501, "Cmap - WENT 를 찾을 수 없습니다."),
    CAFE_NOT_FOUND_FOR_USER(false,3502,"저장된 카페가 없습니다."),
    DUPLICATE_CMAP_TYPE(false,3503,"동일한카페 동일한 type"),
    MATES_CMAP_NOT_FOUND(false,3504,"해당 유저의 cmap리스트가 없습니다"),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.")
    ;

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요
    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
