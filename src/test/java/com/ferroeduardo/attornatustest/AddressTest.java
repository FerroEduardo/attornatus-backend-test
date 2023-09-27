package com.ferroeduardo.attornatustest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferroeduardo.attornatustest.entity.Address;
import com.ferroeduardo.attornatustest.entity.Person;
import com.ferroeduardo.attornatustest.service.AddressService;
import com.ferroeduardo.attornatustest.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AddressTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonService personService;

    @Autowired
    private AddressService addressService;

    @BeforeEach
    void init() {
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
    }

    @Test
    void removeAddress() throws Exception {
        // Delete address
        String addressId = "1";
        MvcResult mvcResult = mockMvc
                .perform(delete("/address/" + addressId))
                .andExpect(status().isNoContent())
                .andReturn();
        JsonNode root = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
        assertFalse(root.isArray());

        // Check user exists and address was removed
        mvcResult = mockMvc.perform(get("/person/1")).andExpect(status().isOk()).andReturn();
        root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertFalse(root.isArray());

        JsonNode mainAddress = root.get("mainAddress");
        assertTrue(mainAddress.isNull());

        JsonNode addresses = root.get("addresses");
        assertFalse(addresses.isNull());
        assertTrue(addresses.isArray());
        assertTrue(addresses.isEmpty());
    }
}
