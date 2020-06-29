package com.phones.phones.repository.dto;

import com.phones.phones.dto.CityTopDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityTopDtoRepository extends JpaRepository<CityTopDto, Long> {
    @Query(
            value = "SELECT c.name, count(c.id) AS quantity FROM cities c " +
                    "INNER JOIN rates r ON c.id = r.id_city_destination " +
                    "INNER JOIN calls ca ON ca.id_rate = r.id " +
                    "INNER JOIN `lines` l ON ca.id_origin_line = l.id " +
                    "INNER JOIN users u ON l.id_user = u.id " +
                    "WHERE u.id = ?1 " +
                    "GROUP BY c.name " +
                    "ORDER BY quantity DESC LIMIT 10",
            nativeQuery = true
    )
    List<CityTopDto> findCitiesTopByUserId(Long id);
}
