package ui;

import controller.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entity.*;
import repository.*;
import service.*;
import util.FeeExporter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final CompanyController companyController;
    private final EmployeeController employeeController;
    private final BuildingController buildingController;
    private final ApartmentController apartmentController;
    private final OwnerController ownerController;
    private final ResidentController residentController;
    private final FeeController feeController;
    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    public ConsoleUI() {
        scanner = new Scanner(System.in);
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

        // Initialize controllers
        companyController = new CompanyController(companyService);
        employeeController = new EmployeeController(employeeService);
        buildingController = new BuildingController(buildingService);
        apartmentController = new ApartmentController(apartmentService);
        ownerController = new OwnerController(ownerService);
        residentController = new ResidentController(residentService);
        feeController = new FeeController(feeService);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> handleCompanyMenu();
                case 2 -> handleEmployeeMenu();
                case 3 -> handleBuildingMenu();
                case 4 -> handleApartmentMenu();
                case 5 -> handleOwnerMenu();
                case 6 -> handleResidentMenu();
                case 7 -> handleFeeMenu();
                case 0 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        // Close resources
        entityManager.close();
        entityManagerFactory.close();
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println("\n=== Electronic Home Manager ===");
        System.out.println("1. Company Management");
        System.out.println("2. Employee Management");
        System.out.println("3. Building Management");
        System.out.println("4. Apartment Management");
        System.out.println("5. Owner Management");
        System.out.println("6. Resident Management");
        System.out.println("7. Fee Management");
        System.out.println("0. Exit");
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private BigDecimal getBigDecimalInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void handleCompanyMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Company Management ===");
            System.out.println("1. Create new company");
            System.out.println("2. View all companies");
            System.out.println("3. View company details");
            System.out.println("4. Update company");
            System.out.println("5. Delete company");
            System.out.println("6. View companies sorted by revenue");
            System.out.println("0. Back to main menu");

            int choice = getIntInput("Enter your choice: ");
            entityManager.getTransaction().begin();
            try {
                switch (choice) {
                    case 1 -> createCompany();
                    case 2 -> viewAllCompanies();
                    case 3 -> viewCompanyDetails();
                    case 4 -> updateCompany();
                    case 5 -> deleteCompany();
                    case 6 -> viewCompaniesByRevenue();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createCompany() {
        String name = getStringInput("Enter company name: ");
        String email = getStringInput("Enter company email: ");
        String contactNumber = getStringInput("Enter contact number: ");
        String address = getStringInput("Enter address: ");

        Company company = companyController.createCompany(name, email, contactNumber, address);
        System.out.println("Company created successfully with ID: " + company.getId());
    }

    private void viewAllCompanies() {
        List<Company> companies = companyController.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("No companies found.");
            return;
        }

        System.out.println("\nCompany List:");
        for (Company company : companies) {
            System.out.printf("ID: %d, Name: %s, Email: %s, Contact: %s%n",
                    company.getId(), company.getName(), company.getEmail(), company.getContactNumber());
        }
    }

    private void viewCompanyDetails() {
        long companyId = getIntInput("Enter company ID: ");
        try {
            Company company = companyController.getCompanyById(companyId);
            System.out.println("\nCompany Details:");
            System.out.println("ID: " + company.getId());
            System.out.println("Name: " + company.getName());
            System.out.println("Email: " + company.getEmail());
            System.out.println("Contact: " + company.getContactNumber());
            System.out.println("Address: " + company.getAddress());
        } catch (IllegalArgumentException e) {
            System.out.println("Company not found.");
        }
    }

    private void updateCompany() {
        long companyId = getIntInput("Enter company ID to update: ");
        try {
            Company company = companyController.getCompanyById(companyId);

            String name = getStringInput("Enter new company name (press Enter to keep current: " + company.getName() + "): ");
            String email = getStringInput("Enter new email (press Enter to keep current: " + company.getEmail() + "): ");
            String contactNumber = getStringInput("Enter new contact number (press Enter to keep current: " + company.getContactNumber() + "): ");
            String address = getStringInput("Enter new address (press Enter to keep current: " + company.getAddress() + "): ");

            name = name.isEmpty() ? company.getName() : name;
            email = email.isEmpty() ? company.getEmail() : email;
            contactNumber = contactNumber.isEmpty() ? company.getContactNumber() : contactNumber;
            address = address.isEmpty() ? company.getAddress() : address;

            companyController.updateCompany(companyId, name, email, contactNumber, address);
            System.out.println("Company updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Company not found.");
        }
    }

    private void deleteCompany() {
        long companyId = getIntInput("Enter company ID to delete: ");
        try {
            companyController.deleteCompany(companyId);
            System.out.println("Company deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting company: " + e.getMessage());
        }
    }

    private void viewCompaniesByRevenue() {
        List<Company> companies = companyController.getCompaniesSortedByRevenue();
        if (companies.isEmpty()) {
            System.out.println("No companies found.");
            return;
        }

        System.out.println("\nCompanies Sorted by Revenue:");
        for (Company company : companies) {
            System.out.printf("ID: %d, Name: %s%n",
                    company.getId(), company.getName());
        }
    }

    private void handleEmployeeMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Employee Management ===");
            System.out.println("1. Add new employee");
            System.out.println("2. View all employees");
            System.out.println("3. View employee details");
            System.out.println("4. Update employee");
            System.out.println("5. Delete employee");
            System.out.println("6. View employees by company");
            System.out.println("7. View employees sorted by building count");
            System.out.println("8. Find employee with least buildings");
            System.out.println("0. Back to main menu");

            int choice = getIntInput("Enter your choice: ");
            entityManager.getTransaction().begin();
            try {
                switch (choice) {
                    case 1 -> addEmployee();
                    case 2 -> viewAllEmployees();
                    case 3 -> viewEmployeeDetails();
                    case 4 -> updateEmployee();
                    case 5 -> deleteEmployee();
                    case 6 -> viewEmployeesByCompany();
                    case 7 -> viewEmployeesByBuildingCount();
                    case 8 -> findEmployeeWithLeastBuildings();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addEmployee() {
        long companyId = getIntInput("Enter company ID: ");
        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone number: ");

        try {
            Employee employee = employeeController.createEmployee(companyId, firstName, lastName, email, phone);
            System.out.println("Employee created successfully with ID: " + employee.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllEmployees() {
        List<Employee> employees = employeeController.getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println("\nEmployee List:");
        for (Employee employee : employees) {
            System.out.printf("ID: %d, Name: %s %s, Email: %s%n",
                    employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getEmail());
        }
    }

    private void viewEmployeeDetails() {
        long employeeId = getIntInput("Enter employee ID: ");
        try {
            Employee employee = employeeController.getEmployeeById(employeeId);
            System.out.println("\nEmployee Details:");
            System.out.println("ID: " + employee.getId());
            System.out.println("Name: " + employee.getFirstName() + " " + employee.getLastName());
            System.out.println("Email: " + employee.getEmail());
            System.out.println("Phone: " + employee.getPhone());
            System.out.println("Company: " + employee.getCompany().getName());
            System.out.println("Number of managed buildings: " +
                    employeeController.getManagedBuildingsCount(employeeId));
        } catch (IllegalArgumentException e) {
            System.out.println("Employee not found.");
        }
    }

    private void updateEmployee() {
        long employeeId = getIntInput("Enter employee ID to update: ");
        try {
            Employee employee = employeeController.getEmployeeById(employeeId);

            String firstName = getStringInput("Enter new first name (press Enter to keep current: " +
                    employee.getFirstName() + "): ");
            String lastName = getStringInput("Enter new last name (press Enter to keep current: " +
                    employee.getLastName() + "): ");
            String email = getStringInput("Enter new email (press Enter to keep current: " +
                    employee.getEmail() + "): ");
            String phone = getStringInput("Enter new phone (press Enter to keep current: " +
                    employee.getPhone() + "): ");

            firstName = firstName.isEmpty() ? employee.getFirstName() : firstName;
            lastName = lastName.isEmpty() ? employee.getLastName() : lastName;
            email = email.isEmpty() ? employee.getEmail() : email;
            phone = phone.isEmpty() ? employee.getPhone() : phone;

            employeeController.updateEmployee(employeeId, firstName, lastName, email, phone);
            System.out.println("Employee updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Employee not found.");
        }
    }

    private void deleteEmployee() {
        long employeeId = getIntInput("Enter employee ID to delete: ");
        try {
            // Transfer buildings before deleting
            employeeController.transferBuildings(employeeId);
            employeeController.deleteEmployee(employeeId);
            System.out.println("Employee deleted successfully and buildings reassigned.");
        } catch (Exception e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }

    private void viewEmployeesByCompany() {
        long companyId = getIntInput("Enter company ID: ");
        try {
            List<Employee> employees = employeeController.getEmployeesByCompany(companyId);
            if (employees.isEmpty()) {
                System.out.println("No employees found for this company.");
                return;
            }

            System.out.println("\nEmployees in company:");
            for (Employee employee : employees) {
                System.out.printf("ID: %d, Name: %s %s, Email: %s%n",
                        employee.getId(), employee.getFirstName(), employee.getLastName(),
                        employee.getEmail());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Company not found.");
        }
    }

    private void viewEmployeesByBuildingCount() {
        List<Employee> employees = employeeController.getEmployeesSortedByBuildingCount();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.println("\nEmployees sorted by number of managed buildings:");
        for (Employee employee : employees) {
            int buildingCount = employeeController.getManagedBuildingsCount(employee.getId());
            System.out.printf("ID: %d, Name: %s %s, Buildings managed: %d%n",
                    employee.getId(), employee.getFirstName(), employee.getLastName(), buildingCount);
        }
    }

    private void findEmployeeWithLeastBuildings() {
        Employee employee = employeeController.getEmployeeWithLeastBuildings();
        if (employee == null) {
            System.out.println("No employees found.");
            return;
        }

        int buildingCount = employeeController.getManagedBuildingsCount(employee.getId());
        System.out.printf("\nEmployee with least buildings:%n" +
                        "ID: %d, Name: %s %s, Buildings managed: %d%n",
                employee.getId(), employee.getFirstName(), employee.getLastName(), buildingCount);
    }
    private void handleBuildingMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Building Management ===");
            System.out.println("1. Add new building");
            System.out.println("2. View all buildings");
            System.out.println("3. View building details");
            System.out.println("4. Update building");
            System.out.println("5. Delete building");
            System.out.println("6. View buildings by employee");
            System.out.println("7. View buildings with free apartments");
            System.out.println("8. View buildings sorted by apartment count");
            System.out.println("9. View building occupancy rate");
            System.out.println("0. Back to main menu");

            int choice = getIntInput("Enter your choice: ");
            entityManager.getTransaction().begin();
            try {
                switch (choice) {
                    case 1 -> addBuilding();
                    case 2 -> viewAllBuildings();
                    case 3 -> viewBuildingDetails();
                    case 4 -> updateBuilding();
                    case 5 -> deleteBuilding();
                    case 6 -> viewBuildingsByEmployee();
                    case 7 -> viewBuildingsWithFreeApartments();
                    case 8 -> viewBuildingsByApartmentCount();
                    case 9 -> viewBuildingOccupancyRate();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addBuilding() {
        String address = getStringInput("Enter building address: ");
        int totalFloors = getIntInput("Enter total number of floors: ");
        int totalApartments = getIntInput("Enter total number of apartments: ");
        BigDecimal builtUpArea = getBigDecimalInput("Enter built-up area (in square meters): ");
        BigDecimal commonArea = getBigDecimalInput("Enter common area (in square meters): ");
        long employeeId = getIntInput("Enter employee ID who will manage this building: ");

        try {
            Building building = buildingController.createBuilding(
                    address, totalFloors, totalApartments, builtUpArea, commonArea, employeeId);
            System.out.println("Building created successfully with ID: " + building.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllBuildings() {
        List<Building> buildings = buildingController.getAllBuildings();
        if (buildings.isEmpty()) {
            System.out.println("No buildings found.");
            return;
        }

        System.out.println("\nBuilding List:");
        for (Building building : buildings) {
            System.out.printf("ID: %d, Address: %s, Floors: %d, Total Apartments: %d%n",
                    building.getId(), building.getAddress(),
                    building.getTotalFloors(), building.getTotalApartments());
        }
    }

    private void viewBuildingDetails() {
        long buildingId = getIntInput("Enter building ID: ");
        try {
            Building building = buildingController.getBuildingById(buildingId);
            System.out.println("\nBuilding Details:");
            System.out.println("ID: " + building.getId());
            System.out.println("Address: " + building.getAddress());
            System.out.println("Total Floors: " + building.getTotalFloors());
            System.out.println("Total Apartments: " + building.getTotalApartments());
            System.out.println("Built-up Area: " + building.getBuiltUpArea() + " sq.m");
            System.out.println("Common Area: " + building.getCommonArea() + " sq.m");
            System.out.println("Managed by: " + building.getEmployee().getFirstName() +
                    " " + building.getEmployee().getLastName());
            System.out.println("Occupancy Rate: " +
                    buildingController.getOccupancyRate(buildingId) + "%");
            System.out.println("Total Residents: " +
                    buildingController.getTotalResidents(buildingId));
        } catch (IllegalArgumentException e) {
            System.out.println("Building not found.");
        }
    }

    private void updateBuilding() {
        long buildingId = getIntInput("Enter building ID to update: ");
        try {
            Building building = buildingController.getBuildingById(buildingId);

            String address = getStringInput("Enter new address (press Enter to keep current: " +
                    building.getAddress() + "): ");
            String totalFloorsStr = getStringInput("Enter new total floors (press Enter to keep current: " +
                    building.getTotalFloors() + "): ");
            String totalApartmentsStr = getStringInput("Enter new total apartments (press Enter to keep current: " +
                    building.getTotalApartments() + "): ");
            String builtUpAreaStr = getStringInput("Enter new built-up area (press Enter to keep current: " +
                    building.getBuiltUpArea() + "): ");
            String commonAreaStr = getStringInput("Enter new common area (press Enter to keep current: " +
                    building.getCommonArea() + "): ");

            int totalFloors = totalFloorsStr.isEmpty() ?
                    building.getTotalFloors() : Integer.parseInt(totalFloorsStr);
            int totalApartments = totalApartmentsStr.isEmpty() ?
                    building.getTotalApartments() : Integer.parseInt(totalApartmentsStr);
            BigDecimal builtUpArea = builtUpAreaStr.isEmpty() ?
                    building.getBuiltUpArea() : new BigDecimal(builtUpAreaStr);
            BigDecimal commonArea = commonAreaStr.isEmpty() ?
                    building.getCommonArea() : new BigDecimal(commonAreaStr);
            address = address.isEmpty() ? building.getAddress() : address;

            buildingController.updateBuilding(buildingId, address, totalFloors,
                    totalApartments, builtUpArea, commonArea);
            System.out.println("Building updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Building not found or invalid input.");
        }
    }

    private void deleteBuilding() {
        long buildingId = getIntInput("Enter building ID to delete: ");
        try {
            buildingController.deleteBuilding(buildingId);
            System.out.println("Building deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting building: " + e.getMessage());
        }
    }

    private void viewBuildingsByEmployee() {
        long employeeId = getIntInput("Enter employee ID: ");
        try {
            List<Building> buildings = buildingController.getBuildingsByEmployee(employeeId);
            if (buildings.isEmpty()) {
                System.out.println("No buildings found for this employee.");
                return;
            }

            System.out.println("\nBuildings managed by employee:");
            for (Building building : buildings) {
                System.out.printf("ID: %d, Address: %s, Total Apartments: %d%n",
                        building.getId(), building.getAddress(), building.getTotalApartments());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Employee not found.");
        }
    }

    private void viewBuildingsWithFreeApartments() {
        List<Building> buildings = buildingController.getBuildingsWithFreeApartments();
        if (buildings.isEmpty()) {
            System.out.println("No buildings with free apartments found.");
            return;
        }

        System.out.println("\nBuildings with free apartments:");
        for (Building building : buildings) {
            int occupancyRate = buildingController.getOccupancyRate(building.getId());
            System.out.printf("ID: %d, Address: %s, Occupancy Rate: %d%%%n",
                    building.getId(), building.getAddress(), occupancyRate);
        }
    }

    private void viewBuildingsByApartmentCount() {
        List<Building> buildings = buildingController.getAllBuildingsSortedByApartmentCount();
        if (buildings.isEmpty()) {
            System.out.println("No buildings found.");
            return;
        }

        System.out.println("\nBuildings sorted by apartment count:");
        for (Building building : buildings) {
            System.out.printf("ID: %d, Address: %s, Total Apartments: %d%n",
                    building.getId(), building.getAddress(), building.getTotalApartments());
        }
    }

    private void viewBuildingOccupancyRate() {
        long buildingId = getIntInput("Enter building ID: ");
        try {
            int occupancyRate = buildingController.getOccupancyRate(buildingId);
            Building building = buildingController.getBuildingById(buildingId);
            System.out.printf("\nBuilding: %s%nOccupancy Rate: %d%%%n",
                    building.getAddress(), occupancyRate);
        } catch (IllegalArgumentException e) {
            System.out.println("Building not found.");
        }
    }
    private void handleApartmentMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Apartment Management ===");
            System.out.println("1. Add new apartment");
            System.out.println("2. View all apartments");
            System.out.println("3. View apartment details");
            System.out.println("4. Update apartment");
            System.out.println("5. Delete apartment");
            System.out.println("6. View apartments by building");
            System.out.println("7. View apartments by floor");
            System.out.println("8. View apartments with unpaid fees");
            System.out.println("9. Calculate monthly fee for apartment");
            System.out.println("10. Add owner to apartment");
            System.out.println("11. Add resident to apartment");
            System.out.println("0. Back to main menu");

            int choice = getIntInput("Enter your choice: ");
            entityManager.getTransaction().begin();
            try {
                switch (choice) {
                    case 1 -> addApartment();
                    case 2 -> viewAllApartments();
                    case 3 -> viewApartmentDetails();
                    case 4 -> updateApartment();
                    case 5 -> deleteApartment();
                    case 6 -> viewApartmentsByBuilding();
                    case 7 -> viewApartmentsByFloor();
                    case 8 -> viewApartmentsWithUnpaidFees();
                    case 9 -> calculateMonthlyFee();
                    case 10 -> addOwnerToApartment();
                    case 11 -> addResidentToApartment();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addApartment() {
        long buildingId = getIntInput("Enter building ID: ");
        String apartmentNumber = getStringInput("Enter apartment number: ");
        int floorNumber = getIntInput("Enter floor number: ");
        BigDecimal area = getBigDecimalInput("Enter apartment area (in square meters): ");

        try {
            Apartment apartment = apartmentController.createApartment(
                    apartmentNumber, floorNumber, area, buildingId);
            System.out.println("Apartment created successfully with ID: " + apartment.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllApartments() {
        List<Apartment> apartments = apartmentController.getAllApartments();
        if (apartments.isEmpty()) {
            System.out.println("No apartments found.");
            return;
        }

        System.out.println("\nApartment List:");
        for (Apartment apartment : apartments) {
            System.out.printf("ID: %d, Number: %s, Floor: %d, Area: %.2f sq.m%n",
                    apartment.getId(), apartment.getApartmentNumber(),
                    apartment.getFloorNumber(), apartment.getArea());
        }
    }

    private void viewApartmentDetails() {
        long apartmentId = getIntInput("Enter apartment ID: ");
        try {
            Apartment apartment = apartmentController.getApartmentById(apartmentId);
            System.out.println("\nApartment Details:");
            System.out.println("ID: " + apartment.getId());
            System.out.println("Number: " + apartment.getApartmentNumber());
            System.out.println("Floor: " + apartment.getFloorNumber());
            System.out.println("Area: " + apartment.getArea() + " sq.m");
            System.out.println("Building: " + apartment.getBuilding().getAddress());
            System.out.println("Number of residents: " +
                    apartmentController.getResidentCount(apartmentId));
            System.out.println("Monthly fee: " +
                    apartmentController.calculateMonthlyFee(apartmentId));
            System.out.println("Has overdue fees: " +
                    (apartmentController.hasOverdueFees(apartmentId) ? "Yes" : "No"));
        } catch (IllegalArgumentException e) {
            System.out.println("Apartment not found.");
        }
    }

    private void updateApartment() {
        long apartmentId = getIntInput("Enter apartment ID to update: ");
        try {
            Apartment apartment = apartmentController.getApartmentById(apartmentId);

            String apartmentNumber = getStringInput("Enter new apartment number (press Enter to keep current: " +
                    apartment.getApartmentNumber() + "): ");
            String floorNumberStr = getStringInput("Enter new floor number (press Enter to keep current: " +
                    apartment.getFloorNumber() + "): ");
            String areaStr = getStringInput("Enter new area (press Enter to keep current: " +
                    apartment.getArea() + "): ");

            apartmentNumber = apartmentNumber.isEmpty() ? apartment.getApartmentNumber() : apartmentNumber;
            int floorNumber = floorNumberStr.isEmpty() ?
                    apartment.getFloorNumber() : Integer.parseInt(floorNumberStr);
            BigDecimal area = areaStr.isEmpty() ?
                    apartment.getArea() : new BigDecimal(areaStr);

            apartmentController.updateApartment(apartmentId, apartmentNumber, floorNumber, area);
            System.out.println("Apartment updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Apartment not found or invalid input.");
        }
    }

    private void deleteApartment() {
        long apartmentId = getIntInput("Enter apartment ID to delete: ");
        try {
            apartmentController.deleteApartment(apartmentId);
            System.out.println("Apartment deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting apartment: " + e.getMessage());
        }
    }

    private void viewApartmentsByBuilding() {
        long buildingId = getIntInput("Enter building ID: ");
        try {
            List<Apartment> apartments = apartmentController.getApartmentsByBuilding(buildingId);
            if (apartments.isEmpty()) {
                System.out.println("No apartments found in this building.");
                return;
            }

            System.out.println("\nApartments in building:");
            for (Apartment apartment : apartments) {
                System.out.printf("ID: %d, Number: %s, Floor: %d, Area: %.2f sq.m%n",
                        apartment.getId(), apartment.getApartmentNumber(),
                        apartment.getFloorNumber(), apartment.getArea());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Building not found.");
        }
    }

    private void viewApartmentsByFloor() {
        int floorNumber = getIntInput("Enter floor number: ");
        List<Apartment> apartments = apartmentController.findByFloorNumber(floorNumber);
        if (apartments.isEmpty()) {
            System.out.println("No apartments found on this floor.");
            return;
        }

        System.out.println("\nApartments on floor " + floorNumber + ":");
        for (Apartment apartment : apartments) {
            System.out.printf("ID: %d, Number: %s, Area: %.2f sq.m, Building: %s%n",
                    apartment.getId(), apartment.getApartmentNumber(),
                    apartment.getArea(), apartment.getBuilding().getAddress());
        }
    }

    private void viewApartmentsWithUnpaidFees() {
        List<Apartment> apartments = apartmentController.getApartmentsWithUnpaidFees();
        if (apartments.isEmpty()) {
            System.out.println("No apartments with unpaid fees found.");
            return;
        }

        System.out.println("\nApartments with unpaid fees:");
        for (Apartment apartment : apartments) {
            System.out.printf("ID: %d, Number: %s, Building: %s%n",
                    apartment.getId(), apartment.getApartmentNumber(),
                    apartment.getBuilding().getAddress());
        }
    }

    private void calculateMonthlyFee() {
        long apartmentId = getIntInput("Enter apartment ID: ");
        try {
            BigDecimal monthlyFee = apartmentController.calculateMonthlyFee(apartmentId);
            Apartment apartment = apartmentController.getApartmentById(apartmentId);
            System.out.printf("\nMonthly fee for apartment %s: %.2f%n",
                    apartment.getApartmentNumber(), monthlyFee);
        } catch (IllegalArgumentException e) {
            System.out.println("Apartment not found.");
        }
    }

    private void addOwnerToApartment() {
        long apartmentId = getIntInput("Enter apartment ID: ");
        long ownerId = getIntInput("Enter owner ID: ");
        try {
            apartmentController.addOwner(apartmentId, ownerId);
            System.out.println("Owner added to apartment successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addResidentToApartment() {
        long apartmentId = getIntInput("Enter apartment ID: ");
        long residentId = getIntInput("Enter resident ID: ");
        try {
            apartmentController.addResident(apartmentId, residentId);
            System.out.println("Resident added to apartment successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void handleOwnerMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Owner Management ===");
            System.out.println("1. Add new owner");
            System.out.println("2. View all owners");
            System.out.println("3. View owner details");
            System.out.println("4. Update owner");
            System.out.println("5. Delete owner");
            System.out.println("6. Search owners by name");
            System.out.println("7. View owners with multiple apartments");
            System.out.println("8. View owners by building");
            System.out.println("9. Transfer apartment ownership");
            System.out.println("0. Back to main menu");

            int choice = getIntInput("Enter your choice: ");
            entityManager.getTransaction().begin();
            try {
                switch (choice) {
                    case 1 -> addOwner();
                    case 2 -> viewAllOwners();
                    case 3 -> viewOwnerDetails();
                    case 4 -> updateOwner();
                    case 5 -> deleteOwner();
                    case 6 -> searchOwnersByName();
                    case 7 -> viewOwnersWithMultipleApartments();
                    case 8 -> viewOwnersByBuilding();
                    case 9 -> transferOwnership();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addOwner() {
        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone number: ");

        try {
            Owner owner = ownerController.createOwner(firstName, lastName, email, phone);
            System.out.println("Owner created successfully with ID: " + owner.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllOwners() {
        List<Owner> owners = ownerController.getAllOwners();
        if (owners.isEmpty()) {
            System.out.println("No owners found.");
            return;
        }

        System.out.println("\nOwner List:");
        for (Owner owner : owners) {
            System.out.printf("ID: %d, Name: %s %s, Email: %s%n",
                    owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail());
        }
    }

    private void viewOwnerDetails() {
        long ownerId = getIntInput("Enter owner ID: ");
        try {
            Owner owner = ownerController.getOwnerById(ownerId);
            System.out.println("\nOwner Details:");
            System.out.println("ID: " + owner.getId());
            System.out.println("Name: " + owner.getFirstName() + " " + owner.getLastName());
            System.out.println("Email: " + owner.getEmail());
            System.out.println("Phone: " + owner.getPhone());
            System.out.println("Number of owned apartments: " +
                    ownerController.getNumberOfOwnedApartments(ownerId));
        } catch (IllegalArgumentException e) {
            System.out.println("Owner not found.");
        }
    }

    private void updateOwner() {
        long ownerId = getIntInput("Enter owner ID to update: ");
        try {
            Owner owner = ownerController.getOwnerById(ownerId);

            String firstName = getStringInput("Enter new first name (press Enter to keep current: " +
                    owner.getFirstName() + "): ");
            String lastName = getStringInput("Enter new last name (press Enter to keep current: " +
                    owner.getLastName() + "): ");
            String email = getStringInput("Enter new email (press Enter to keep current: " +
                    owner.getEmail() + "): ");
            String phone = getStringInput("Enter new phone (press Enter to keep current: " +
                    owner.getPhone() + "): ");

            firstName = firstName.isEmpty() ? owner.getFirstName() : firstName;
            lastName = lastName.isEmpty() ? owner.getLastName() : lastName;
            email = email.isEmpty() ? owner.getEmail() : email;
            phone = phone.isEmpty() ? owner.getPhone() : phone;

            ownerController.updateOwner(ownerId, firstName, lastName, email, phone);
            System.out.println("Owner updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Owner not found.");
        }
    }

    private void deleteOwner() {
        long ownerId = getIntInput("Enter owner ID to delete: ");
        try {
            ownerController.deleteOwner(ownerId);
            System.out.println("Owner deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting owner: " + e.getMessage());
        }
    }

    private void searchOwnersByName() {
        String firstName = getStringInput("Enter first name (or press Enter to skip): ");
        String lastName = getStringInput("Enter last name (or press Enter to skip): ");

        List<Owner> owners = ownerController.findByFirstNameAndLastName(firstName, lastName);
        if (owners.isEmpty()) {
            System.out.println("No owners found with the given name.");
            return;
        }

        System.out.println("\nFound owners:");
        for (Owner owner : owners) {
            System.out.printf("ID: %d, Name: %s %s, Email: %s%n",
                    owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail());
        }
    }

    private void viewOwnersWithMultipleApartments() {
        List<Owner> owners = ownerController.getOwnersWithMultipleApartments();
        if (owners.isEmpty()) {
            System.out.println("No owners with multiple apartments found.");
            return;
        }

        System.out.println("\nOwners with multiple apartments:");
        for (Owner owner : owners) {
            int apartmentCount = ownerController.getNumberOfOwnedApartments(owner.getId());
            System.out.printf("ID: %d, Name: %s %s, Number of apartments: %d%n",
                    owner.getId(), owner.getFirstName(), owner.getLastName(), apartmentCount);
        }
    }

    private void viewOwnersByBuilding() {
        long buildingId = getIntInput("Enter building ID: ");
        try {
            List<Owner> owners = ownerController.findByBuildingId(buildingId);
            if (owners.isEmpty()) {
                System.out.println("No owners found for this building.");
                return;
            }

            System.out.println("\nOwners in building:");
            for (Owner owner : owners) {
                System.out.printf("ID: %d, Name: %s %s, Email: %s%n",
                        owner.getId(), owner.getFirstName(), owner.getLastName(), owner.getEmail());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Building not found.");
        }
    }

    private void transferOwnership() {
        long apartmentId = getIntInput("Enter apartment ID: ");
        long oldOwnerId = getIntInput("Enter current owner ID: ");
        long newOwnerId = getIntInput("Enter new owner ID: ");

        try {
            ownerController.transferOwnership(apartmentId, oldOwnerId, newOwnerId);
            System.out.println("Ownership transferred successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void handleResidentMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Resident Management ===");
            System.out.println("1. Add new resident");
            System.out.println("2. View all residents");
            System.out.println("3. View resident details");
            System.out.println("4. Update resident");
            System.out.println("5. Delete resident");
            System.out.println("6. View residents by apartment");
            System.out.println("7. View residents by building");
            System.out.println("8. View residents with pets");
            System.out.println("9. View residents using elevator");
            System.out.println("10. Move resident to different apartment");
            System.out.println("11. Update resident status (pet/elevator)");
            System.out.println("0. Back to main menu");

            int choice = getIntInput("Enter your choice: ");
            entityManager.getTransaction().begin();
            try {
                switch (choice) {
                    case 1 -> addResident();
                    case 2 -> viewAllResidents();
                    case 3 -> viewResidentDetails();
                    case 4 -> updateResident();
                    case 5 -> deleteResident();
                    case 6 -> viewResidentsByApartment();
                    case 7 -> viewResidentsByBuilding();
                    case 8 -> viewResidentsWithPets();
                    case 9 -> viewResidentsUsingElevator();
                    case 10 -> moveResident();
                    case 11 -> updateResidentStatus();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addResident() {
        long apartmentId = getIntInput("Enter apartment ID: ");
        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");
        int age = getIntInput("Enter age: ");
        boolean hasPet = getStringInput("Has pet? (yes/no): ").toLowerCase().startsWith("y");
        boolean usesElevator = getStringInput("Uses elevator? (yes/no): ").toLowerCase().startsWith("y");

        try {
            Resident resident = residentController.createResident(
                    firstName, lastName, age, hasPet, usesElevator, apartmentId);
            System.out.println("Resident created successfully with ID: " + resident.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllResidents() {
        List<Resident> residents = residentController.getAllResidents();
        if (residents.isEmpty()) {
            System.out.println("No residents found.");
            return;
        }

        System.out.println("\nResident List:");
        for (Resident resident : residents) {
            System.out.printf("ID: %d, Name: %s %s, Age: %d%n",
                    resident.getId(), resident.getFirstName(), resident.getLastName(), resident.getAge());
        }
    }

    private void viewResidentDetails() {
        long residentId = getIntInput("Enter resident ID: ");
        try {
            Resident resident = residentController.getResidentById(residentId);
            System.out.println("\nResident Details:");
            System.out.println("ID: " + resident.getId());
            System.out.println("Name: " + resident.getFirstName() + " " + resident.getLastName());
            System.out.println("Age: " + resident.getAge());
            System.out.println("Has Pet: " + (resident.getHasPet() ? "Yes" : "No"));
            System.out.println("Uses Elevator: " + (resident.getUsesElevator() ? "Yes" : "No"));
            System.out.println("Apartment: " + resident.getApartment().getApartmentNumber());
            System.out.println("Building: " + resident.getApartment().getBuilding().getAddress());
            System.out.println("Eligible for elevator fee: " +
                    (residentController.isEligibleForElevatorFee(residentId) ? "Yes" : "No"));
        } catch (IllegalArgumentException e) {
            System.out.println("Resident not found.");
        }
    }

    private void updateResident() {
        long residentId = getIntInput("Enter resident ID to update: ");
        try {
            Resident resident = residentController.getResidentById(residentId);

            String firstName = getStringInput("Enter new first name (press Enter to keep current: " +
                    resident.getFirstName() + "): ");
            String lastName = getStringInput("Enter new last name (press Enter to keep current: " +
                    resident.getLastName() + "): ");
            String ageStr = getStringInput("Enter new age (press Enter to keep current: " +
                    resident.getAge() + "): ");

            firstName = firstName.isEmpty() ? resident.getFirstName() : firstName;
            lastName = lastName.isEmpty() ? resident.getLastName() : lastName;
            int age = ageStr.isEmpty() ? resident.getAge() : Integer.parseInt(ageStr);

            residentController.updateResident(residentId, firstName, lastName, age,
                    resident.getHasPet(), resident.getUsesElevator());
            System.out.println("Resident updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Resident not found or invalid input.");
        }
    }

    private void deleteResident() {
        long residentId = getIntInput("Enter resident ID to delete: ");
        try {
            residentController.deleteResident(residentId);
            System.out.println("Resident deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting resident: " + e.getMessage());
        }
    }

    private void viewResidentsByApartment() {
        long apartmentId = getIntInput("Enter apartment ID: ");
        try {
            List<Resident> residents = residentController.findByApartmentId(apartmentId);
            if (residents.isEmpty()) {
                System.out.println("No residents found in this apartment.");
                return;
            }

            System.out.println("\nResidents in apartment:");
            for (Resident resident : residents) {
                System.out.printf("ID: %d, Name: %s %s, Age: %d%n",
                        resident.getId(), resident.getFirstName(), resident.getLastName(), resident.getAge());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Apartment not found.");
        }
    }

    private void viewResidentsByBuilding() {
        long buildingId = getIntInput("Enter building ID: ");
        try {
            List<Resident> residents = residentController.findByBuildingId(buildingId);
            if (residents.isEmpty()) {
                System.out.println("No residents found in this building.");
                return;
            }

            System.out.println("\nResidents in building:");
            for (Resident resident : residents) {
                System.out.printf("ID: %d, Name: %s %s, Apartment: %s%n",
                        resident.getId(), resident.getFirstName(), resident.getLastName(),
                        resident.getApartment().getApartmentNumber());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Building not found.");
        }
    }

    private void viewResidentsWithPets() {
        List<Resident> residents = residentController.findResidentsWithPets();
        if (residents.isEmpty()) {
            System.out.println("No residents with pets found.");
            return;
        }

        System.out.println("\nResidents with pets:");
        for (Resident resident : residents) {
            System.out.printf("ID: %d, Name: %s %s, Apartment: %s%n",
                    resident.getId(), resident.getFirstName(), resident.getLastName(),
                    resident.getApartment().getApartmentNumber());
        }
    }

    private void viewResidentsUsingElevator() {
        List<Resident> residents = residentController.findResidentsUsingElevator();
        if (residents.isEmpty()) {
            System.out.println("No residents using elevator found.");
            return;
        }

        System.out.println("\nResidents using elevator:");
        for (Resident resident : residents) {
            System.out.printf("ID: %d, Name: %s %s, Age: %d, Eligible for fee: %s%n",
                    resident.getId(), resident.getFirstName(), resident.getLastName(),
                    resident.getAge(),
                    residentController.isEligibleForElevatorFee(resident.getId()) ? "Yes" : "No");
        }
    }

    private void moveResident() {
        long residentId = getIntInput("Enter resident ID: ");
        long newApartmentId = getIntInput("Enter new apartment ID: ");

        try {
            residentController.moveResident(residentId, newApartmentId);
            System.out.println("Resident moved successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateResidentStatus() {
        long residentId = getIntInput("Enter resident ID: ");
        try {
            Resident resident = residentController.getResidentById(residentId);
            boolean hasPet = getStringInput("Has pet? (yes/no) [Current: " +
                    (resident.getHasPet() ? "yes" : "no") + "]: ").toLowerCase().startsWith("y");
            boolean usesElevator = getStringInput("Uses elevator? (yes/no) [Current: " +
                    (resident.getUsesElevator() ? "yes" : "no") + "]: ").toLowerCase().startsWith("y");

            residentController.updateResidentStatus(residentId, hasPet, usesElevator);
            System.out.println("Resident status updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Resident not found.");
        }
    }
    private void handleFeeMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Fee Management ===");
            System.out.println("1. Create new fee");
            System.out.println("2. View all fees");
            System.out.println("3. View fee details");
            System.out.println("4. Mark fee as paid");
            System.out.println("5. View fees by apartment");
            System.out.println("6. View fees by building");
            System.out.println("7. View overdue fees");
            System.out.println("8. Generate monthly fees for building");
            System.out.println("9. Apply late fee");
            System.out.println("10. View total unpaid fees for apartment");
            System.out.println("11. View total collected fees by building");
            System.out.println("12. Export paid fees to file");
            System.out.println("0. Back to main menu");

            int choice = getIntInput("Enter your choice: ");
            entityManager.getTransaction().begin();
            try {
                switch (choice) {
                    case 1 -> createFee();
                    case 2 -> viewAllFees();
                    case 3 -> viewFeeDetails();
                    case 4 -> markFeeAsPaid();
                    case 5 -> viewFeesByApartment();
                    case 6 -> viewFeesByBuilding();
                    case 7 -> viewOverdueFees();
                    case 8 -> generateMonthlyFees();
                    case 9 -> applyLateFee();
                    case 10 -> viewTotalUnpaidFees();
                    case 11 -> viewTotalCollectedFees();
                    case 12 -> exportPaidFees();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                entityManager.getTransaction().rollback();
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void createFee() {
        long apartmentId = getIntInput("Enter apartment ID: ");
        BigDecimal amount = getBigDecimalInput("Enter fee amount: ");
        System.out.println("Enter due date (in months from now): ");
        int monthsFromNow = getIntInput("Number of months: ");

        try {
            Fee fee = feeController.createFee(apartmentId, amount,
                    LocalDate.now().plusMonths(monthsFromNow));
            System.out.println("Fee created successfully with ID: " + fee.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllFees() {
        List<Fee> fees = feeController.getAllFees();
        if (fees.isEmpty()) {
            System.out.println("No fees found.");
            return;
        }

        System.out.println("\nFee List:");
        for (Fee fee : fees) {
            System.out.printf("ID: %d, Amount: %.2f, Due Date: %s, Status: %s%n",
                    fee.getId(), fee.getAmount(), fee.getDueDate(), fee.getStatus());
        }
    }

    private void viewFeeDetails() {
        long feeId = getIntInput("Enter fee ID: ");
        try {
            Fee fee = feeController.getFeeById(feeId);
            System.out.println("\nFee Details:");
            System.out.println("ID: " + fee.getId());
            System.out.println("Amount: " + fee.getAmount());
            System.out.println("Due Date: " + fee.getDueDate());
            System.out.println("Status: " + fee.getStatus());
            System.out.println("Apartment: " + fee.getApartment().getApartmentNumber());
            System.out.println("Building: " + fee.getApartment().getBuilding().getAddress());
            if (fee.getPaymentDate() != null) {
                System.out.println("Payment Date: " + fee.getPaymentDate());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Fee not found.");
        }
    }

    private void markFeeAsPaid() {
        long feeId = getIntInput("Enter fee ID: ");
        try {
            feeController.markFeeAsPaid(feeId);
            System.out.println("Fee marked as paid successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewFeesByApartment() {
        long apartmentId = getIntInput("Enter apartment ID: ");
        try {
            List<Fee> fees = feeController.getFeesByApartment(apartmentId);
            if (fees.isEmpty()) {
                System.out.println("No fees found for this apartment.");
                return;
            }

            System.out.println("\nFees for apartment:");
            for (Fee fee : fees) {
                System.out.printf("ID: %d, Amount: %.2f, Due Date: %s, Status: %s%n",
                        fee.getId(), fee.getAmount(), fee.getDueDate(), fee.getStatus());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Apartment not found.");
        }
    }

    private void viewFeesByBuilding() {
        long buildingId = getIntInput("Enter building ID: ");
        try {
            List<Fee> fees = feeController.findByBuildingId(buildingId);
            if (fees.isEmpty()) {
                System.out.println("No fees found for this building.");
                return;
            }

            System.out.println("\nFees for building:");
            for (Fee fee : fees) {
                System.out.printf("ID: %d, Apartment: %s, Amount: %.2f, Status: %s%n",
                        fee.getId(), fee.getApartment().getApartmentNumber(),
                        fee.getAmount(), fee.getStatus());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Building not found.");
        }
    }

    private void viewOverdueFees() {
        List<Fee> overdueFees = feeController.getOverdueFees();
        if (overdueFees.isEmpty()) {
            System.out.println("No overdue fees found.");
            return;
        }

        System.out.println("\nOverdue Fees:");
        for (Fee fee : overdueFees) {
            System.out.printf("ID: %d, Amount: %.2f, Due Date: %s, Apartment: %s%n",
                    fee.getId(), fee.getAmount(), fee.getDueDate(),
                    fee.getApartment().getApartmentNumber());
        }
    }

    private void generateMonthlyFees() {
        long buildingId = getIntInput("Enter building ID: ");
        try {
            feeController.generateMonthlyFees(buildingId);
            System.out.println("Monthly fees generated successfully for all apartments in the building.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void applyLateFee() {
        long feeId = getIntInput("Enter fee ID: ");
        BigDecimal lateFeeAmount = getBigDecimalInput("Enter late fee amount: ");

        try {
            feeController.applyLateFee(feeId, lateFeeAmount);
            System.out.println("Late fee applied successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewTotalUnpaidFees() {
        long apartmentId = getIntInput("Enter apartment ID: ");
        try {
            BigDecimal totalUnpaid = feeController.calculateTotalUnpaidFees(apartmentId);
            System.out.printf("\nTotal unpaid fees for apartment: %.2f%n", totalUnpaid);
        } catch (IllegalArgumentException e) {
            System.out.println("Apartment not found.");
        }
    }

    private void viewTotalCollectedFees() {
        long buildingId = getIntInput("Enter building ID: ");
        try {
            BigDecimal totalCollected = feeController.getTotalFeesByBuilding(buildingId);
            System.out.printf("\nTotal collected fees for building: %.2f%n", totalCollected);
        } catch (IllegalArgumentException e) {
            System.out.println("Building not found.");
        }
    }
    private void exportPaidFees() {
        try {
            List<Fee> paidFees = feeController.findByStatus(Fee.FeeStatus.PAID);
            if (paidFees.isEmpty()) {
                System.out.println("No paid fees found to export.");
                return;
            }
            FeeExporter.exportPaidFees(paidFees);
        } catch (Exception e) {
            System.out.println("Error exporting fees: " + e.getMessage());
        }
    }
    public void shutdown() {
        try {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
            if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
            }
            if (scanner != null) {
                scanner.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}