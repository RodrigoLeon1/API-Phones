package com.phones.phones.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "`lines`")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String number;

    @JsonIgnore
    private Date creationDate;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private LineStatus status;

    @NotNull
    @JsonBackReference(value = "lineTypeLine")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_line_type", nullable = false)
    private LineType lineType;

    @NotNull
    @JsonBackReference(value = "userLine")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @OneToMany(mappedBy = "line")
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "originLine")
    private List<Call> originCalls;

    @OneToMany(mappedBy = "destinationLine")
    private List<Call> destinationCalls;

    public boolean isDisabled() {
        return String.valueOf(status).contains("DISABLED");
    }

    public boolean isSuspended() {
        return String.valueOf(status).contains("SUSPENDED");
    }

}
