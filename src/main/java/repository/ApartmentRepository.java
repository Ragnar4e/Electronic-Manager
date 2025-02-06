package repository;

import org.example.entity.Apartment;

import java.util.List;

public interface ApartmentRepository extends GenericRepository<Apartment, Long> {
    List<Apartment> findByBuildingId(Long buildingId);
    List<Apartment> findByFloorNumber(Integer floorNumber);
    List<Apartment> findByApartmentNumber(String apartmentNumber);
    // Get apartments with unpaid fees
    List<Apartment> findApartmentsWithUnpaidFees();
    // Get apartments sorted by area
    List<Apartment> findAllSortedByArea();
}