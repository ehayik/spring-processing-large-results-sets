package org.github.ehayik;

import com.google.common.base.Stopwatch;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hibernate.LockMode.NONE;
import static org.hibernate.ScrollMode.FORWARD_ONLY;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    @Value("${spring.jpa.properties.hibernate.jdbc.fetch_size}")
    private final int fetchSize;

    private final EntityManager entityManager;
    private final EmployeeRepository employeeRepository;

    public void logAllEmployeeFullNamesUsingHibernate() {
        var stopwatch = Stopwatch.createStarted();
        var statelessSession =
                ((Session) entityManager.getDelegate()).getSessionFactory().openStatelessSession();

        try (statelessSession) {
            var query = statelessSession.createQuery("FROM Employee e", Employee.class);
            query.setFetchSize(fetchSize);
            query.setReadOnly(true);
            query.setLockMode("e", NONE);

            try (var employees = query.scroll(FORWARD_ONLY)) {
                while (employees.next()) {
                    log.debug("Employee's name is: {}", employees.get().getFullName());
                }
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
}
