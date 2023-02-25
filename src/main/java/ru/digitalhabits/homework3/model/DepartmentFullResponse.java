package ru.digitalhabits.homework3.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.digitalhabits.homework3.domain.Department;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class DepartmentFullResponse {
    private Integer id;
    private String name;
    private boolean closed;
    private List<PersonShortResponse> persons;

    public DepartmentFullResponse getFromDepartment(Department department) {
        if (department == null) return this;
        return this.setId(department.getId())
                .setName(department.getName())
                .setClosed(department.isClosed())
                .setPersons(
                        Optional.ofNullable(department.getPersons())
                                .orElse(new ArrayList<>())
                                .stream()
                                .map(person -> new PersonShortResponse().getFromPerson(person)).collect(Collectors.toList())
                );
    }

}
