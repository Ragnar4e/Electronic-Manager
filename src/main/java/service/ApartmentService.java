package service;

import org.example.entity.Apartment;
import java.math.BigDecimal;
import java.util.List;

public interface ApartmentService extends GenericService<Apartment, Long> {
    List<Apartment> findByBuildingId(Long buildingId);
    List<Apartment> findByFloorNumber(Integer floorNumber);
    List<Apartment> findByApartmentNumber(String apartmentNumber);
    List<Apartment> findApartmentsWithUnpaidFees();
    List<Apartment> getAllApartmentsSortedByArea();

    // Business logic methods
    BigDecimal calculateMonthlyFee(Long apartmentId);
    void addOwnerToApartment(Long apartmentId, Long ownerId);
    void addResidentToApartment(Long apartmentId, Long residentId);
    boolean hasOverdueFees(Long apartmentId);
    int getResidentCount(Long apartmentId);
}