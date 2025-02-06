package repository;

import org.example.entity.Owner;

import java.util.List;

public interface OwnerRepository extends GenericRepository<Owner, Long> {
    List<Owner> findByFirstNameAndLastName(String firstName, String lastName);
    List<Owner> findByEmail(String email);
    // Get owners with multiple apartments
    List<Owner> findOwnersWithMultipleApartments();
    // Get owners with apartments in specific building
    List<Owner> findByBuildingId(Long buildingId);
}