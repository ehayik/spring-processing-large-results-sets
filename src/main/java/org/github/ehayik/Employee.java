package org.github.ehayik;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;

@ToString
@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @Column(name = "emp_no")
    private int employeeNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public enum Gender {
        M, F
    }
}
