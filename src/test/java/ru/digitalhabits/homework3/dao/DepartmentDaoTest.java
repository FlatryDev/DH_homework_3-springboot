package ru.digitalhabits.homework3.dao;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ru.digitalhabits.homework3.domain.Department;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ComponentScan(basePackages = "ru.digitalhabits.homework3")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class DepartmentDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DepartmentDao departmentDao;

    private Department getRandomDepartment() {
        return new Department()
                .setName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                ;
    }
    private Department getDepartmentCreated(Department department) {
        return departmentDao.update(department);
    }
    private Department getDepartmentRandomCreated() {
        return getDepartmentCreated(getRandomDepartment());
    }

    @Test
    void findById() {
        Department departmentRandom = getDepartmentRandomCreated();
        Department departmentFind = departmentDao.findById(departmentRandom.getId());
        assertEquals(departmentFind, departmentRandom);
    }

    @Test
    void findAll() {
        List<Department> departmentList = new ArrayList<>();
        for(int i=0;i < RandomUtils.nextInt(5,10);i++)
            departmentList.add(getDepartmentRandomCreated());
        List<Department> departmentFind = departmentDao.findAll();
        assertTrue(departmentFind.containsAll(departmentList));
        assertTrue(departmentList.containsAll(departmentFind));
    }

    @Test
    void update() {
        Department departmentRandom = getDepartmentRandomCreated();
        departmentRandom
                .setName("Department 1")
                ;
        departmentDao.update(departmentRandom);
        Department departmentFind = departmentDao.findById(departmentRandom.getId());
        assertEquals(departmentFind.getName(), "Department 1");
    }

    @Test
    void delete() {
        Department departmentRandom = getDepartmentRandomCreated();
        departmentDao.delete(departmentRandom.getId());
        assertNull(departmentDao.findById(departmentRandom.getId()));
    }
}