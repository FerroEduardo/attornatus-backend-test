package com.ferroeduardo.attornatustest.service;

import com.ferroeduardo.attornatustest.entity.Address;
import com.ferroeduardo.attornatustest.entity.Person;
import com.ferroeduardo.attornatustest.repository.AddressRepository;
import com.ferroeduardo.attornatustest.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;
    private final PersonRepository  personRepository;
    private final Logger logger = LoggerFactory.getLogger(AddressService.class);

    public AddressService(AddressRepository addressRepository, PersonRepository personRepository) {
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
    }

    public List<Address> indexByPersonId(Long personId) {
        return addressRepository.findAllByPersonId(personId);
    }

    @Transactional
    public Address save(Long personId, Address address) {
        Person person = personRepository.getReferenceById(personId);
        address.setPerson(person);

        logger.info("Saving address '{}'", address);
        return addressRepository.save(address);
    }

    public boolean exists(Long addressId) {
        return addressRepository.existsById(addressId);
    }

    @Transactional
    public void setMainAddress(Long personId, Long addressId) {
        Person  person  = personRepository.getReferenceById(personId);
        Address address = addressRepository.getReferenceById(addressId);
        person.setMainAddress(address);

        logger.info("Defining address '{}' as main to person '{}'", addressId, personId);
        personRepository.save(person);
    }

    @Transactional
    public void removeMainAddress(Long personId) {
        personRepository.removeMainAddressByPersonId(personId);
    }

    @Transactional
    public void deleteById(Long addressId) {
        personRepository.removeMainAddressByAddressId(addressId);
        addressRepository.deleteById(addressId);
    }
}
