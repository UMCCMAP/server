package com.umc.cmap.Domain.Cafe.Controller;

import com.umc.cmap.Domain.Cafe.Entity.CafeEntity;
import com.umc.cmap.Domain.Cafe.Service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cafes")
public class CafeController {

    private final CafeService cafeService;

    @GetMapping
    public List<CafeEntity> getAllCafes() {
        return cafeService.getAllCafes();
    }

    @GetMapping("/{cafeIdx}")
    public ResponseEntity<CafeEntity> getCafeById(@PathVariable Long cafeIdx) {
        CafeEntity cafe = cafeService.getCafeById(cafeIdx);
        return ResponseEntity.ok(cafe);
    }

    @PostMapping
    public ResponseEntity<CafeEntity> createCafe(@RequestBody CafeEntity cafe) {
        CafeEntity createdCafe = cafeService.createCafe(cafe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCafe);
    }

    @PutMapping("/{cafeIdx}")
    public ResponseEntity<CafeEntity> updateCafe(@PathVariable Long cafeIdx, @RequestBody CafeEntity cafe) {
        CafeEntity updatedCafe = cafeService.updateCafe(cafeIdx, cafe);
        return ResponseEntity.ok(updatedCafe);
    }

    @DeleteMapping("/{cafeIdx}")
    public ResponseEntity<Void> deleteCafe(@PathVariable Long cafeIdx) {
        cafeService.deleteCafe(cafeIdx);
        return ResponseEntity.noContent().build();
    }


}