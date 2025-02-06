package controller;

import org.example.entity.Resident;
import service.ResidentService;
import java.util.List;

public class ResidentController {
    private final ResidentService residentService;

    public ResidentController(ResidentService residentService) {
        this.residentService = residentService;
    }

    // Create operations
    public Resident createResident(String firstName, String lastName, Integer age,
                                   Boolean hasPet, Boolean usesElevator, Long apartmentId) {
        Resident resident = new Resident();
        resident.setFirstName(firstName);
        resident.setLastName(lastName);
        resident.setAge(age);
        resident.setHasPet(hasPet);
        resident.setUsesElevator(usesElevator);
        return residentService.save(resident);
    }

    // Read operations
    public Resident getResidentById(Long id) {
        return residentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resident not found with ID: " + id));
    }

    public List<Resident> getAllResidents() {
        return residentService.findAll();
    }

    public List<Resident> getResidentsByApartment(Long apartmentId) {
        return residentService.findByApartmentId(apartmentId);
    }

    public List<Resident> getResidentsByBuilding(Long buildingId) {
        return residentService.findByBuildingId(buildingId);
    }

    public List<Resident> getResidentsWithPets() {
        return residentService.findResidentsWithPets();
    }

    public List<Resident> getResidentsUsingElevator() {
        return residentService.findResidentsUsingElevator();
    }

    public List<Resident> findByApartmentId(Long apartmentId) {
        return residentService.findByApartmentId(apartmentId);
    }

    public List<Resident> findByBuildingId(Long buildingId) {
        return residentService.findByBuildingId(buildingId);
    }

    public List<Resident> findResidentsWithPets() {
        return residentService.findResidentsWithPets();
    }

    public List<Resident> findResidentsUsingElevator() {
        return residentService.findResidentsUsingElevator();
    }

    // Update operations
    public Resident updateResident(Long id, String firstName, String lastName,
                                   Integer age, Boolean hasPet, Boolean usesElevator) {
        Resident resident = getResidentById(id);
        resident.setFirstName(firstName);
        resident.setLastName(lastName);
        resident.setAge(age);
        resident.setHasPet(hasPet);
        resident.setUsesElevator(usesElevator);
        return residentService.save(resident);
    }

    // Delete operations
    public void deleteResident(Long id) {
        residentService.deleteById(id);
    }

    // Business operations
    public void moveResident(Long residentId, Long newApartmentId) {
        residentService.moveResident(residentId, newApartmentId);
    }

    public boolean isEligibleForElevatorFee(Long residentId) {
        return residentService.isEligibleForElevatorFee(residentId);
    }

    public boolean hasPetFee(Long residentId) {
        return residentService.hasPetFee(residentId);
    }

    public void updateResidentStatus(Long residentId, boolean hasPet, boolean usesElevator) {
        residentService.updateResidentStatus(residentId, hasPet, usesElevator);
    }
}
