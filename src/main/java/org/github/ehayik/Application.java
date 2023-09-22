package org.github.ehayik;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(EmployeeService employeeService) {
        return x -> {
            employeeService.logAllEmployeeFullNamesUsingHibernate();
            employeeService.logAllEmployeeFullNamesUsingSpringData();
        };
    }
}
