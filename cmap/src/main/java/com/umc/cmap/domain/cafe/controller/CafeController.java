package com.umc.cmap.domain.cafe.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.config.BaseResponse;
import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.service.CafeService;
import lombok.RequiredArgsConstructor;
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
    public List<Cafe> getAllCafes() {
        return cafeService.getAllCafes();
    }

    @GetMapping("/{idx}")
    public ResponseEntity<Cafe> getCafeById(@PathVariable Long idx) throws BaseException {
        Cafe cafe = cafeService.getCafeById(idx);
        return ResponseEntity.ok(cafe);
    }


    @PostMapping
    public ResponseEntity<Cafe> createCafe(@RequestBody Cafe cafe) {
        Cafe createdCafe = cafeService.createCafe(cafe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCafe);
    }

    @PutMapping("/{idx}")
    public ResponseEntity<Cafe> updateCafe(@PathVariable Long idx, @RequestBody Cafe cafe) throws BaseException {
        Cafe updatedCafe = cafeService.updateCafe(idx, cafe);
        return ResponseEntity.ok(updatedCafe);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<Void> deleteCafe(@PathVariable Long idx) throws BaseException {
        cafeService.deleteCafe(idx);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<?>> handleBaseException(BaseException ex) {
        BaseResponse<?> response = ex.getStatus();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}