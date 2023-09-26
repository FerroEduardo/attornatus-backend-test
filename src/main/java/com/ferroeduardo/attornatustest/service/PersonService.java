package com.ferroeduardo.attornatustest.service;

import com.ferroeduardo.attornatustest.entity.Person;
import com.ferroeduardo.attornatustest.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> index() {
        return repository.findAll();
    }

    public Optional<Person> show(Long id) {
        return repository.findById(id);
    }

    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Transactional
    public Person save(Person person) {
        return repository.save(person);
    }
}
