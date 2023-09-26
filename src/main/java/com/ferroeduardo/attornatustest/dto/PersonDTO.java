package com.ferroeduardo.attornatustest.dto;

import com.ferroeduardo.attornatustest.entity.Person;

import java.time.LocalDate;
import java.util.List;

public record PersonDTO(Long id, String name, LocalDate birthDate, AddressDTO mainAddress, List<AddressDTO> addresses) {
    public static PersonDTO fromEntity(Person person) {
        if (person == null) {
            return null;
        }
        List<AddressDTO> addresses = (person.getAddresses() == null) ? List.of() : person.getAddresses().stream().map(AddressDTO::fromEntity).toList();

        return new PersonDTO(
                person.getId(),
                person.getName(),
                person.getBirthDate(),
                AddressDTO.fromEntity(person.getMainAddress()),
                addresses
        );
    }
}
