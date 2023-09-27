package com.ferroeduardo.attornatustest.controller;

import com.ferroeduardo.attornatustest.dto.AddressDTO;
import com.ferroeduardo.attornatustest.entity.Address;
import com.ferroeduardo.attornatustest.request.AddressValidationRequest;
import com.ferroeduardo.attornatustest.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("address")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @DeleteMapping("{addressId}")
    public ResponseEntity<Object> deleteAddress(@Valid @PathVariable @NotNull @PositiveOrZero Long addressId) {
        if (!service.exists(addressId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "address not found"));
        }

        service.deleteById(addressId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{addressId}")
    public ResponseEntity<Object> updateAddress(
            @Valid @PathVariable @NotNull @PositiveOrZero Long addressId,
            @Valid @RequestBody AddressValidationRequest request
    ) {
        Optional<Address> addressOptional = service.findById(addressId);
        if (addressOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "address not found"));
        }

        Address address = addressOptional.get();
        address.setCity(request.getCity());
        address.setCep(request.getCep());
        address.setLogradouro(request.getLogradouro());
        address.setNumber(request.getNumber());
        AddressDTO addressDTO = AddressDTO.fromEntity(service.update(address));

        return ResponseEntity.ok(addressDTO);
    }
}
