package ru.digitalhabits.homework3.service;

import ru.digitalhabits.homework3.domain.Department;
import ru.digitalhabits.homework3.model.DepartmentFullResponse;
import ru.digitalhabits.homework3.model.DepartmentRequest;
import ru.digitalhabits.homework3.model.DepartmentShortResponse;

import javax.annotation.Nonnull;
import java.util.List;

public interface DepartmentService {

    @Nonnull
    List<DepartmentShortResponse> findAll();

    @Nonnull
    DepartmentFullResponse getById(int id);

    int create(@Nonnull DepartmentRequest request);

    @Nonnull
    DepartmentFullResponse update(int id, @Nonnull DepartmentRequest request);

    void delete(int id);

    void close(int id);

    public Department getDepartment(int id);
}
