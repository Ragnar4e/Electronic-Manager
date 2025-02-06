package controller;

import org.example.entity.Apartment;
import service.ApartmentService;
import java.math.BigDecimal;
import java.util.List;

public class ApartmentController {
    private final ApartmentService apartmentService;

    public ApartmentController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    // Create operations
    public Apartment createApartment(String apartmentNumber, Integer floorNumber,
                                     BigDecimal area, Long buildingId) {
        Apartment apartment = new Apartment();
        apartment.setApartmentNumber(apartmentNumber);
        apartment.setFloorNumber(floorNumber);
        apartment.setArea(area);
        return apartmentService.save(apartment);
    }

    // Read operations
    public Apartment getApartmentById(Long id) {
        return apartmentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Apartment not found with ID: " + id));
    }

    public List<Apartment> getAllApartments() {
        return apartmentService.findAll();
    }

    public List<Apartment> getApartmentsByBuilding(Long buildingId) {
        return apartmentService.findByBuildingId(buildingId);
    }

    public List<Apartment> getApartmentsByFloor(Integer floorNumber) {
        return apartmentService.findByFloorNumber(floorNumber);
    }

    public List<Apartment> getApartmentsWithUnpaidFees() {
        return apartmentService.findApartmentsWithUnpaidFees();
    }

    // Read operation for finding apartments by floor number
    public List<Apartment> findByFloorNumber(Integer floorNumber) {
        return apartmentService.findByFloorNumber(floorNumber);
    }

    // Update operations
    public Apartment updateApartment(Long id, String apartmentNumber,
                                     Integer floorNumber, BigDecimal area) {
        Apartment apartment = getApartmentById(id);
        apartment.setApartmentNumber(apartmentNumber);
        apartment.setFloorNumber(floorNumber);
        apartment.setArea(area);
        return apartmentService.save(apartment);
    }

    // Delete operations
    public void deleteApartment(Long id) {
        apartmentService.deleteById(id);
    }

    // Business operations
    public BigDecimal calculateMonthlyFee(Long apartmentId) {
        return apartmentService.calculateMonthlyFee(apartmentId);
    }

    public void addOwner(Long apartmentId, Long ownerId) {
        apartmentService.addOwnerToApartment(apartmentId, ownerId);
    }

    public void addResident(Long apartmentId, Long residentId) {
        apartmentService.addResidentToApartment(apartmentId, residentId);
    }

    public boolean hasOverdueFees(Long apartmentId) {
        return apartmentService.hasOverdueFees(apartmentId);
    }

    public int getResidentCount(Long apartmentId) {
        return apartmentService.getResidentCount(apartmentId);
    }
}
