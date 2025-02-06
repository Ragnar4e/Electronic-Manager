package service;

import jakarta.persistence.EntityManager;
import org.example.entity.Building;
import org.example.entity.Employee;
import repository.BuildingRepository;
import repository.EmployeeRepository;
import repository.ApartmentRepository;

import java.util.List;
import java.util.Optional;

public class BuildingServiceImpl implements BuildingService {
    private final EntityManager entityManager;
    private final BuildingRepository buildingRepository;
    private final EmployeeRepository employeeRepository;
    private final ApartmentRepository apartmentRepository;

    public BuildingServiceImpl(EntityManager entityManager,
                               BuildingRepository buildingRepository,
                               EmployeeRepository employeeRepository,
                               ApartmentRepository apartmentRepository) {
        this.entityManager = entityManager;
        this.buildingRepository = buildingRepository;
        this.employeeRepository = employeeRepository;
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public Building save(Building building) {
        return buildingRepository.save(building);
    }

    @Override
    public Optional<Building> findById(Long id) {
        return buildingRepository.findById(id);
    }

    @Override
    public List<Building> findAll() {
        return buildingRepository.findAll();
    }

    @Override
    public void delete(Building building) {
        buildingRepository.delete(building);
    }

    @Override
    public void deleteById(Long id) {
        buildingRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return buildingRepository.existsById(id);
    }

    @Override
    public List<Building> findByEmployeeId(Long employeeId) {
        return buildingRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<Building> findByAddress(String address) {
        return buildingRepository.findByAddress(address);
    }

    @Override
    public List<Building> getAllBuildingsSortedByApartmentCount() {
        return buildingRepository.findAllSortedByApartmentCount();
    }

    @Override
    public List<Building> findBuildingsWithFreeApartments() {
        return buildingRepository.findBuildingsWithFreeApartments();
    }

    @Override
    public boolean hasAvailableApartments(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));
        return building.getApartments().size() < building.getTotalApartments();
    }

    @Override
    public int getOccupancyRate(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));
        int totalApartments = building.getTotalApartments();
        int occupiedApartments = building.getApartments().size();
        return (occupiedApartments * 100) / totalApartments;
    }

    @Override
    public void reassignBuildingToEmployee(Long buildingId, Long newEmployeeId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));

        Employee newEmployee = employeeRepository.findById(newEmployeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        // Business validation: Ensure the employee is not overloaded
        int currentBuildingCount = newEmployee.getManagedBuildings().size();
        if (currentBuildingCount >= 10) { // Example: maximum 10 buildings per employee
            throw new IllegalStateException("Employee has reached the maximum number of managed buildings");
        }

        building.setEmployee(newEmployee);
        buildingRepository.save(building);
    }

    @Override
    public int getTotalResidents(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));
        return building.getApartments().stream()
                .mapToInt(apartment -> apartment.getResidents().size())
                .sum();
    }
}
