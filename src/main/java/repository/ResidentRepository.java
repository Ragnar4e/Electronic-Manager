package repository;

import org.example.entity.Resident;

import java.util.List;

public interface ResidentRepository extends GenericRepository<Resident, Long> {
    List<Resident> findByApartmentId(Long apartmentId);
    List<Resident> findByBuildingId(Long buildingId);
    // Find residents over certain age
    List<Resident> findByAgeGreaterThan(Integer age);
    // Find residents with pets
    List<Resident> findResidentsWithPets();
    // Find residents using elevator
    List<Resident> findResidentsUsingElevator();
}