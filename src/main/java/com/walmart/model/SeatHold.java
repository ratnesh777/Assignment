package com.walmart.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeatHold implements Serializable {

    private static final long serialVersionUID = 5096219399452791799L;
    private Integer id;
    private String customerEmail;
   
    @NotNull
    @Min(value=1)
    @Max(value=4)
    private Integer level;
    
    @NotNull
    @Min(value=1)
    private Integer seatsHoldCount;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "America/New_York")
    private Date date;

}
