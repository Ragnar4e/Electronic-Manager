package controller;

import org.example.entity.Employee;
import service.EmployeeService;
import java.util.List;

public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create operations
    public Employee createEmployee(Long companyId, String firstName, String lastName,
                                   String email, String phone) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPhone(phone);
        return employeeService.save(employee);
    }

    // Read operations
    public Employee getEmployeeById(Long id) {
        return employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + id));
    }

    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    public List<Employee> getEmployeesByCompany(Long companyId) {
        return employeeService.findByCompanyId(companyId);
    }

    public List<Employee> getEmployeesSortedByBuildingCount() {
        return employeeService.getAllEmployeesSortedByBuildingCount();
    }

    public Employee getEmployeeWithLeastBuildings() {
        return employeeService.findEmployeeWithLeastBuildings();
    }

    // Update operations
    public Employee updateEmployee(Long id, String firstName, String lastName,
                                   String email, String phone) {
        Employee employee = getEmployeeById(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPhone(phone);
        return employeeService.save(employee);
    }

    // Delete operations
    public void deleteEmployee(Long id) {
        employeeService.deleteById(id);
    }

    // Business operations
    public void assignBuildingToEmployee(Long employeeId, Long buildingId) {
        employeeService.assignBuildingToEmployee(employeeId, buildingId);
    }

    public void transferBuildings(Long employeeId) {
        employeeService.transferBuildingsToOtherEmployees(employeeId);
    }

    public int getManagedBuildingsCount(Long employeeId) {
        return employeeService.getNumberOfManagedBuildings(employeeId);
    }
}