package com.umc.cmap.domain.cafe.controller;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.cafe.entity.Location;
import com.umc.cmap.domain.cafe.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        Location createdLocation = locationService.createLocation(location.getLatitude(), location.getLongitude());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
    }

    @GetMapping("/{locationIdx}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long locationIdx) throws BaseException {
        Location location = locationService.getLocationById(locationIdx);
        return ResponseEntity.ok(location);
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }


}
