package ru.digitalhabits.homework3.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import ru.digitalhabits.homework3.dao.DepartmentDao;
import ru.digitalhabits.homework3.dao.PersonDao;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.model.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ComponentScan(basePackages = "ru.digitalhabits.homework3")
@Transactional
class DepartmentServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentDao departmentDao;

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
        List<DepartmentRequest> requestList = new ArrayList<>();
        for(int i = 0; i < RandomUtils.nextInt(5,10); i++)
            requestList.add(getDepartmentRequestRandom());
        List<String> expectedList = requestList.stream()
                .map(request -> String.valueOf(departmentService.create(request)) + " " + request.getName())
                .collect(Collectors.toList());
        List<DepartmentShortResponse> responseFind = departmentService.findAll();
        assertTrue(
                responseFind.stream()
                        .map(resp -> resp.getId() + " " + resp.getName())
                        .collect(Collectors.toList())
                        .containsAll(expectedList)
        );
    }

    @Test
    void findById() {
        DepartmentRequest request = getDepartmentRequestRandom();
        DepartmentFullResponse response = departmentService.getById(departmentService.create(request));
        assertEquals(request.getName(), response.getName());
    }

    @Test
    void create() {
        DepartmentRequest request = getDepartmentRequestRandom();
        Department department = departmentDao.findById(departmentService.create(request));
        assertEquals(request.getName(), department.getName());
    }

    @Test
    void update() {
        DepartmentRequest request = getDepartmentRequestRandom();
        int departmentId = departmentService.create(request);
        request.setName("Name 1");
        departmentService.update(departmentId, request);
        assertEquals(request.getName(), departmentService.getById(departmentId).getName());
    }

    @Test
    void delete() {
        DepartmentRequest request = getDepartmentRequestRandom();
        int departmentId = departmentService.create(request);
        departmentService.delete(departmentId);

        boolean isDeleted = false;
        try {
            departmentService.getById(departmentId);
        }
        catch(EntityNotFoundException e) {
            isDeleted = true;
        }
        assertTrue(isDeleted);
    }

    @Test
    void close() {
        PersonRequest request = getPersonRequestRandom();
        int personId = personService.create(request);
        DepartmentRequest departmentRequest = getDepartmentRequestRandom();
        int departmentId = departmentService.create(departmentRequest);
        personService.addPersonToDepartment(departmentId, personId);
        assertNotNull(personService.getById(personId).getDepartment());
        assertFalse(departmentService.getById(departmentId).getPersons().isEmpty());

        departmentService.close(departmentId);
        DepartmentFullResponse response = departmentService.getById(departmentId);
        assertTrue(response.isClosed());
        assertTrue(response.getPersons().isEmpty());
        assertNull(personService.getById(personId).getDepartment());

    }
}