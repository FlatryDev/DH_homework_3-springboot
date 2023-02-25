package ru.digitalhabits.homework3.web;

import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.model.*;
import ru.digitalhabits.homework3.service.DepartmentService;
import ru.digitalhabits.homework3.service.PersonService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DepartmentController.class)
class DepartmentControllerTest {
    private final String URL = "/api/v1/departments/";
    private final String URL_ONE = "/api/v1/departments/{id}";
    private final String URL_CLOSE = "/api/v1/departments/{id}/close";
    private final String URL_DOUBLE_OPER = URL + "{id}/{personId}";
    private final int TESTID_DEPARTMENT = 1;
    private final int TESTID_PERSON = 1;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepartmentService departmentService;
    @MockBean
    private PersonService personService;

    private Department getRandomDepartment() {
        return new Department()
                .setName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                ;
    }

    @Test
    void departments() throws Exception {
        List<DepartmentShortResponse> expectedResult = new ArrayList<>();
        for(int i = 0; i < RandomUtils.nextInt(5,10); i++) {
            expectedResult.add(new DepartmentShortResponse().getFromDepartment(getRandomDepartment().setId(i+1)));
        }

        when(departmentService.findAll()).thenReturn(expectedResult);

        mockMvc.perform(get(URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
        ;
    }

    @Test
    void department() throws Exception {
        Department department = getRandomDepartment().setId(TESTID_DEPARTMENT);
        DepartmentFullResponse expectedResult = new DepartmentFullResponse().getFromDepartment(department);
        when(departmentService.getById(TESTID_DEPARTMENT)).thenReturn(expectedResult);
        mockMvc.perform(get(URL_ONE,TESTID_DEPARTMENT).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(department.getName()))
        ;
    }

    @Test
    void createDepartment() throws Exception {
        Department department = getRandomDepartment().setId(TESTID_DEPARTMENT);
        DepartmentRequest request = new DepartmentRequest()
                .setName(department.getName());
        when(departmentService.create(request)).thenReturn(department.getId());
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
                )
                .andExpect(status().isCreated())
        ;
    }

    @Test
    void updateDepartment() throws Exception {
        Department department = getRandomDepartment().setId(TESTID_DEPARTMENT);
        DepartmentRequest request = new DepartmentRequest()
                .setName(department.getName());
        DepartmentFullResponse expectedResult = new DepartmentFullResponse().getFromDepartment(department);

        when (departmentService.update(department.getId(),request)).thenReturn(expectedResult);

        mockMvc.perform(patch(URL_ONE,TESTID_DEPARTMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(department.getId()))
                .andExpect(jsonPath("$.name").value(department.getName()))
        ;
    }

    @Test
    void deleteDepartment() throws Exception {
        mockMvc.perform(delete(URL_ONE,TESTID_DEPARTMENT))
                .andExpect(status().is(204));
    }

    @Test
    void addPersonToDepartment() throws Exception {
        mockMvc.perform(post(URL_DOUBLE_OPER,TESTID_DEPARTMENT,TESTID_PERSON))
                .andExpect(status().is(204));
    }

    @Test
    void removePersonToDepartment() throws Exception {
        mockMvc.perform(delete(URL_DOUBLE_OPER,TESTID_DEPARTMENT,TESTID_PERSON))
                .andExpect(status().is(204));
    }

    @Test
    void closeDepartment() throws Exception {
        mockMvc.perform(post(URL_CLOSE,TESTID_DEPARTMENT))
                .andExpect(status().is(204));
    }
}