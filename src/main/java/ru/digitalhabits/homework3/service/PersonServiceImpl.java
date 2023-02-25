package ru.digitalhabits.homework3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.digitalhabits.homework3.dao.DepartmentDao;
import ru.digitalhabits.homework3.dao.PersonDao;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.domain.Person;
import ru.digitalhabits.homework3.model.PersonFullResponse;
import ru.digitalhabits.homework3.model.PersonRequest;
import ru.digitalhabits.homework3.model.PersonShortResponse;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl
        implements PersonService {

    private final PersonDao personDao;
    private final DepartmentDao departmentDao;
    private final DepartmentService departmentService;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<PersonShortResponse> findAll() {
        return personDao.findAll().stream()
                .map(person -> new PersonShortResponse().getFromPerson(person))
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public PersonFullResponse getById(int id) {
        return new PersonFullResponse().getFromPerson(getPerson(id));
    }

    @Override
    @Transactional
    public int create(@Nonnull PersonRequest request) {
        return personDao
                .update(request.getPerson())
                .getId();
    }

    @Nonnull
    @Override
    @Transactional
    public PersonFullResponse update(int id, @Nonnull PersonRequest request) {
        return new PersonFullResponse().getFromPerson(
                        personDao.update(
                                request.getAsPerson(getPerson(id))
                        )
                    );
    }

    @Override
    @Transactional
    public void delete(int id) {
        personDao.delete(id);
    }


    @Override
    @Transactional
    public void addPersonToDepartment(int departmentId, int personId) {
        Department department = departmentService.getDepartment(departmentId);
        if (department.isClosed()) throw new IllegalStateException("Department with id="+departmentId+" is closed");
        Person person = getPerson(personId);
        person.setDepartment(department);
        personDao.update(person);

        List<Person> personList = Optional
                                        .ofNullable(department.getPersons())
                                        .orElse(new ArrayList<>());
        personList.add(person);
        department.setPersons(personList);
        departmentDao.update(department);
    }

    @Override
    @Transactional
    public void removePersonFromDepartment(int departmentId, int personId) {
        Department department = departmentService.getDepartment(departmentId);
        Person person = personDao.findById(personId);
        if (person != null && department.getPersons().contains(person)) {
            person.setDepartment(null);
            personDao.update(person);
            department.getPersons().remove(person);
            departmentDao.update(department);
        }
    }

    @Override
    @Transactional
    public Person getPerson(int id) {
        return Optional
                .ofNullable(personDao.findById(id))
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id = " + id));
    }
}
