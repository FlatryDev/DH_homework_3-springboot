package ru.digitalhabits.homework3.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.digitalhabits.homework3.domain.Person;

@Data
@Accessors(chain = true)
public class PersonFullResponse {
    private Integer id;
    private String fullName;
    private Integer age;
    private DepartmentShortResponse department;

    public PersonFullResponse getFromPerson(Person person) {
        if (person == null) return this;
        return this.setId(person.getId())
                .setFullName(person.getFullName())
                .setAge(person.getAge())
                .setDepartment(
                        new DepartmentShortResponse().getFromDepartment(person.getDepartment())
                )
                ;
    }
}
