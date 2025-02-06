package service;

import jakarta.persistence.EntityManager;
import org.example.entity.Employee;
import org.example.entity.Building;
import repository.EmployeeRepository;
import repository.BuildingRepository;
import java.util.List;
import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {
    private final EntityManager entityManager;
    private final EmployeeRepository employeeRepository;
    private final BuildingRepository buildingRepository;

    public EmployeeServiceImpl(EntityManager entityManager,
                               EmployeeRepository employeeRepository,
                               BuildingRepository buildingRepository) {
        this.entityManager = entityManager;
        this.employeeRepository = employeeRepository;
        this.buildingRepository = buildingRepository;
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return employeeRepository.existsById(id);
    }

    @Override
    public List<Employee> findByCompanyId(Long companyId) {
        return employeeRepository.findByCompanyId(companyId);
    }

    @Override
    public List<Employee> getAllEmployeesSortedByBuildingCount() {
        return employeeRepository.findAllSortedByManagedBuildingsCount();
    }

    @Override
    public Employee findEmployeeWithLeastBuildings() {
        return employeeRepository.findEmployeeWithLeastBuildings();
    }

    @Override
    public void assignBuildingToEmployee(Long employeeId, Long buildingId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));

        // Проверка на работното натоварване
        int currentBuildings = employee.getManagedBuildings().size();
        if (currentBuildings >= 10) {
            throw new IllegalStateException("Employee has reached maximum building capacity");
        }

        // Проверка дали сградата вече не е назначена на друг служител
        if (building.getEmployee() != null &&
                !building.getEmployee().getId().equals(employeeId)) {
            throw new IllegalStateException("Building is already assigned to another employee");
        }

        building.setEmployee(employee);
        buildingRepository.save(building);
    }

    @Override
    public void transferBuildingsToOtherEmployees(Long employeeId) {
        Employee leastLoadedEmployee = findEmployeeWithLeastBuildings();
        if (leastLoadedEmployee != null && !leastLoadedEmployee.getId().equals(employeeId)) {
            List<Building> buildings = buildingRepository.findByEmployeeId(employeeId);
            for (Building building : buildings) {
                building.setEmployee(leastLoadedEmployee);
                buildingRepository.save(building);
            }
        }
    }

    @Override
    public int getNumberOfManagedBuildings(Long employeeId) {
        return buildingRepository.findByEmployeeId(employeeId).size();
    }
}
