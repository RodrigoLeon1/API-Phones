package com.phones.phones.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class Invoice {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Integer numberCalls;

    @NotNull
    private Float costPrice;

    @NotNull
    private Float totalPrice;

    @NotNull
    private Date creationDate;

    @NotNull
    private Date dueDate;

    // Relacion DB
    @NotNull
    private Line line;
}
