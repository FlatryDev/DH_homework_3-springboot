package ru.digitalhabits.homework3.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.digitalhabits.homework3.domain.Person;

@Data
@Accessors(chain = true)
public class PersonShortResponse {
    private Integer id;
    private String fullName;

    public PersonShortResponse getFromPerson(Person person) {
        if (person == null) return null;
        return this
                .setId(person.getId())
                .setFullName(person.getFullName())
                ;
    }

}
