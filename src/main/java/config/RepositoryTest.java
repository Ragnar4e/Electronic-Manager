package config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entity.*;
import repository.*;

import java.math.BigDecimal;

public class RepositoryTest {
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
            ApartmentRepository apartmentRepo = new ApartmentRepositoryImpl(entityManager);
            ResidentRepository residentRepo = new ResidentRepositoryImpl(entityManager);
            OwnerRepository ownerRepo = new OwnerRepositoryImpl(entityManager);

            // Start transaction
            entityManager.getTransaction().begin();

            // Test Company Repository
            Company company = new Company();
            company.setName("Test Company");
            company.setEmail("test@company.com");
            company.setContactNumber("1234567890");
            companyRepo.save(company);
            System.out.println("Company saved with ID: " + company.getId());

            // Test Employee Repository
            Employee employee = new Employee();
            employee.setFirstName("John");
            employee.setLastName("Doe");
            employee.setCompany(company);
            employeeRepo.save(employee);
            System.out.println("Employee saved with ID: " + employee.getId());

            // Test Building Repository
            Building building = new Building();
            building.setAddress("123 Test Street");
            building.setTotalFloors(5);
            building.setTotalApartments(10);
            building.setBuiltUpArea(new BigDecimal("1000.00"));
            building.setCommonArea(new BigDecimal("200.00"));
            building.setEmployee(employee);
            buildingRepo.save(building);
            System.out.println("Building saved with ID: " + building.getId());

            // Test Apartment Repository
            Apartment apartment1 = new Apartment();
            apartment1.setApartmentNumber("101");
            apartment1.setFloorNumber(1);
            apartment1.setArea(new BigDecimal("75.50"));
            apartment1.setBuilding(building);
            apartmentRepo.save(apartment1);
            System.out.println("Apartment 1 saved with ID: " + apartment1.getId());

            // Test Owner Repository
            Owner owner = new Owner();
            owner.setFirstName("Alice");
            owner.setLastName("Johnson");
            owner.setEmail("alice@example.com");
            owner.getOwnedApartments().add(apartment1);
            ownerRepo.save(owner);
            System.out.println("Owner saved with ID: " + owner.getId());

            // Test Resident Repository
            Resident resident = new Resident();
            resident.setFirstName("Bob");
            resident.setLastName("Smith");
            resident.setAge(30);
            resident.setHasPet(true);
            resident.setUsesElevator(true);
            resident.setApartment(apartment1);
            residentRepo.save(resident);
            System.out.println("Resident saved with ID: " + resident.getId());

            // Test some queries
            System.out.println("\nTesting queries:");
            System.out.println("Buildings managed by employee: " +
                    buildingRepo.findByEmployeeId(employee.getId()).size());
            System.out.println("Apartments on floor 1: " +
                    apartmentRepo.findByFloorNumber(1).size());
            System.out.println("Residents with pets: " +
                    residentRepo.findResidentsWithPets().size());

            // Commit transaction
            entityManager.getTransaction().commit();
            System.out.println("\nRepository tests completed successfully!");

        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
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