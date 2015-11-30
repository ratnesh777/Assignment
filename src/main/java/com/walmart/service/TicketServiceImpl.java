package com.walmart.service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.transaction.Transactional;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.walmart.entities.LevelDetail;
import com.walmart.entities.Reservation;
import com.walmart.entities.SeatHoldReservation;
import com.walmart.exception.LevelNotFoundException;
import com.walmart.exception.ReservationException;
import com.walmart.exception.SeatHoldNotFoundException;
import com.walmart.model.SeatHold;
import com.walmart.repository.LevelRepository;
import com.walmart.repository.ReservationRepository;
import com.walmart.repository.SeatHoldReservationRepository;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    LeveLService levelService;

    @Autowired
    LevelRepository levelRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    SeatHoldReservationRepository seatHoldReservationRepository;

    @Autowired
    CacheManager cacheManager;

    /**
     * The number of seats in the requested level that are neither held nor
     * reserved
     *
     * @param venueLevel
     *            a numeric venue level identifier to limit the search
     * @return the number of tickets available on the provided level
     * @throws LevelNotFoundException
     */

    @Override
    public int numSeatsAvailable(Optional<Integer> venueLevel) throws LevelNotFoundException {

        if (venueLevel.isPresent()) {
            LevelDetail levelDetail = levelService.findLeveLById(venueLevel.get());
            if (levelDetail == null) {
                throw new LevelNotFoundException("Level " + venueLevel.get() + " not found");
            }

            return levelDetail.getRemainingSeats();
        } else {
            return levelService.getAllSeats();
        }

    }

    /**
     * Find and hold the best available seats for a customer
     *
     * @param numSeats
     *            the number of seats to find and hold
     * @param minLevel
     *            the minimum venue level
     * @param maxLevel
     *            the maximum venue level
     * @param customerEmail
     *            unique identifier for the customer
     * @return a SeatHold object identifying the specific seats and related
     *         information
     * @throws LevelNotFoundException
     */
    @Override
    @Cacheable(value = "seatHoldCache", key = "#customerEmail")
    public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
            String customerEmail) throws ReservationException, LevelNotFoundException {
        SeatHold holdSeat = null;

        boolean seatFound = false;
        Optional<Integer> level = minLevel;

        while (!seatFound && level.get() <= maxLevel.get()) {
            if (numSeats < numSeatsAvailable(level)) {
                seatFound = true;
            }
            level = Optional.of(level.get() + 1);
        }

        if (seatFound) {
            holdSeat = holdSeatModel(customerEmail, level.get() - 1, numSeats);
        }

        if (holdSeat != null) {
            SeatHoldReservation seatHoldReservation = new SeatHoldReservation();
            seatHoldReservation.setSeatHoldId(holdSeat.getId());
            seatHoldReservation.setCustomerEmail(customerEmail);
            seatHoldReservationRepository.save(seatHoldReservation);
        }

        return holdSeat;
    }

    /**
     * Commit seats held for a specific customer
     *
     * @param seatHoldId
     *            the seat hold identifier
     * @param customerEmail
     *            the email address of the customer to which the seat hold is
     *            assigned
     * @return a reservation confirmation code
     * @throws SeatHoldNotFoundException
     * 
     */

    @Override
    @Transactional
    public String reserveSeatsHold(int seatHoldId, String customerEmail) throws ReservationException,
            SeatHoldNotFoundException, LevelNotFoundException {

        // seatHold Id and emailId validation
        SeatHoldReservation seatHoldReservation = seatHoldReservationRepository.findBySeatHoldIdAndCustomerEmail(seatHoldId,customerEmail);

        if (seatHoldReservation == null) {
            throw new SeatHoldNotFoundException(" No hold seats are found with seatHoldId" + seatHoldId
                    + " for given email");
        }

        // cache Validation
        SeatHold seatHold = null;
        Cache seatHoldCache = (Cache) cacheManager.getCache("seatHoldCache");
        if (seatHoldCache != null) {
            Element element = seatHoldCache.get(customerEmail);
            if (element != null) {
                seatHold = (SeatHold) element.getObjectValue();
            }
        }

        if (seatHold == null) {
            throw new SeatHoldNotFoundException(" No hold seats are found in cache with seatHoldId - "+ seatHoldId + " and customer emailId - "
                    + customerEmail);
        }
        return reserveSeatsHoldModel(seatHold, seatHoldId, customerEmail);
    }

    @CacheEvict(cacheNames = "seatHoldCache", key = "#customerEmail", beforeInvocation = true)
    public String reserveSeatsHoldModel(SeatHold seatHold, int seatHoldId, String customerEmail)
            throws ReservationException, LevelNotFoundException {
        Reservation reservation = null;

        final LevelDetail levelDetail = levelRepository.Lock(seatHold.getLevel());
        if (levelDetail == null) {
            throw new LevelNotFoundException("Level " + seatHold.getLevel() + " not found");
        }

        if (seatHold.getSeatsHoldCount() < levelDetail.getRemainingSeats()) {

            // make the reservation
            reservation = new Reservation();
            reservation.setCustomerEmail(customerEmail);
            reservation.setSeatHoldId(seatHoldId);
            reservation.setSeatsHoldCount(seatHold.getSeatsHoldCount());
            reservation.setLevel(seatHold.getLevel());
            reservation.setDate(new Date());

            reservation = reservationRepository.save(reservation);

            levelDetail.setRemainingSeats(levelDetail.getRemainingSeats() - seatHold.getSeatsHoldCount());
            levelRepository.save(levelDetail);

        } else {
            throw new ReservationException("Required number of seats are not available in given level");
        }

        return reservation.getId() + "";
    }

    // method to generated unique random number
    public static Integer getRandomCode() {
        final int randomRange = 1632960; // Math.pow(36,4) - 46656;

        return ThreadLocalRandom.current().nextInt(randomRange) + 46656; // Random
                                                                         // value
    }

    public SeatHold holdSeatModel(String email, Integer level, Integer seatsHoldCount) {
        SeatHold seatHoldModel = new SeatHold();
        seatHoldModel.setId(getRandomCode());
        seatHoldModel.setLevel(level);
        seatHoldModel.setSeatsHoldCount(seatsHoldCount);
        seatHoldModel.setCustomerEmail(email);
        seatHoldModel.setDate(new Date());
        return seatHoldModel;
    }

}
