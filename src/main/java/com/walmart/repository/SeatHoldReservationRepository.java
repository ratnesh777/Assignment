package com.walmart.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.walmart.entities.SeatHoldReservation;

@Repository
public interface SeatHoldReservationRepository extends CrudRepository<SeatHoldReservation, Integer> {
    
  
    SeatHoldReservation findBySeatHoldIdAndCustomerEmail(final Integer seatHoldId, final String customerEmail);
    
}
