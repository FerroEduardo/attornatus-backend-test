package com.ferroeduardo.attornatustest.repository;

import com.ferroeduardo.attornatustest.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Modifying
    @Query("update Person p set p.mainAddress = null where p.id = :personId")
    void removeMainAddressByPersonId(@Param("personId") Long personId);
}
