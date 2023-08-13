/*package com.umc.cmap.domain.cmap.controller;

import com.umc.cmap.domain.cafe.entity.Cafe;
import com.umc.cmap.domain.cmap.service.CMAPService;
import com.umc.cmap.domain.filter.entity.Filter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cmap")
public class CMAPController {
    private final CMAPService cmapService;

    // 생성자 주입

    @GetMapping("/search")
    public List<Cafe> searchCafes(@RequestParam(required = false) String cafeName,
                                  @RequestParam(required = false) String theme) {
        if (cafeName != null) {
            return cmapService.searchCafesByName(cafeName);
        } else if (theme != null) {
            return cmapService.searchCafesByTheme(theme);
        } else {
            return cmapService.getRandomCafes();
        }
    }

    @GetMapping("/filters")
    public List<Filter> getFilters() {
        return cmapService.getAllFilters();
    }

    @PostMapping("/filters")
    public Filter createFilter(@RequestBody Filter filter) {
        return cmapService.createFilter(filter);
    }

    @PutMapping("/filters/{filterIdx}")
    public Filter updateFilter(@PathVariable Long filterIdx, @RequestBody Filter updatedFilter) {
        return cmapService.updateFilter(filterIdx, updatedFilter);
    }

    @DeleteMapping("/filters/{filterIdx}")
    public void deleteFilter(@PathVariable Long filterIdx) {
        cmapService.deleteFilter(filterIdx);
    }
}
*/