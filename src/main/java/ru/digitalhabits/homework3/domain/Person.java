package ru.digitalhabits.homework3.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "person")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_person")
    @SequenceGenerator(name = "sequence_person", sequenceName = "sequence_person", allocationSize = 1)
    Integer id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "middle_name")
    String middleName;

    @Column(name = "age")
    int age;

    @ManyToOne
    @JoinColumn(name="id_department")
    Department department;

    public String getFullName() {
        return firstName +
                (middleName!=null ? " "+middleName : "") +
                (lastName!=null ? " "+lastName : "");
    }

}
