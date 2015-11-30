package com.walmart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.walmart.entities.LevelDetail;
import com.walmart.repository.LevelRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { AssignmentApplicationTests.class })
@WebAppConfiguration
public class LevelRepositoryTest {

    @Autowired
    private LevelRepository levelRepository;

    @Test
    public void testGetLevelById() {
        int levelId = 1;
        Integer totalSeats = 1250;

        LevelDetail levelDetail = levelRepository.findOne(levelId);
        assertNotNull("LevelDetail should not be null", levelDetail);
        assertEquals("Total seat should match ", totalSeats, levelDetail.getTotalSeats());
    }

    @Test
    public void testGetNonExistentLevelById() {
        int nonExistentLevelId = 1111;

        LevelDetail levelDetail = levelRepository.findOne(nonExistentLevelId);
        assertNull("LevelDetail should be null", levelDetail);
    }

}
