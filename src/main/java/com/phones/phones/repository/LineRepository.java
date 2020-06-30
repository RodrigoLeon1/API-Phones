package com.phones.phones.repository;

import com.phones.phones.model.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
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


    @Transactional
    @Modifying
    @Query(
            value = "UPDATE `lines` l " +
                    "SET l.status = 'DISABLED' " +
                    "WHERE l.id = ?1",
            nativeQuery = true
    )
    int disableById(Long id);


    @Transactional
    @Modifying
    @Query(
            value = "UPDATE `lines` l " +
                    "SET l.number =  ?2, " +
                    "l.status = ?3, " +
                    "l.id_line_type = ?4 " +
                    "WHERE l.id = ?1",
            nativeQuery = true
    )
    int updateById(Long id, String number, String status, Long lineType);

}
