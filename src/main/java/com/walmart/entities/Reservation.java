package com.walmart.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
@Entity
@Table(name = "reservation")
@JsonIgnoreProperties(ignoreUnknown = true)

public class Reservation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    
    @NotNull
    private Integer seatHoldId;
    
    @NotEmpty
    @Column(name = "customer_email")
    private String customerEmail;
    
    private Integer level;
    
    @Column(name = "seat_count")
    private Integer seatsHoldCount;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "America/New_York")
    @Column(name = "transcation_date")
    private Date date;
}
