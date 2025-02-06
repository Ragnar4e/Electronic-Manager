package config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entity.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        try {
            EntityManagerFactory entityManagerFactory =
                    Persistence.createEntityManagerFactory("home_manager");
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            // Start transaction
            entityManager.getTransaction().begin();

            try {
                // Create a test company
                Company company = new Company();
                company.setName("Test Company 3");
                company.setEmail("test3@company.com");
                company.setContactNumber("1234567890");
                company.setAddress("Test Address 3");

                // Save the company
                entityManager.persist(company);

                // Create a test employee
                Employee employee = new Employee();
                employee.setFirstName("Jane");
                employee.setLastName("Smith");
                employee.setEmail("jane.smith@test.com");
                employee.setPhone("0987654321");
                employee.setCompany(company);
                company.getEmployees().add(employee);

                // Save the employee
                entityManager.persist(employee);

                // Create a test building
                Building building = new Building();
                building.setAddress("123 Test Street");
                building.setTotalFloors(5);
                building.setTotalApartments(20);
                building.setBuiltUpArea(new BigDecimal("1000.00"));
                building.setCommonArea(new BigDecimal("200.00"));
                building.setEmployee(employee);
                employee.getManagedBuildings().add(building);

                // Save the building
                entityManager.persist(building);

                // Create a test apartment
                Apartment apartment = new Apartment();
                apartment.setApartmentNumber("101");
                apartment.setFloorNumber(1);
                apartment.setArea(new BigDecimal("75.50"));
                apartment.setBuilding(building);
                building.getApartments().add(apartment);

                // Save the apartment
                entityManager.persist(apartment);

                // Create a test owner
                Owner owner = new Owner();
                owner.setFirstName("Alice");
                owner.setLastName("Johnson");
                owner.setEmail("alice.johnson@test.com");
                owner.setPhone("1122334455");
                owner.getOwnedApartments().add(apartment);

                // Save the owner
                entityManager.persist(owner);

                // Create a test resident
                Resident resident = new Resident();
                resident.setFirstName("Bob");
                resident.setLastName("Smith");
                resident.setAge(25);
                resident.setHasPet(true);
                resident.setUsesElevator(true);
                resident.setApartment(apartment);
                apartment.getResidents().add(resident);

                // Save the resident
                entityManager.persist(resident);

                // Create a test fee
                Fee fee = new Fee();
                fee.setAmount(new BigDecimal("150.00"));
                fee.setDueDate(LocalDate.now().plusMonths(1));
                fee.setStatus(Fee.FeeStatus.PENDING);
                fee.setApartment(apartment);
                apartment.getFees().add(fee);

                // Save the fee
                entityManager.persist(fee);

                // Commit transaction
                entityManager.getTransaction().commit();

                // Print created entities IDs
                System.out.println("Company created with ID: " + company.getId());
                System.out.println("Employee created with ID: " + employee.getId());
                System.out.println("Building created with ID: " + building.getId());
                System.out.println("Apartment created with ID: " + apartment.getId());
                System.out.println("Owner created with ID: " + owner.getId());
                System.out.println("Resident created with ID: " + resident.getId());
                System.out.println("Fee created with ID: " + fee.getId());

            } catch (Exception e) {
                // Rollback transaction in case of error
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw e;
            }

            // Close resources
            entityManager.close();
            entityManagerFactory.close();

            System.out.println("Database connection and operations successful!");
        } catch (Exception e) {
            System.out.println("Operation failed!");
            e.printStackTrace();
        }
    }
}