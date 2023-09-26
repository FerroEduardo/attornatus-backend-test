package com.ferroeduardo.attornatustest.controller;

import com.ferroeduardo.attornatustest.dto.AddressDTO;
import com.ferroeduardo.attornatustest.dto.IndexPersonDTO;
import com.ferroeduardo.attornatustest.dto.PersonDTO;
import com.ferroeduardo.attornatustest.entity.Address;
import com.ferroeduardo.attornatustest.entity.Person;
import com.ferroeduardo.attornatustest.request.EditPersonRequest;
import com.ferroeduardo.attornatustest.request.SavePersonAddressRequest;
import com.ferroeduardo.attornatustest.request.SavePersonRequest;
import com.ferroeduardo.attornatustest.service.AddressService;
import com.ferroeduardo.attornatustest.service.PersonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("person")
@Validated
public class PersonController {

    private final PersonService  personService;
    private final AddressService addressService;

    public PersonController(PersonService personService, AddressService addressService) {
        this.personService = personService;
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<IndexPersonDTO>> index() {
        List<IndexPersonDTO> body = personService.index().stream().map(IndexPersonDTO::fromEntity).toList();

        return ResponseEntity.ok(body);
    }

    @GetMapping("{personId}")
    public ResponseEntity<PersonDTO> index(@Valid @PathVariable @NotNull @PositiveOrZero Long personId) {
        Optional<Person> result = personService.show(personId);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        PersonDTO body = PersonDTO.fromEntity(result.get());

        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<PersonDTO> save(@Valid @RequestBody SavePersonRequest request) {
        Person person = new Person();
        person.setName(request.getName());
        person.setBirthDate(request.getBirthDate());
        PersonDTO body = PersonDTO.fromEntity(personService.save(person));

        URI location = URI.create("/person/" + person.getId());

        return ResponseEntity.created(location).body(body);
    }

    @PutMapping("{personId}")
    public ResponseEntity<PersonDTO> edit(
            @Valid @PathVariable @NotNull @PositiveOrZero Long personId,
            @Valid @RequestBody EditPersonRequest request
    ) {
        if (!personService.exists(personId)) {
            return ResponseEntity.notFound().build();
        }
        Person person = new Person();
        person.setId(personId);
        person.setName(request.getName());
        person.setBirthDate(request.getBirthDate());
        PersonDTO body = PersonDTO.fromEntity(personService.save(person));

        return ResponseEntity.ok(body);
    }

    @GetMapping("{personId}/address")
    public ResponseEntity<Object> indexAddress(
            @Valid @PathVariable @NotNull @PositiveOrZero Long personId
    ) {
        if (!personService.exists(personId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "person not found"));
        }
        List<AddressDTO> body = addressService.indexByPersonId(personId).stream().map(AddressDTO::fromEntity).toList();

        return ResponseEntity.ok(body);
    }

    @PostMapping("{personId}/address")
    public ResponseEntity<Object> saveAddress(
            @Valid @PathVariable @NotNull @PositiveOrZero Long personId,
            @Valid @RequestBody SavePersonAddressRequest request
    ) {
        if (!personService.exists(personId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "person not found"));
        }
        Address address = new Address();
        address.setCep(request.getCep());
        address.setCity(request.getCity());
        address.setLogradouro(request.getLogradouro());
        address.setNumber(request.getNumber());

        AddressDTO body = AddressDTO.fromEntity(addressService.save(personId, address));

        URI location = URI.create("/address/" + address.getId());

        return ResponseEntity.created(location).body(body);
    }

    @PostMapping("{personId}/address/main/{addressId}")
    public ResponseEntity<Object> setMainAddress(
            @Valid @PathVariable @NotNull @PositiveOrZero Long personId,
            @Valid @PathVariable @NotNull @PositiveOrZero Long addressId
    ) {
        if (!personService.exists(personId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "person not found"));
        }
        if (!addressService.exists(addressId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "address not found"));
        }

        addressService.setMainAddress(personId, addressId);

        return ResponseEntity.noContent().build();
    }
}
