# 나의 카페 지도 "CMAP"

---
## 문제점 정의
- 꾸준한 커피 시장 규모 증가
- 계속해서 증가하는 커피 매장

**점점 다양한 카페가 많이지는 만큼 사람들은 본인들이 선택해서 카페를 갈 수 있게 되었습니다.**
<br>**하지만 많아진 만큼 가서 실망하는 카페도 많아졌습니다.**
<br></br>**따라서 정리된 카페 정보들과 나만의 카페 지도가 있으면 좋겠다고 생각했습니다.**
---
## 핵심 기능 소개
### 소셜 로그인
![image](https://github.com/UMCCMAP/server/assets/89764169/8581268b-3f57-452f-8036-c5ddfeda379c)
- 소셜 로그인은 네이버 / 구글 로그인이 있습니다.
### 메인 지도 / 검색
![image](https://github.com/UMCCMAP/server/assets/89764169/d59b686c-3c38-4ecf-b24c-cf06d011c366)
- 메인 화면에서 `카페 둘러보기`를 클릭하면 메인 지도로 넘어갑니다.

![image](https://github.com/UMCCMAP/server/assets/89764169/273478e8-cf05-4f6d-aaec-f47c4ef2f0d1)
- 검색 창으로 카페를 검색할 수 있습니다.
- 검색 후 엔터를 누르지 않아도 실시간으로 연관된 카페들이 화면에 표시됩니다.

![image](https://github.com/UMCCMAP/server/assets/89764169/e780dfc8-9117-484d-a251-a6afc8ed93f9)
- 지도에 카페 데이터를 핀으로 나타냅니다.
- 이 핀을 누르면 아래 사진과 같이 카페 정보를 불러올 수 있습니다.

![image](https://github.com/UMCCMAP/server/assets/89764169/5742d059-614e-4bb5-8d9f-f67bffc28261)
- 이 곳에서 CMAP에 저장 및 리뷰 작성 / 수정을 할 수 있습니다.

---
## 사용한 기술
### Back-end
- `Spring boot`
- `Spring Data JPA`
- `Spring Security`
- `AWS S3`
- `AWS EC2`
- `AWS RDS`
- `MySQL`
- `Docker`
- `NGINX`