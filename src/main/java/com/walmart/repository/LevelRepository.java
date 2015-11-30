package com.walmart.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.walmart.entities.LevelDetail;

@Repository
public interface LevelRepository extends CrudRepository<LevelDetail, Integer> {
    
   /* @Lock(LockModeType.OPTIMISTIC)
    @Query("select s from LevelDetail s where s.id=:levelId")
    LevelDetail readLock(@Param("levelId") final Integer levelId);*/
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from LevelDetail s where s.id=:levelId")
    LevelDetail Lock(@Param("levelId") final Integer levelId);

    @Query("select sum(s.remainingSeats) from LevelDetail s")
    Integer getAllSeats();
}
