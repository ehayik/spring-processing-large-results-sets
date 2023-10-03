package org.github.ehayik;

import com.google.common.base.Stopwatch;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.support.WindowIterator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hibernate.LockMode.NONE;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EntityManager entityManager;
    private final EmployeeRepository employeeRepository;

    public void logAllEmployeeFullNamesUsingHibernate() {
        var stopwatch = Stopwatch.createStarted();
        var statelessSession =
                ((Session) entityManager.getDelegate()).getSessionFactory().openStatelessSession();

        try (statelessSession) {
            var query = statelessSession
                    .createQuery("FROM Employee e ORDER BY e.employeeNumber", Employee.class)
                    .setReadOnly(true)
                    .setFetchSize(Integer.MIN_VALUE)
                    .setLockMode("e", NONE);

            try (var employeesStream = query.stream()) {
                employeesStream.forEach(employee -> log.debug("Employee's name is: {}", employee.getFullName()));
            }
        }

        log.info("Hibernate: All employees were printed after {} milliseconds.", stopwatch.elapsed(MILLISECONDS));
    }

    @Transactional(readOnly = true)
    public void logAllEmployeeFullNamesUsingSpringData() {
        var stopwatch = Stopwatch.createStarted();

        try (var employees = employeeRepository.scroll()) {
            employees.forEach(employee -> log.debug("Employee's name is: {}", employee.getFullName()));
        }

        log.info("Spring Data: All employees were printed after {} milliseconds.", stopwatch.elapsed(MILLISECONDS));
    }

    @Transactional(readOnly = true)
    public void logAllEmployeeUsingSpringDataScrollAPI() {
        var stopwatch = Stopwatch.createStarted();
        var employees = WindowIterator.of(employeeRepository::findFirst100000ByOrderByEmployeeNumber)
                .startingAt(ScrollPosition.keyset());
        employees.forEachRemaining(employee -> log.debug("Employee's name is: {}", employee.getFullName()));
        log.info(
                "Spring Data Scroll API: All employees were printed after {} milliseconds.",
                stopwatch.elapsed(MILLISECONDS));
    }
}
