package com.walmart.entities;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "level_details")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LevelDetail {

    public static final int MAX_LEN_NAME = 30;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
 
    @NotEmpty
    @Length(max = MAX_LEN_NAME)
    @Column(name = "level_name", nullable = false, length = MAX_LEN_NAME)
    private String levelName;

    @NotNull
    @Min(value=1)
    @Column(name = "rows", nullable = false)
    int rows;
    
    @NotNull
    @Min(value=1)
    @Column(name = "seats_per_row", nullable = false)
    int seatsPerRow;
    
    @NotNull
    @Min(value=1)
    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "total_seats")
    private int totalSeats;
    
    @Column(name = "remaining_seats")
    private int remainingSeats ;
    
    @Override
    public String toString() {
       // return "LevelDetail{" + "id='" + id + '\'' + ", levelName='" + levelName + '\'' + '}';
        return "LevelDetail{" + "id=" + id + ", levelName=" + levelName + ", rows="+rows + ", total seats=" + totalSeats+ ", remaining seats="+ remainingSeats+ "}";
    }
    
    //@Version
    //private Integer version;
    
   //TODO will update this and load data at application startup
}
