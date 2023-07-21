package com.umc.cmap.domain.cafe.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.config.BaseResponseStatus;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;



    public Cafe getCafeById(Long idx) throws BaseException {
        return cafeRepository.findById(idx)
                .orElseThrow(() -> new BaseException(new BaseResponse<>(BaseResponseStatus.CAFE_NOT_FOUND)));
    }




    public List<Cafe> getAllCafes() {
        return cafeRepository.findAll();
    }

    public Cafe createCafe(Cafe cafe) {
        return cafeRepository.save(cafe);
    }

    public Cafe updateCafe(Long idx, Cafe cafe) throws BaseException {
        Cafe existingCafe = getCafeById(idx);

        Cafe updatedCafe = Cafe.builder()
                .idx(existingCafe.getIdx())
                .name(existingCafe.getName())
                .location(existingCafe.getLocation())
                .info(existingCafe.getInfo())
                .build();

        return cafeRepository.save(updatedCafe);
    }

    public void deleteCafe(Long idx) throws BaseException {
        Cafe cafe = getCafeById(idx);
        cafeRepository.delete(cafe);
    }
}
