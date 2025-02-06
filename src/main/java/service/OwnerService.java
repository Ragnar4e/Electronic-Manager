package service;

import org.example.entity.Owner;
import java.util.List;

public interface OwnerService extends GenericService<Owner, Long> {
    List<Owner> findByFirstNameAndLastName(String firstName, String lastName);
    List<Owner> findByEmail(String email);
    List<Owner> findOwnersWithMultipleApartments();
    List<Owner> findByBuildingId(Long buildingId);

    // Business logic methods
    void transferOwnership(Long apartmentId, Long oldOwnerId, Long newOwnerId);
    int getNumberOfOwnedApartments(Long ownerId);
    boolean isOwnerOfApartment(Long ownerId, Long apartmentId);
}