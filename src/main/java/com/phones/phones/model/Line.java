package com.phones.phones.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class Line {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Integer number;

    @NotNull
    private Date creationDate;

    @NotNull
    private boolean isActive;

    // Relaciones DB
    @NotNull
    private User user;

}
