package controller;

import org.example.entity.Building;
import service.BuildingService;
import java.math.BigDecimal;
import java.util.List;

public class BuildingController {
    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    // Create operations
    public Building createBuilding(String address, Integer totalFloors,
                                   Integer totalApartments, BigDecimal builtUpArea,
                                   BigDecimal commonArea, Long employeeId) {
        Building building = new Building();
        building.setAddress(address);
        building.setTotalFloors(totalFloors);
        building.setTotalApartments(totalApartments);
        building.setBuiltUpArea(builtUpArea);
        building.setCommonArea(commonArea);
        return buildingService.save(building);
    }

    // Read operations
    public Building getBuildingById(Long id) {
        return buildingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with ID: " + id));
    }

    public List<Building> getAllBuildings() {
        return buildingService.findAll();
    }

    public List<Building> getBuildingsByEmployee(Long employeeId) {
        return buildingService.findByEmployeeId(employeeId);
    }

    public List<Building> getBuildingsByAddress(String address) {
        return buildingService.findByAddress(address);
    }

    public List<Building> getBuildingsWithFreeApartments() {
        return buildingService.findBuildingsWithFreeApartments();
    }

    // New method
    public List<Building> getAllBuildingsSortedByApartmentCount() {
        return buildingService.getAllBuildingsSortedByApartmentCount();
    }

    // Update operations
    public Building updateBuilding(Long id, String address, Integer totalFloors,
                                   Integer totalApartments, BigDecimal builtUpArea,
                                   BigDecimal commonArea) {
        Building building = getBuildingById(id);
        building.setAddress(address);
        building.setTotalFloors(totalFloors);
        building.setTotalApartments(totalApartments);
        building.setBuiltUpArea(builtUpArea);
        building.setCommonArea(commonArea);
        return buildingService.save(building);
    }

    // Delete operations
    public void deleteBuilding(Long id) {
        buildingService.deleteById(id);
    }

    // Business operations
    public boolean hasAvailableApartments(Long buildingId) {
        return buildingService.hasAvailableApartments(buildingId);
    }

    public int getOccupancyRate(Long buildingId) {
        return buildingService.getOccupancyRate(buildingId);
    }

    public void reassignToNewEmployee(Long buildingId, Long newEmployeeId) {
        buildingService.reassignBuildingToEmployee(buildingId, newEmployeeId);
    }

    public int getTotalResidents(Long buildingId) {
        return buildingService.getTotalResidents(buildingId);
    }
}
