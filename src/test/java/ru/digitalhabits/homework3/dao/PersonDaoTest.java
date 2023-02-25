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
import org.springframework.context.annotation.Import;
import ru.digitalhabits.homework3.domain.Person;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ComponentScan(basePackages = "ru.digitalhabits.homework3")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PersonDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonDao personDao;

    private Person getRandomPerson() {
        return new Person()
                .setFirstName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                .setMiddleName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                .setLastName(RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(5, 15)))
                .setAge(RandomUtils.nextInt(18, 65))
                ;
    }
    private Person getPersonCreated(Person person) {
        return personDao.update(person);
    }
    private Person getPersonRandomCreated() {
        return getPersonCreated(getRandomPerson());
    }

    @Test
    void findById() {
        Person personRandom = getPersonRandomCreated();
        Person personFind = personDao.findById(personRandom.getId());
        assertEquals(personFind, personRandom);
    }

    @Test
    void findAll() {
        List<Person> personList = new ArrayList<>();
        for(int i=0;i < RandomUtils.nextInt(5,10);i++)
            personList.add(getPersonRandomCreated());
        List<Person> personFind = personDao.findAll();
        assertTrue(personFind.containsAll(personList));
        assertTrue(personList.containsAll(personFind));
    }

    @Test
    void update() {
        Person personRandom = getPersonRandomCreated();
        personRandom
                .setFirstName("FirstName 1")
                .setMiddleName("MiddleName 2")
                .setLastName("LastName 3")
                .setAge(55);
        personDao.update(personRandom);
        Person personFind = personDao.findById(personRandom.getId());
        assertEquals(personFind.getFullName() + " " + personFind.getAge(),
                "FirstName 1 MiddleName 2 LastName 3 55"
        );
    }

    @Test
    void delete() {
        Person personRandom = getPersonRandomCreated();
        personDao.delete(personRandom.getId());
        assertNull(personDao.findById(personRandom.getId()));
    }
}