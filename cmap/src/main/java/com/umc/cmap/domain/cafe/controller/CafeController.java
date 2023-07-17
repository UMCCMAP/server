package com.umc.cmap.domain.cafe.controller;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cafe.service.CafeService;
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
    public List<Cafe> getAllCafes() {
        return cafeService.getAllCafes();
    }

    @GetMapping("/{idx}")
    public ResponseEntity<Cafe> getCafeById(@PathVariable Long idx) {
        Cafe cafe = cafeService.getCafeById(idx);
        return ResponseEntity.ok(cafe);
    }

    @PostMapping
    public ResponseEntity<Cafe> createCafe(@RequestBody Cafe cafe) {
        Cafe createdCafe = cafeService.createCafe(cafe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCafe);
    }

    @PutMapping("/{idx}")
    public ResponseEntity<Cafe> updateCafe(@PathVariable Long idx, @RequestBody Cafe cafe) {
        Cafe updatedCafe = cafeService.updateCafe(idx, cafe);
        return ResponseEntity.ok(updatedCafe);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<Void> deleteCafe(@PathVariable Long idx) {
        cafeService.deleteCafe(idx);
        return ResponseEntity.noContent().build();
    }



}