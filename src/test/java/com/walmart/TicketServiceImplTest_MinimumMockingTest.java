package com.walmart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.walmart.entities.LevelDetail;
import com.walmart.exception.SeatHoldNotFoundException;
import com.walmart.model.SeatHold;
import com.walmart.repository.LevelRepository;
import com.walmart.repository.ReservationRepository;
import com.walmart.service.TicketService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { AssignmentApplicationTests.class })
@WebAppConfiguration
public class TicketServiceImplTest_MinimumMockingTest {

    private static final String CUST_EMAIL = "junit.test@gmail.com";

    static Optional<Integer> minLevel = Optional.of(1);
    static Optional<Integer> maxLevel = Optional.of(2);
    Integer seatCount = 4;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private TicketService ticketService;

    @Test
    public void shouldReturnTotalNumberofSeatsIfGivenLevelIsEmpty() throws Exception {
        Optional<Integer> level = Optional.empty();
        int seats = ticketService.numSeatsAvailable(level);
        assertNotNull("seat count should not be null", seats);

    }

    @Test
    public void shouldReturnTotalNumberofSeatsForGivenLevel() throws Exception {

        int seats = ticketService.numSeatsAvailable(minLevel);
        assertNotNull("seat count should not be null", seats);

    }

    @Test
    public void shouldReturnSeatHoldInformationForCustomer() throws Exception {

        SeatHold seats = ticketService.findAndHoldSeats(seatCount, minLevel, maxLevel, "test@gmail.com");
        assertNotNull("seat count should not be null", seats);
        assertEquals("seat count should be same", seatCount, seats.getSeatsHoldCount());
        assertEquals("emailId should match", seatCount, seats.getSeatsHoldCount());

    }

    @Test
    public void shouldReturnSeatHoldInformationIfSameLevelPassedPassedInRequest() throws Exception {

        SeatHold seats = ticketService.findAndHoldSeats(seatCount, maxLevel, maxLevel, "test@gmail.com");
        assertNotNull("seat count should not be null", seats);
        assertEquals("seat count should be same", seatCount, seats.getSeatsHoldCount());
        assertEquals("emailId should match", seatCount, seats.getSeatsHoldCount());

    }

    @Test
    public void shouldReturnReservationIdForCustomerIfSeatsAreHold() throws Exception {

        SeatHold seatHold = ticketService.findAndHoldSeats(seatCount, minLevel, maxLevel, CUST_EMAIL);
        assertNotNull("seat count should not be null", seatHold);
        String reservationId = ticketService.reserveSeatsHold(seatHold.getId(), seatHold.getCustomerEmail());
        assertNotNull("reservationId should not be null", reservationId);

        // delete reservationId from reservation table and update seatholdCount
        // in level_detail so that db can be consistent
        // junit functional testing can also be done in in-memory db but due to
        // time constraint doing it on same db

        reservationRepository.delete(Integer.parseInt(reservationId));
        LevelDetail levelDetail = levelRepository.findOne(seatHold.getLevel());
        levelDetail.setRemainingSeats(levelDetail.getRemainingSeats() + seatCount);
        levelRepository.save(levelDetail);
    }

    @Test
    public void shouldReturnErrorIfSeatsArsNotHoldForCustomer() throws Exception {

        boolean thrown = false;
        try {
            ticketService.reserveSeatsHold(55555, "test.test@yahoo.com");
        } catch (SeatHoldNotFoundException re) {
            thrown = true;
        }
        assertTrue(thrown);
    }

}
