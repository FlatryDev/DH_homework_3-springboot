package ru.digitalhabits.homework3.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "department")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Department {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_department")
    @SequenceGenerator(name="sequence_department", sequenceName = "sequence_department", allocationSize = 1)
    Integer id;

    @Column(name = "name")
    String name;

    @Column(name = "closed")
    boolean closed;

    @OneToMany(mappedBy = "department")
    List<Person> persons;

}