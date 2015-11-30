package com.walmart.service;

import com.walmart.entities.LevelDetail;
import com.walmart.entities.Reservation;
import com.walmart.exception.ReservationNotFoundException;

public interface LeveLService {

    LevelDetail createLevel(LevelDetail levelDetail);

    LevelDetail findLeveLById(Integer levelId);

    public Integer getAllSeats();

    Reservation findReservationById(String resId) throws ReservationNotFoundException;
}
