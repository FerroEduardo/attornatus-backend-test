package com.ferroeduardo.attornatustest.repository;

import com.ferroeduardo.attornatustest.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}
