package com.walmart.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.walmart.entities.Reservation;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
}
