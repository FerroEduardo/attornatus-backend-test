package com.ferroeduardo.attornatustest.dto;

import com.ferroeduardo.attornatustest.entity.Address;

public record AddressDTO(Long id, String logradouro, String cep, String number, String city) {
    public static AddressDTO fromEntity(Address address) {
        if (address == null) {
            return null;
        }
        return new AddressDTO(
                address.getId(),
                address.getLogradouro(),
                address.getCep(),
                address.getNumber(),
                address.getCity()
        );
    }
}
