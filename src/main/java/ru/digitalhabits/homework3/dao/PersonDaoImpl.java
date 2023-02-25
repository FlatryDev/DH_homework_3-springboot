package ru.digitalhabits.homework3.dao;

import org.springframework.stereotype.Repository;
import ru.digitalhabits.homework3.domain.Person;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("PersonDao")
public class PersonDaoImpl
        implements PersonDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Nullable
    @Override
    public Person findById(@Nonnull Integer id) {
        return entityManager.find(Person.class, id);
    }

    @Nonnull
    @Override
    public List<Person> findAll() {
        return entityManager.createQuery("select p from Person p", Person.class).getResultList();
    }

    @Nonnull
    @Override
    public Person update(@Nonnull Person person) {
        if (person.getId() == null) {
            entityManager.persist(person);
            return person;
        } else {
            return entityManager.merge(person);
        }
    }

    @Nullable
    @Override
    public Person delete(@Nonnull Integer id) {
        Person person = findById(id);
        if (person != null)
            entityManager.remove(person);
        return null;
    }
}
