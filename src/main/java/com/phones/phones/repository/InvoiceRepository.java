package com.phones.phones.repository;

import com.phones.phones.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(
            value = "SELECT i.* FROM invoices i" +
                    "INNER JOIN `lines` l ON i.id_line = l.id " +
                    "INNER JOIN users u ON l.id_user = u.id " +
                    "WHERE u.id = ?1",
            nativeQuery = true
    )
    List<Invoice> findByUserId(Long id);

    @Query(
            value = "SELECT i.* FROM invoices i " +
                    "INNER JOIN `lines` l ON l.id = i.id_line " +
                    "INNER JOIN users u ON u.id = l.id_user " +
                    "WHERE (u.id = ?1) AND (i.creation_date BETWEEN ?2 AND ?3)",
            nativeQuery = true
    )
    List<Invoice> findByUserIdBetweenDates(Long id, LocalDateTime from, LocalDateTime to);

}
