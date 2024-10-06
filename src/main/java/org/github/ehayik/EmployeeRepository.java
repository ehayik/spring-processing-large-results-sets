package org.github.ehayik;

import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("FROM Employee ep ORDER BY ep.employeeNumber")
    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE))
    Stream<Employee> scroll();

    Window<Employee> findAllByBirthDateAfter(LocalDate birthDate, Pageable pageable, ScrollPosition scrollPosition);
}
