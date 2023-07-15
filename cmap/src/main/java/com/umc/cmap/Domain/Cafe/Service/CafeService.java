package com.umc.cmap.Domain.Cafe.Service;


import com.umc.cmap.Domain.Cafe.Entity.CafeEntity;
import com.umc.cmap.Domain.Cafe.Repository.CafeRepository;
import com.umc.cmap.Domain.Cafe.Exception.CafeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;

    /*public CafeEntity getCafeById(Long cafeIdx) {
        return cafeRepository.findById(cafeIdx)
                .orElseThrow(() -> new CafeNotFoundException(cafeIdx + " :존재하지 않는 카페"));
    }*/

    public CafeEntity getCafeById(Long cafeIdx) {
        try {
            return cafeRepository.findById(cafeIdx)
                    .orElseThrow(() -> new CafeNotFoundException("cafeIdx"+ cafeIdx + " 은 존재하지 않는 카페"));
        } catch (CafeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 카페", e);
        }
    }


    public List<CafeEntity> getAllCafes() {
        return cafeRepository.findAll();
    }

    public CafeEntity createCafe(CafeEntity cafe) {
        return cafeRepository.save(cafe);
    }

    public CafeEntity updateCafe(Long cafeIdx, CafeEntity cafe) {
        CafeEntity existingCafe = getCafeById(cafeIdx);

        CafeEntity updatedCafe = CafeEntity.builder()
                .cafeIdx(existingCafe.getCafeIdx())
                .cafeName(existingCafe.getCafeName())
                .location(existingCafe.getLocation())
                .cafeInfo(existingCafe.getCafeInfo())
                .build();

        return cafeRepository.save(updatedCafe);
    }

    public void deleteCafe(Long cafeIdx) {
        CafeEntity cafe = getCafeById(cafeIdx);
        cafeRepository.delete(cafe);
    }
}
