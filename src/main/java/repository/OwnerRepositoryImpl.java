package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.entity.Owner;

import java.util.List;

public class OwnerRepositoryImpl extends GenericRepositoryImpl<Owner, Long> implements OwnerRepository {

    public OwnerRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Owner.class);
    }

    @Override
    public List<Owner> findByFirstNameAndLastName(String firstName, String lastName) {
        TypedQuery<Owner> query = entityManager.createQuery(
                "SELECT o FROM Owner o WHERE o.firstName = :firstName AND o.lastName = :lastName",
                Owner.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        return query.getResultList();
    }

    @Override
    public List<Owner> findByEmail(String email) {
        TypedQuery<Owner> query = entityManager.createQuery(
                "SELECT o FROM Owner o WHERE o.email = :email", Owner.class);
        query.setParameter("email", email);
        return query.getResultList();
    }

    @Override
    public List<Owner> findOwnersWithMultipleApartments() {
        String jpql = """
            SELECT o FROM Owner o 
            JOIN o.ownedApartments a 
            GROUP BY o 
            HAVING COUNT(a) > 1""";
        return entityManager.createQuery(jpql, Owner.class).getResultList();
    }

    @Override
    public List<Owner> findByBuildingId(Long buildingId) {
        String jpql = """
            SELECT DISTINCT o FROM Owner o 
            JOIN o.ownedApartments a 
            WHERE a.building.id = :buildingId""";
        TypedQuery<Owner> query = entityManager.createQuery(jpql, Owner.class);
        query.setParameter("buildingId", buildingId);
        return query.getResultList();
    }
}