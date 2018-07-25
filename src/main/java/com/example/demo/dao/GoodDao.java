package com.example.demo.dao;

import com.example.demo.domain.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

public interface GoodDao extends JpaRepository<Good,String> {

    /**
     * 悲观锁
     * @param id
     * @return
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select g from Good g where g.id = :id")
    Good findLockForUpdate(@Param("id") String id);

    /**
     * 乐观锁
     * @param goodNumber
     * @param id
     * @return
     */
    @Query(value = "update good g set g.good_number=?1, g.version = g.version +1 where g.id=?2 and g.version = ?3", nativeQuery=true)
    @Modifying
    @Transactional
    int updateGoodNumberById(Integer goodNumber, String id, Integer version);

}
