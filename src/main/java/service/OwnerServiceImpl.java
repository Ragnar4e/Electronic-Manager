package service;

import jakarta.persistence.EntityManager;
import org.example.entity.Owner;
import org.example.entity.Apartment;
import repository.OwnerRepository;
import repository.ApartmentRepository;
import java.util.List;
import java.util.Optional;

public class OwnerServiceImpl implements OwnerService {
    private final EntityManager entityManager;
    private final OwnerRepository ownerRepository;
    private final ApartmentRepository apartmentRepository;

    public OwnerServiceImpl(EntityManager entityManager,
                            OwnerRepository ownerRepository,
                            ApartmentRepository apartmentRepository) {
        this.entityManager = entityManager;
        this.ownerRepository = ownerRepository;
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Override
    public Optional<Owner> findById(Long id) {
        return ownerRepository.findById(id);
    }

    @Override
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    @Override
    public void delete(Owner owner) {
        ownerRepository.delete(owner);
    }

    @Override
    public void deleteById(Long id) {
        ownerRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return ownerRepository.existsById(id);
    }

    @Override
    public List<Owner> findByFirstNameAndLastName(String firstName, String lastName) {
        return ownerRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public List<Owner> findByEmail(String email) {
        return ownerRepository.findByEmail(email);
    }

    @Override
    public List<Owner> findOwnersWithMultipleApartments() {
        return ownerRepository.findOwnersWithMultipleApartments();
    }

    @Override
    public List<Owner> findByBuildingId(Long buildingId) {
        return ownerRepository.findByBuildingId(buildingId);
    }

    @Override
    public void transferOwnership(Long apartmentId, Long oldOwnerId, Long newOwnerId) {
        Owner oldOwner = ownerRepository.findById(oldOwnerId)
                .orElseThrow(() -> new IllegalArgumentException("Old owner not found"));
        Owner newOwner = ownerRepository.findById(newOwnerId)
                .orElseThrow(() -> new IllegalArgumentException("New owner not found"));
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new IllegalArgumentException("Apartment not found"));

        // Проверка дали старият собственик наистина притежава апартамента
        if (!oldOwner.getOwnedApartments().contains(apartment)) {
            throw new IllegalStateException("Old owner does not own this apartment");
        }

        // Проверка дали новият собственик вече не притежава апартамента
        if (newOwner.getOwnedApartments().contains(apartment)) {
            throw new IllegalStateException("New owner already owns this apartment");
        }

        oldOwner.getOwnedApartments().remove(apartment);
        newOwner.getOwnedApartments().add(apartment);

        ownerRepository.save(oldOwner);
        ownerRepository.save(newOwner);
    }

    @Override
    public int getNumberOfOwnedApartments(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        return owner.getOwnedApartments().size();
    }

    @Override
    public boolean isOwnerOfApartment(Long ownerId, Long apartmentId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        return owner.getOwnedApartments().stream()
                .anyMatch(apt -> apt.getId().equals(apartmentId));
    }
}
