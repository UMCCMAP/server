package com.umc.cmap.domain.cafe.service;

import com.umc.cmap.config.BaseException;
import com.umc.cmap.domain.cafe.entity.Location;
import com.umc.cmap.domain.cafe.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.umc.cmap.config.BaseResponseStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location createLocation(double latitude, double longitude) {
        Location location = new Location(latitude, longitude);
        return locationRepository.save(location);
    }

    public Location getLocationById(Long locationIdx) throws BaseException {
        return locationRepository.findById(locationIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.LOCATION_NOT_FOUND));
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
