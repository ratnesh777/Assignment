package com.walmart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.entities.LevelDetail;
import com.walmart.entities.Reservation;
import com.walmart.exception.ReservationNotFoundException;
import com.walmart.repository.LevelRepository;
import com.walmart.repository.ReservationRepository;

@Service
public class LevelServiceImpl implements LeveLService {

    @Autowired
    private LevelRepository leveRepository;
    
    @Autowired
    private ReservationRepository reservationRepository;

    public LevelDetail createLevel(LevelDetail levelDetail) {
        levelDetail = leveRepository.save(levelDetail);
        return levelDetail;
    }

    public LevelDetail findLeveLById(Integer levelId) {
        return leveRepository.findOne(levelId);
    }

    public Integer getAllSeats() {
        return leveRepository.getAllSeats();
    }

    
    @Override
    public Reservation findReservationById(String resId) throws ReservationNotFoundException {
        Reservation reservation = null;
        try {
            reservation = reservationRepository.findOne(Integer.parseInt(resId));
        } catch (NumberFormatException e) {
        }
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation with id " + resId + "not found.");
        }
        return reservation;
    }
}
