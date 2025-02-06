package config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entity.*;
import repository.*;
import service.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ServiceTest {
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
            OwnerRepository ownerRepo = new OwnerRepositoryImpl(entityManager);
            ResidentRepository residentRepo = new ResidentRepositoryImpl(entityManager);
            FeeRepository feeRepo = new FeeRepositoryImpl(entityManager);

            // Initialize services
            CompanyService companyService = new CompanyServiceImpl(entityManager, companyRepo, employeeRepo, buildingRepo);
            EmployeeService employeeService = new EmployeeServiceImpl(entityManager, employeeRepo, buildingRepo);
            BuildingService buildingService = new BuildingServiceImpl(entityManager, buildingRepo, employeeRepo, apartmentRepo);
            ApartmentService apartmentService = new ApartmentServiceImpl(entityManager, apartmentRepo, ownerRepo, residentRepo, feeRepo);
            OwnerService ownerService = new OwnerServiceImpl(entityManager, ownerRepo, apartmentRepo);
            ResidentService residentService = new ResidentServiceImpl(entityManager, residentRepo, apartmentRepo);
            FeeService feeService = new FeeServiceImpl(entityManager, feeRepo, apartmentRepo, buildingRepo);

            System.out.println("Testing Services...");

            // Test Company Service
            Company company = new Company();
            company.setName("Service Test Company");
            company.setEmail("service.test@company.com");
            company = companyService.save(company);
            System.out.println("Company created with ID: " + company.getId());

            // Test Employee Service
            Employee employee = new Employee();
            employee.setFirstName("John");
            employee.setLastName("Service");
            employee.setCompany(company);
            employee = employeeService.save(employee);
            System.out.println("Employee created with ID: " + employee.getId());

            // Test Building Service
            Building building = new Building();
            building.setAddress("123 Service St");
            building.setTotalFloors(5);
            building.setTotalApartments(10);
            building.setBuiltUpArea(new BigDecimal("1000.00"));
            building.setEmployee(employee);
            building = buildingService.save(building);
            System.out.println("Building created with ID: " + building.getId());

            // Test Apartment Service
            Apartment apartment = new Apartment();
            apartment.setApartmentNumber("101");
            apartment.setFloorNumber(1);
            apartment.setArea(new BigDecimal("75.50"));
            apartment.setBuilding(building);
            apartment = apartmentService.save(apartment);
            System.out.println("Apartment created with ID: " + apartment.getId());

            // Test Owner Service
            Owner owner = new Owner();
            owner.setFirstName("Alice");
            owner.setLastName("Service");
            owner.setEmail("alice.service@test.com");
            owner.getOwnedApartments().add(apartment);
            owner = ownerService.save(owner);
            System.out.println("Owner created with ID: " + owner.getId());

            // Test Resident Service
            Resident resident = new Resident();
            resident.setFirstName("Bob");
            resident.setLastName("Service");
            resident.setAge(25);
            resident.setHasPet(true);
            resident.setUsesElevator(true);
            resident.setApartment(apartment);
            resident = residentService.save(resident);
            System.out.println("Resident created with ID: " + resident.getId());

            // Test Fee Service
            Fee fee = new Fee();
            fee.setAmount(new BigDecimal("150.00"));
            fee.setDueDate(LocalDate.now().plusMonths(1));
            fee.setStatus(Fee.FeeStatus.PENDING);
            fee.setApartment(apartment);
            fee = feeService.save(fee);
            System.out.println("Fee created with ID: " + fee.getId());

            // Test some service methods
            System.out.println("\nTesting additional service methods:");
            System.out.println("Building occupancy rate: " + buildingService.getOccupancyRate(building.getId()) + "%");
            System.out.println("Total residents in building: " + buildingService.getTotalResidents(building.getId()));
            System.out.println("Monthly fee for apartment: " + apartmentService.calculateMonthlyFee(apartment.getId()));
            System.out.println("Resident has pet fee: " + residentService.hasPetFee(resident.getId()));

            System.out.println("\nService tests completed successfully!");

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