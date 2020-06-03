package com.phones.phones.repository;

import com.phones.phones.model.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    Optional<Line> findByNumber(String number);

    @Query(
            value = "SELECT l.number from `lines` l " +
                    "INNER JOIN users u ON u.id = l.id_user " +
                    "WHERE u.id = ?1",
            nativeQuery = true
    )
    List<Line> findNumberByUserId(Long id);


    @Query(
            value = "SELECT l.* from `lines` l " +
                    "INNER JOIN users u ON u.id = l.id_user " +
                    "WHERE u.id = ?1",
            nativeQuery = true
    )
    List<Line> findAllByUserId(Long id);

}
