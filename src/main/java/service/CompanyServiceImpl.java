package service;

import jakarta.persistence.EntityManager;
import org.example.entity.Building;
import org.example.entity.Company;
import org.example.entity.Employee;
import repository.BuildingRepository;
import repository.CompanyRepository;
import repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

public class CompanyServiceImpl implements CompanyService {
    private final EntityManager entityManager;
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final BuildingRepository buildingRepository;

    public CompanyServiceImpl(EntityManager entityManager,
                              CompanyRepository companyRepository,
                              EmployeeRepository employeeRepository,
                              BuildingRepository buildingRepository) {
        this.entityManager = entityManager;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.buildingRepository = buildingRepository;
    }

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public void delete(Company company) {
        companyRepository.delete(company);
    }

    @Override
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return companyRepository.existsById(id);
    }

    @Override
    public List<Company> findByName(String name) {
        return companyRepository.findByName(name);
    }

    @Override
    public List<Company> getAllCompaniesSortedByRevenue() {
        return companyRepository.findAllSortedByRevenue();
    }

    @Override
    public void reassignEmployeesAfterEmployeeDismissal(Long dismissedEmployeeId) {
        Employee leastLoadedEmployee = employeeRepository.findEmployeeWithLeastBuildings();
        if (leastLoadedEmployee != null) {
            List<Building> buildingsToReassign = buildingRepository.findByEmployeeId(dismissedEmployeeId);
            for (Building building : buildingsToReassign) {
                building.setEmployee(leastLoadedEmployee);
                buildingRepository.save(building);
            }
        }
    }

    @Override
    public void distributeNewBuildingToLeastLoadedEmployee(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));

        Employee leastLoadedEmployee = employeeRepository.findEmployeeWithLeastBuildings();

        // Business validation: Check if there are available employees
        if (leastLoadedEmployee == null) {
            throw new IllegalStateException("No available employees to assign building");
        }

        // Business validation: Check if the building is already assigned
        if (building.getEmployee() != null) {
            throw new IllegalStateException("Building is already assigned to an employee");
        }

        building.setEmployee(leastLoadedEmployee);
        buildingRepository.save(building);
    }
}
