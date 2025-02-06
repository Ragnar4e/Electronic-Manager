package controller;

import org.example.entity.Owner;
import service.OwnerService;
import java.util.List;

public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    // Create operations
    public Owner createOwner(String firstName, String lastName, String email, String phone) {
        Owner owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setEmail(email);
        owner.setPhone(phone);
        return ownerService.save(owner);
    }

    // Read operations
    public Owner getOwnerById(Long id) {
        return ownerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found with ID: " + id));
    }

    public List<Owner> getAllOwners() {
        return ownerService.findAll();
    }

    public List<Owner> findByName(String firstName, String lastName) {
        return ownerService.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<Owner> findByFirstNameAndLastName(String firstName, String lastName) {
        return ownerService.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<Owner> findByBuildingId(Long buildingId) {
        return ownerService.findByBuildingId(buildingId);
    }

    public List<Owner> getOwnersWithMultipleApartments() {
        return ownerService.findOwnersWithMultipleApartments();
    }

    // Update operations
    public Owner updateOwner(Long id, String firstName, String lastName,
                             String email, String phone) {
        Owner owner = getOwnerById(id);
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setEmail(email);
        owner.setPhone(phone);
        return ownerService.save(owner);
    }

    // Delete operations
    public void deleteOwner(Long id) {
        ownerService.deleteById(id);
    }

    // Business operations
    public void transferOwnership(Long apartmentId, Long oldOwnerId, Long newOwnerId) {
        ownerService.transferOwnership(apartmentId, oldOwnerId, newOwnerId);
    }

    public int getNumberOfOwnedApartments(Long ownerId) {
        return ownerService.getNumberOfOwnedApartments(ownerId);
    }

    public boolean isOwnerOfApartment(Long ownerId, Long apartmentId) {
        return ownerService.isOwnerOfApartment(ownerId, apartmentId);
    }
}
