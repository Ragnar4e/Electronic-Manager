package repository;

import org.example.entity.Building;

import java.util.List;

public interface BuildingRepository extends GenericRepository<Building, Long> {
    List<Building> findByEmployeeId(Long employeeId);
    List<Building> findByAddress(String address);
    // Get buildings sorted by number of apartments
    List<Building> findAllSortedByApartmentCount();
    // Get buildings with free apartments (total_apartments > count of actual apartments)
    List<Building> findBuildingsWithFreeApartments();
}