package ru.digitalhabits.homework3.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.digitalhabits.homework3.domain.Department;

@Data
@Accessors(chain = true)
public class DepartmentShortResponse {
    private Integer id;
    private String name;

    public DepartmentShortResponse getFromDepartment(Department department) {
        if (department == null) return null;
        return this
                .setId(department.getId())
                .setName(department.getName());
    }
}
