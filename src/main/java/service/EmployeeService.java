package service;

import org.example.entity.Employee;
import java.util.List;

public interface EmployeeService extends GenericService<Employee, Long> {
    List<Employee> findByCompanyId(Long companyId);
    List<Employee> getAllEmployeesSortedByBuildingCount();
    Employee findEmployeeWithLeastBuildings();
    // Business logic methods
    void assignBuildingToEmployee(Long employeeId, Long buildingId);
    void transferBuildingsToOtherEmployees(Long employeeId);
    int getNumberOfManagedBuildings(Long employeeId);
}