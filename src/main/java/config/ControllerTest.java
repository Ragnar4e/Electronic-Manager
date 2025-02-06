package config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entity.*;
import repository.*;
import service.*;
import controller.*;
import java.math.BigDecimal;

public class ControllerTest {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("home_manager");
            entityManager = entityManagerFactory.createEntityManager();

            // Initialize repositories
            CompanyRepository companyRepo = new CompanyRepositoryImpl(entityManager);
            EmployeeRepository employeeRepo = new EmployeeRepositoryImpl(entityManager);
            BuildingRepository buildingRepo = new BuildingRepositoryImpl(entityManager);

            // Initialize services
            CompanyService companyService = new CompanyServiceImpl(entityManager, companyRepo, employeeRepo, buildingRepo);
            EmployeeService employeeService = new EmployeeServiceImpl(entityManager, employeeRepo, buildingRepo);
            BuildingService buildingService = new BuildingServiceImpl(entityManager, buildingRepo, employeeRepo, null);

            // Initialize controllers
            CompanyController companyController = new CompanyController(companyService);
            EmployeeController employeeController = new EmployeeController(employeeService);
            BuildingController buildingController = new BuildingController(buildingService);

            // Start transaction
            entityManager.getTransaction().begin();

            try {
                // Test creating a company
                Company company = companyController.createCompany(
                        "Test Company",
                        "test@company.com",
                        "1234567890",
                        "Test Address"
                );
                System.out.println("Created company with ID: " + company.getId());

                // Test creating an employee
                Employee employee = new Employee();
                employee.setFirstName("John");
                employee.setLastName("Doe");
                employee.setEmail("john.doe@test.com");
                employee.setPhone("0987654321");
                employee.setCompany(company);
                employee = employeeService.save(employee);
                System.out.println("Created employee with ID: " + employee.getId());

                // Test creating a building
                Building building = new Building();
                building.setAddress("123 Test Street");
                building.setTotalFloors(5);
                building.setTotalApartments(10);
                building.setBuiltUpArea(new BigDecimal("1000.00"));
                building.setCommonArea(new BigDecimal("200.00"));
                building.setEmployee(employee);
                building = buildingService.save(building);
                System.out.println("Created building with ID: " + building.getId());

                // Test some queries
                System.out.println("\nTesting queries:");
                System.out.println("Number of employees in company: " +
                        employeeController.getEmployeesByCompany(company.getId()).size());
                System.out.println("Buildings managed by employee: " +
                        buildingController.getBuildingsByEmployee(employee.getId()).size());
                System.out.println("Building occupancy rate: " +
                        buildingController.getOccupancyRate(building.getId()) + "%");

                entityManager.getTransaction().commit();
                System.out.println("\nController tests completed successfully!");

            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw e;
            }

        } catch (Exception e) {
            System.out.println("Test failed!");
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
        }
    }
}