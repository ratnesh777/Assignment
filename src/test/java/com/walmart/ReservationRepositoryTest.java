package com.walmart;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.walmart.entities.Reservation;
import com.walmart.repository.ReservationRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { AssignmentApplicationTests.class})
@WebAppConfiguration
public class ReservationRepositoryTest {
   
    @Autowired
    private ReservationRepository reservationRepository;


    @Test
    public void testResevartionById() {
        int levelId = 1;
        Reservation reservation = reservationRepository.findOne(levelId);
        assertNotNull("Reservation should not be null", reservation);
    }
    
    @Test
    public void testGetReservationById() {
        int nonExistentResvId = 111111;
       
        Reservation levelDetail = reservationRepository.findOne(nonExistentResvId);
        assertNull("Reservation should be null", levelDetail);
    }
    
    
}
