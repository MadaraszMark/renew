package hu.renew.main.service;

import hu.renew.main.repository.LaptopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final LaptopRepository laptopRepository;

    public List<Map<String, Object>> getLaptopStatsByManufacturer() {
        List<Object[]> results = laptopRepository.countLaptopsByManufacturer();
        List<Map<String, Object>> stats = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("manufacturer", row[0]);
            map.put("count", row[1]);
            stats.add(map);
        }

        return stats;
    }
}

