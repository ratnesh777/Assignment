package com.walmart.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.entities.Reservation;
import com.walmart.exception.InvalidRequestException;
import com.walmart.exception.LevelNotFoundException;
import com.walmart.exception.ReservationException;
import com.walmart.exception.ReservationNotFoundException;
import com.walmart.exception.SeatHoldNotFoundException;
import com.walmart.model.SeatHold;
import com.walmart.service.LeveLService;
import com.walmart.service.TicketService;

@RestController
@RequestMapping(value = MovieReservationController.REQUEST_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieReservationController {

    public static final String REQUEST_MAPPING = "/assignment";

    @Autowired
    private LeveLService levelService;

    @Autowired
    private TicketService ticketService;

    @RequestMapping(value = "/findSeats", method = RequestMethod.GET)
    public Integer numSeatAvailable(@RequestParam(required = false, value = "levelId") Optional<Integer> levelId ) throws LevelNotFoundException {
        return ticketService.numSeatsAvailable(levelId);
    }
   
    @RequestMapping(value = "/seatsHolds", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SeatHold> findAndHoldBooking(@RequestBody @Valid SeatHold seatHoldModel,
            BindingResult bindingResult) throws ReservationException, LevelNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Invalid create request", bindingResult);
        }
        Optional<Integer> optional = Optional.of(seatHoldModel.getLevel());
        Optional<Integer> optionalMax = Optional.of(4); //HardCoded
        SeatHold seatHold = ticketService.findAndHoldSeats(seatHoldModel.getSeatsHoldCount(), optional,
                optionalMax, seatHoldModel.getCustomerEmail());

        seatHold.setId(seatHold.getId());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", generateSeatHoldResourceUri(seatHold.getId()));
        return new ResponseEntity<SeatHold>(seatHold, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/reservation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reservation> ticketReservation(@RequestBody @Valid Reservation reservation,
            BindingResult bindingResult) throws ReservationException, SeatHoldNotFoundException, LevelNotFoundException, ReservationNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Invalid create request", bindingResult);
        }

        String reservationId = ticketService.reserveSeatsHold(reservation.getSeatHoldId(),
                reservation.getCustomerEmail());
        //reservation.setId(Integer.parseInt(reservationId)); updated to get all information
        reservation=levelService.findReservationById(reservationId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", generateReservationResourceUri(reservationId));
        return new ResponseEntity<Reservation>(reservation, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.GET)
    public Reservation findReservationById(@PathVariable String id) throws ReservationNotFoundException {
        return levelService.findReservationById(id);
    }
    
    private String generateSeatHoldResourceUri(Integer id) {
        return REQUEST_MAPPING + "/seatHolds/" + id;
    }

    private String generateReservationResourceUri(String resId) {
        return REQUEST_MAPPING + "/reservation/" + resId;
    }
}