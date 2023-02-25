package ru.digitalhabits.homework3.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import ru.digitalhabits.homework3.dao.PersonDao;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.model.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = "ru.digitalhabits.homework3")
@Transactional
class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PersonDao personDao;

    private PersonRequest getPersonRequestRandom () {
        return new PersonRequest()
                .setFirstName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                .setMiddleName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                .setLastName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                .setAge(RandomUtils.nextInt(18, 65))
                ;
    }
    private DepartmentRequest getDepartmentRequestRandom () {
        return new DepartmentRequest()
                .setName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                ;
    }

    @Test
    void findAll() {
        List<PersonRequest> requestList = new ArrayList<>();
        for(int i=0;i < RandomUtils.nextInt(5,10);i++)
            requestList.add(getPersonRequestRandom());
        List<String> expectedList = requestList.stream()
                .map(request -> String.valueOf(personService.create(request)) + " " + request.getFirstName() + " " + request.getMiddleName() + " " + request.getLastName())
                .collect(Collectors.toList());
        List<PersonShortResponse> responseFind = personService.findAll();
        assertTrue(
                responseFind.stream()
                        .map(resp -> resp.getId() + " " + resp.getFullName())
                        .collect(Collectors.toList())
                        .containsAll(expectedList)
        );
    }

    @Test
    void findById() {
        PersonRequest request = getPersonRequestRandom();
        PersonFullResponse response = personService.getById(personService.create(request));
        assertEquals(request.getFirstName() + " " + request.getMiddleName() + " " + request.getLastName() + " " + request.getAge(),
                response.getFullName() + " " + response.getAge()
        );
    }

    @Test
    void create() {
        PersonRequest request = getPersonRequestRandom();
        Person person = personDao.findById(personService.create(request));
        assertEquals(request.getFirstName() + " " + request.getMiddleName() + " " + request.getLastName() + " " + request.getAge(),
                person.getFullName() + " " + person.getAge()
                );
    }

    @Test
    void update() {
        PersonRequest request = getPersonRequestRandom();
        int personId = personService.create(request);
        request.setFirstName("FirstName 1")
               .setMiddleName("MiddleName 2")
               .setLastName("LastName 3");
        personService.update(personId, request);
        assertEquals(request.getFirstName() + " " + request.getMiddleName() + " " + request.getLastName(),
                personService.getById(personId).getFullName());
    }

    @Test
    void delete() {
        PersonRequest request = getPersonRequestRandom();
        int personId = personService.create(request);
        personService.delete(personId);

        boolean isDeleted = false;
        try {
            personService.getById(personId);
        }
        catch(EntityNotFoundException e) {
            isDeleted = true;
        }
        assertTrue(isDeleted);
    }

    @Test
    void addPersonToDepartment() {
        PersonRequest request = getPersonRequestRandom();
        int personId = personService.create(request);
        DepartmentRequest departmentRequest = getDepartmentRequestRandom();
        int departmentId = departmentService.create(departmentRequest);
        personService.addPersonToDepartment(departmentId, personId);
        assertEquals(departmentId, personService.getById(personId).getDepartment().getId());
        assertFalse(departmentService.getById(departmentId).getPersons().isEmpty());
    }

    @Test
    void removePersonFromDepartment() {
        PersonRequest request = getPersonRequestRandom();
        int personId = personService.create(request);
        DepartmentRequest departmentRequest = getDepartmentRequestRandom();
        int departmentId = departmentService.create(departmentRequest);
        personService.addPersonToDepartment(departmentId, personId);
        assertNotNull(personService.getById(personId).getDepartment());
        assertFalse(departmentService.getById(departmentId).getPersons().isEmpty());

        personService.removePersonFromDepartment(departmentId,personId);
        assertNull(personService.getById(personId).getDepartment());
        assertTrue(departmentService.getById(departmentId).getPersons().isEmpty());
    }
}