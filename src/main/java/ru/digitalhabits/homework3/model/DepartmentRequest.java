package ru.digitalhabits.homework3.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.digitalhabits.homework3.domain.Department;

import javax.validation.constraints.NotEmpty;

@Data
@Accessors(chain = true)
public class DepartmentRequest {
    @NotEmpty(message = "{field.is.empty}")
    private String name;

    public Department getDepartment() {
        return new Department().setName(name);
    }

    public Department getAsDepartment(Department department) {
        return department.setName(name);
    }

}
