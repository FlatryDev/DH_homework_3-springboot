package ru.digitalhabits.homework3.web;

import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.model.PersonFullResponse;
import ru.digitalhabits.homework3.model.PersonRequest;
import ru.digitalhabits.homework3.model.PersonShortResponse;
import ru.digitalhabits.homework3.service.PersonService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

    private final String URL = "/api/v1/persons/";
    private final int TESTID = 1;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PersonService personService;

    private Person getRandomPerson() {
        return new Person()
                .setFirstName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                .setMiddleName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                .setLastName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                .setAge(RandomUtils.nextInt(18, 65))
                ;
    }

    @Test
    void persons() throws Exception {
        List<PersonShortResponse> expectedResult = new ArrayList<>();
        for(int i = 0; i < RandomUtils.nextInt(5,10); i++) {
            expectedResult.add(new PersonShortResponse().getFromPerson(getRandomPerson().setId(i+1)));
        }

        when(personService.findAll()).thenReturn(expectedResult);

        mockMvc.perform(get(URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                ;
    }

    @Test
    void person() throws Exception {
        Person person = getRandomPerson().setId(TESTID);
        PersonFullResponse expectedResult = new PersonFullResponse().getFromPerson(person);
        when(personService.getById(TESTID)).thenReturn(expectedResult);
        mockMvc.perform(get(URL+TESTID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value(person.getFullName()))
                .andExpect(jsonPath("$.age").value(person.getAge()))
        ;
    }

    @Test
    void createPerson() throws Exception {
        Person person = getRandomPerson().setId(TESTID);
        PersonRequest request = new PersonRequest()
                .setFirstName(person.getFirstName())
                .setMiddleName(person.getMiddleName())
                .setLastName(person.getLastName())
                .setAge(person.getAge());
        when(personService.create(request)).thenReturn(person.getId());
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
                )
                .andExpect(status().isCreated())
       ;
    }

    @Test
    void updatePerson() throws Exception {
        Person person = getRandomPerson().setId(TESTID);
        PersonRequest request = new PersonRequest()
                .setFirstName(person.getFirstName())
                .setMiddleName(person.getMiddleName())
                .setLastName(person.getLastName())
                .setAge(person.getAge());
        PersonFullResponse expectedResult = new PersonFullResponse().getFromPerson(person);

        when (personService.update(person.getId(),request)).thenReturn(expectedResult);

        mockMvc.perform(patch(URL+TESTID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(person.getId()))
                .andExpect(jsonPath("$.fullName").value(person.getFullName()))
                .andExpect(jsonPath("$.age").value(person.getAge()))
                ;
    }

    @Test
    void deletePerson() throws Exception {
        mockMvc.perform(delete(URL+TESTID))
                .andExpect(status().is(204));
    }
}