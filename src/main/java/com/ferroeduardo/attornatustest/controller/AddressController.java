package com.ferroeduardo.attornatustest.controller;

import com.ferroeduardo.attornatustest.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
}
