package org.github.ehayik;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("""
            FROM Employee ep
            """)
    Stream<Employee> scrollAll();
}
