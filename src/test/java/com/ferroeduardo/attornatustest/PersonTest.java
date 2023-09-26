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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PersonTest {

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
    void index() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/person")).andExpect(status().isOk()).andReturn();
        JsonNode  root      = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertTrue(root.isArray());
        assertEquals(1, root.size());

        JsonNode child = root.get(0);
        assertNotNull(child.get("id"));
        assertEquals("Eduardo", child.get("name").asText());
        assertEquals(DateTimeFormatter.ISO_DATE.format(LocalDate.of(2000, 8, 29)), child.get("birthDate").asText());

        JsonNode mainAddress = child.get("mainAddress");
        assertNotNull(mainAddress);
        assertEquals(1, mainAddress.get("id").asInt());
        assertEquals("123-ABC", mainAddress.get("number").asText());
        assertEquals("Rua de Tal", mainAddress.get("logradouro").asText());
        assertEquals("Rio de Janeiro", mainAddress.get("city").asText());
        assertEquals("12345678", mainAddress.get("cep").asText());

        JsonNode addresses = child.get("addresses");
        assertNull(addresses);
    }

    @Test
    void show() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/person/1")).andExpect(status().isOk()).andReturn();
        JsonNode  root      = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertFalse(root.isArray());

        assertEquals("Eduardo", root.get("name").asText());
        assertEquals(DateTimeFormatter.ISO_DATE.format(LocalDate.of(2000, 8, 29)), root.get("birthDate").asText());

        JsonNode mainAddress = root.get("mainAddress");
        assertNotNull(mainAddress);
        assertEquals(1, mainAddress.get("id").asInt());
        assertEquals("123-ABC", mainAddress.get("number").asText());
        assertEquals("Rua de Tal", mainAddress.get("logradouro").asText());
        assertEquals("Rio de Janeiro", mainAddress.get("city").asText());
        assertEquals("12345678", mainAddress.get("cep").asText());

        JsonNode addresses = root.get("addresses");
        assertNotNull(addresses);
        assertEquals(1, addresses.size());

        JsonNode address = addresses.get(0);
        assertNotNull(address);
        assertEquals(1, address.get("id").asInt());
        assertEquals("123-ABC", address.get("number").asText());
        assertEquals("Rua de Tal", address.get("logradouro").asText());
        assertEquals("Rio de Janeiro", address.get("city").asText());
        assertEquals("12345678", address.get("cep").asText());
    }

    @Test
    void save() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(
                        post("/person")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                 {
                                                     "name": "attornatus",
                                                     "birthDate": "2018-01-01"
                                                 }
                                                 """)
                )
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertFalse(root.isArray());

        assertNotNull(root.get("id"));
        assertEquals("attornatus", root.get("name").asText());
        assertEquals(DateTimeFormatter.ISO_DATE.format(LocalDate.of(2018, 1, 1)), root.get("birthDate").asText());

        JsonNode mainAddress = root.get("mainAddress");
        assertNotNull(mainAddress);

        JsonNode addresses = root.get("addresses");
        assertNotNull(addresses);
    }

    @Test
    void saveInvalidBody() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(
                        post("/person")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                 {
                                                     "name": null,
                                                     "birthDate": null
                                                 }
                                                 """)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        JsonNode root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertFalse(root.isArray());
        assertEquals("must not be blank", root.get("name").asText());
        assertEquals("must not be null", root.get("birthDate").asText());

        mvcResult = mockMvc
                .perform(
                        post("/person")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                 {
                                                     "name": "eduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardo",
                                                     "birthDate": "2000-08-29"
                                                 }
                                                 """)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertFalse(root.isArray());
        assertEquals("size must be between 1 and 100", root.get("name").asText());

        mvcResult = mockMvc
                .perform(
                        post("/person")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                 {
                                                     "name": "eduardo",
                                                     "birthDate": "2000-08-99"
                                                 }
                                                 """)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertFalse(root.isArray());
        assertEquals("invalid format", root.get("birthDate").asText());
    }

    @Test
    void update() throws Exception {
        MvcResult showMvcResult = mockMvc.perform(get("/person/1")).andExpect(status().isOk()).andReturn();
        JsonNode  showRoot      = objectMapper.readTree(showMvcResult.getResponse().getContentAsString());
        assertFalse(showRoot.isArray());

        assertNotNull(showRoot.get("id"));
        assertEquals("Eduardo", showRoot.get("name").asText());
        assertEquals(DateTimeFormatter.ISO_DATE.format(LocalDate.of(2000, 8, 29)), showRoot.get("birthDate").asText());

        Person person = new Person();
        person.setName("attornatus");
        person.setBirthDate(LocalDate.of(2018, 1, 1));
        String body     = objectMapper.writeValueAsString(person);
        int    personId = showRoot.get("id").asInt();

        MvcResult updateMvcResult = mockMvc
                .perform(
                        put("/person/" + personId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk())
                .andReturn();

        JsonNode updateRoot = objectMapper.readTree(updateMvcResult.getResponse().getContentAsString());
        assertFalse(updateRoot.isArray());

        assertNotNull(updateRoot.get("id"));
        assertEquals("attornatus", updateRoot.get("name").asText());
        assertEquals(DateTimeFormatter.ISO_DATE.format(LocalDate.of(2018, 1, 1)), updateRoot.get("birthDate").asText());
    }

    @Test
    void updateInvalidBody() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(
                        put("/person/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                 {
                                                     "name": null,
                                                     "birthDate": null
                                                 }
                                                 """)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        JsonNode root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertFalse(root.isArray());
        assertEquals("must not be blank", root.get("name").asText());
        assertEquals("must not be null", root.get("birthDate").asText());

        mvcResult = mockMvc
                .perform(
                        put("/person/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                 {
                                                     "name": "eduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardoeduardo",
                                                     "birthDate": "2000-08-29"
                                                 }
                                                 """)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertFalse(root.isArray());
        assertEquals("size must be between 1 and 100", root.get("name").asText());

        mvcResult = mockMvc
                .perform(
                        put("/person/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                 {
                                                     "name": "eduardo",
                                                     "birthDate": "2000-08-99"
                                                 }
                                                 """)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertFalse(root.isArray());
        assertEquals("invalid format", root.get("birthDate").asText());
    }

    @Test
    void indexAddresses() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/person/1/address")).andExpect(status().isOk()).andReturn();
        JsonNode  root      = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertTrue(root.isArray());
        assertEquals(1, root.size());

        JsonNode child = root.get(0);
        assertEquals(1, child.get("id").asInt());
        assertEquals("123-ABC", child.get("number").asText());
        assertEquals("Rua de Tal", child.get("logradouro").asText());
        assertEquals("Rio de Janeiro", child.get("city").asText());
        assertEquals("12345678", child.get("cep").asText());
    }

    @Test
    void saveAddresses() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(
                        post("/person/1/address")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                 {
                                                     "logradouro": "Avenida das Palmeiras",
                                                     "cep": "87456321",
                                                     "number": "547-C",
                                                     "city": "São Paulo"
                                                 }
                                                 """)
                )
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode root = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
        assertFalse(root.isArray());

        assertNotNull(root.get("id"));
        assertEquals("Avenida das Palmeiras", root.get("logradouro").asText());
        assertEquals("87456321", root.get("cep").asText());
        assertEquals("547-C", root.get("number").asText());
        assertEquals("São Paulo", root.get("city").asText());
    }

    @Test
    void setMainAddresses() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(
                        post("/person/1/address")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                 {
                                                     "logradouro": "Avenida das Palmeiras",
                                                     "cep": "87456321",
                                                     "number": "547-C",
                                                     "city": "São Paulo"
                                                 }
                                                 """)
                )
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode root = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
        assertFalse(root.isArray());

        assertNotNull(root.get("id"));
        int addressId = root.get("id").asInt();
        assertEquals("Avenida das Palmeiras", root.get("logradouro").asText());
        assertEquals("87456321", root.get("cep").asText());
        assertEquals("547-C", root.get("number").asText());
        assertEquals("São Paulo", root.get("city").asText());


        mockMvc
                .perform(
                        post("/person/1/address/main/" + addressId)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

}
