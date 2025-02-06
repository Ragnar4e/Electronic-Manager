package repository;

import org.example.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends GenericRepository<Employee, Long> {
    List<Employee> findByCompanyId(Long companyId);
    // Find employees sorted by number of managed buildings
    List<Employee> findAllSortedByManagedBuildingsCount();
    // Find employee with least number of buildings
    Employee findEmployeeWithLeastBuildings();
}