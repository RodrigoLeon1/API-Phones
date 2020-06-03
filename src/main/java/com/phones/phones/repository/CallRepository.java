package com.phones.phones.repository;

import com.phones.phones.model.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CallRepository extends JpaRepository<Call, Long> {

    @Query(
            value = "SELECT c.* from `lines` l " +
                    "INNER JOIN users u ON u.id = l.id_user " +
                    "INNER JOIN calls c ON c.id_origin_line = l.id ",
            nativeQuery = true
    )
    List<Call> findAllByUserId(Long id);

    @Query(
            value = "SELECT c.* FROM calls c " +
                    "INNER JOIN `lines` l ON l.id = c.id_origin_line " +
                    "INNER JOIN users u ON u.id = l.id_user " +
                    "WHERE (u.id = ?1) AND (c.creation_date BETWEEN ?2 AND ?3)",
            nativeQuery = true
    )
    List<Call> findAllByUserIdBetweenDates(Long id, LocalDateTime from, LocalDateTime to);

    @Query(
            value = "SELECT l.number FROM `lines` l " +
                    "INNER JOIN (SELECT calls.id_destination_line as 'destination_id', count(id_destination_line) FROM calls c " +
                    "WHERE (calls.id_origin_line = ?1)" +
                    "GROUP BY calls.id_destination_line" +
                    "ORDER BY desc" +
                    "LIMIT 1) as mostCalled" +
                    "ON l.id = mostCalled.destination_id",
            nativeQuery = true
    )
    String findMostCalledByOriginId(Long id);


}
