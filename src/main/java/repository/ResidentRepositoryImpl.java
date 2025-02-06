package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.entity.Resident;

import java.util.List;

public class ResidentRepositoryImpl extends GenericRepositoryImpl<Resident, Long> implements ResidentRepository {

    public ResidentRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Resident.class);
    }

    @Override
    public List<Resident> findByApartmentId(Long apartmentId) {
        TypedQuery<Resident> query = entityManager.createQuery(
                "SELECT r FROM Resident r WHERE r.apartment.id = :apartmentId", Resident.class);
        query.setParameter("apartmentId", apartmentId);
        return query.getResultList();
    }

    @Override
    public List<Resident> findByBuildingId(Long buildingId) {
        String jpql = """
            SELECT r FROM Resident r 
            WHERE r.apartment.building.id = :buildingId""";
        TypedQuery<Resident> query = entityManager.createQuery(jpql, Resident.class);
        query.setParameter("buildingId", buildingId);
        return query.getResultList();
    }

    @Override
    public List<Resident> findByAgeGreaterThan(Integer age) {
        TypedQuery<Resident> query = entityManager.createQuery(
                "SELECT r FROM Resident r WHERE r.age > :age", Resident.class);
        query.setParameter("age", age);
        return query.getResultList();
    }

    @Override
    public List<Resident> findResidentsWithPets() {
        return entityManager.createQuery(
                        "SELECT r FROM Resident r WHERE r.hasPet = true", Resident.class)
                .getResultList();
    }

    @Override
    public List<Resident> findResidentsUsingElevator() {
        return entityManager.createQuery(
                        "SELECT r FROM Resident r WHERE r.usesElevator = true", Resident.class)
                .getResultList();
    }
}