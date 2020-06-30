package com.phones.phones.repository;

import com.phones.phones.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    @Query(
            value = "SELECT p.* FROM users u " +
                    "INNER JOIN cities c ON u.id_city = c.id " +
                    "INNER JOIN provinces p ON c.id_province = p.id " +
                    "WHERE u.id = ?1",
            nativeQuery = true
    )
    Optional<Province> findByUserId(Long id);

    Optional<Province> findByName(String name);

}
