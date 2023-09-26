package com.ferroeduardo.attornatustest.dto;

import com.ferroeduardo.attornatustest.entity.Person;

import java.time.LocalDate;

public record IndexPersonDTO(Long id, String name, LocalDate birthDate, AddressDTO mainAddress) {
    public static IndexPersonDTO fromEntity(Person person) {
        if (person == null) {
            return null;
        }

        return new IndexPersonDTO(
                person.getId(),
                person.getName(),
                person.getBirthDate(),
                AddressDTO.fromEntity(person.getMainAddress())
        );
    }
}
