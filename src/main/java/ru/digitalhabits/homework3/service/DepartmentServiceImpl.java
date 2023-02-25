package ru.digitalhabits.homework3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.digitalhabits.homework3.dao.DepartmentDao;
import ru.digitalhabits.homework3.dao.PersonDao;
import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.model.DepartmentFullResponse;
import ru.digitalhabits.homework3.model.DepartmentRequest;
import ru.digitalhabits.homework3.model.DepartmentShortResponse;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl
        implements DepartmentService {

    private final DepartmentDao departmentDao;
    private final PersonDao personDao;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentShortResponse> findAll() {
        return departmentDao.findAll().stream()
                .map(department -> new DepartmentShortResponse().getFromDepartment(department))
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public DepartmentFullResponse getById(int id) {
        return new DepartmentFullResponse().getFromDepartment(getDepartment(id));
    }

    @Override
    @Transactional
    public int create(@Nonnull DepartmentRequest request) {
        return departmentDao.update(request.getDepartment()).getId();
    }

    @Nonnull
    @Override
    @Transactional
    public DepartmentFullResponse update(int id, @Nonnull DepartmentRequest request) {
        return new DepartmentFullResponse()
                .getFromDepartment(
                        request.getAsDepartment(getDepartment(id))
                );
    }

    @Override
    @Transactional
    public void delete(int id) {
        departmentDao.delete(id);
    }

    @Override
    @Transactional
    public void close(int id) {
        Department department = getDepartment(id);
        department.setClosed(true)
                .getPersons().stream()
                .map(person -> person.setDepartment(null))
                .forEach(personDao::update);

        department.setPersons(new ArrayList<>());
        departmentDao.update(department);
    }

    @Override
    @Transactional
    public Department getDepartment(int id) {
        return Optional
                .ofNullable(departmentDao.findById(id))
                .orElseThrow(() -> new EntityNotFoundException("Cannot find department by id = " + id));
    }

}
