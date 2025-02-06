package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.entity.Apartment;

import java.util.List;

public class ApartmentRepositoryImpl extends GenericRepositoryImpl<Apartment, Long> implements ApartmentRepository {

    public ApartmentRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Apartment.class);
    }

    @Override
    public List<Apartment> findByBuildingId(Long buildingId) {
        TypedQuery<Apartment> query = entityManager.createQuery(
                "SELECT a FROM Apartment a WHERE a.building.id = :buildingId", Apartment.class);
        query.setParameter("buildingId", buildingId);
        return query.getResultList();
    }

    @Override
    public List<Apartment> findByFloorNumber(Integer floorNumber) {
        TypedQuery<Apartment> query = entityManager.createQuery(
                "SELECT a FROM Apartment a WHERE a.floorNumber = :floorNumber", Apartment.class);
        query.setParameter("floorNumber", floorNumber);
        return query.getResultList();
    }

    @Override
    public List<Apartment> findByApartmentNumber(String apartmentNumber) {
        TypedQuery<Apartment> query = entityManager.createQuery(
                "SELECT a FROM Apartment a WHERE a.apartmentNumber = :apartmentNumber", Apartment.class);
        query.setParameter("apartmentNumber", apartmentNumber);
        return query.getResultList();
    }

    @Override
    public List<Apartment> findApartmentsWithUnpaidFees() {
        String jpql = """
            SELECT DISTINCT a FROM Apartment a 
            JOIN a.fees f 
            WHERE f.status = 'PENDING'""";
        return entityManager.createQuery(jpql, Apartment.class).getResultList();
    }

    @Override
    public List<Apartment> findAllSortedByArea() {
        return entityManager.createQuery(
                        "SELECT a FROM Apartment a ORDER BY a.area DESC", Apartment.class)
                .getResultList();
    }
}