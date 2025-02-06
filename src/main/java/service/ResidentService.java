package service;

import org.example.entity.Resident;
import java.util.List;

public interface ResidentService extends GenericService<Resident, Long> {
    List<Resident> findByApartmentId(Long apartmentId);
    List<Resident> findByBuildingId(Long buildingId);
    List<Resident> findByAgeGreaterThan(Integer age);
    List<Resident> findResidentsWithPets();
    List<Resident> findResidentsUsingElevator();

    // Business logic methods
    void moveResident(Long residentId, Long newApartmentId);
    boolean isEligibleForElevatorFee(Long residentId);
    boolean hasPetFee(Long residentId);
    void updateResidentStatus(Long residentId, boolean hasPet, boolean usesElevator);
}