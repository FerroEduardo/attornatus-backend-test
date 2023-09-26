package com.ferroeduardo.attornatustest.config;

import com.ferroeduardo.attornatustest.entity.Address;
import com.ferroeduardo.attornatustest.entity.Person;
import com.ferroeduardo.attornatustest.service.AddressService;
import com.ferroeduardo.attornatustest.service.PersonService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
/**
 * To initialize the database, add the CLI argument `--initialize-database`
 */
public class DatabaseInitializer implements ApplicationRunner {

    private final PersonService  personService;
    private final AddressService addressService;

    public DatabaseInitializer(PersonService personService, AddressService addressService) {
        this.personService = personService;
        this.addressService = addressService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!args.containsOption("initialize-database")) return;
        Person person = new Person();
        person.setName("Eduardo");
        person.setBirthDate(LocalDate.of(2000, 8, 29));
        person = personService.save(person);

        Address address = new Address();
        address.setNumber("123-ABC");
        address.setLogradouro("Rua de Tal");
        address.setCity("Rio de Janeiro");
        address.setCep("12345678");
        address = addressService.save(person.getId(), address);
        addressService.setMainAddress(person.getId(), address.getId());

        person = new Person();
        person.setName("attornatus");
        person.setBirthDate(LocalDate.of(2018, 1, 1));
        person = personService.save(person);
    }
}
