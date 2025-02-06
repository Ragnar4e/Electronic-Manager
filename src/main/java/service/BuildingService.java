package service;

import org.example.entity.Building;
import java.util.List;

public interface BuildingService extends GenericService<Building, Long> {
    List<Building> findByEmployeeId(Long employeeId);
    List<Building> findByAddress(String address);
    List<Building> getAllBuildingsSortedByApartmentCount();
    List<Building> findBuildingsWithFreeApartments();

    // Business logic methods
    boolean hasAvailableApartments(Long buildingId);
    int getOccupancyRate(Long buildingId);
    void reassignBuildingToEmployee(Long buildingId, Long newEmployeeId);
    int getTotalResidents(Long buildingId);
}