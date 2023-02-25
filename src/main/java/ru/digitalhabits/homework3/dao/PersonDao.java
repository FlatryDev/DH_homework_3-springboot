package ru.digitalhabits.homework3.dao;

import ru.digitalhabits.homework3.domain.Person;

//@NoRepositoryBean
public interface PersonDao
        extends CrudOperations<Person, Integer> {}