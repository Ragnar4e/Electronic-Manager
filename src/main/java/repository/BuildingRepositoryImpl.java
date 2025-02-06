package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.entity.Building;

import java.util.List;

public class BuildingRepositoryImpl extends GenericRepositoryImpl<Building, Long> implements repository.BuildingRepository {

    public BuildingRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Building.class);
    }

    @Override
    public List<Building> findByEmployeeId(Long employeeId) {
        TypedQuery<Building> query = entityManager.createQuery(
                "SELECT b FROM Building b WHERE b.employee.id = :employeeId", Building.class);
        query.setParameter("employeeId", employeeId);
        return query.getResultList();
    }

    @Override
    public List<Building> findByAddress(String address) {
        TypedQuery<Building> query = entityManager.createQuery(
                "SELECT b FROM Building b WHERE b.address LIKE :address", Building.class);
        query.setParameter("address", "%" + address + "%");
        return query.getResultList();
    }

    @Override
    public List<Building> findAllSortedByApartmentCount() {
        String jpql = """
        SELECT b FROM Building b 
        LEFT JOIN b.apartments a 
        GROUP BY b 
        ORDER BY COUNT(a) DESC""";
        return entityManager.createQuery(jpql, Building.class).getResultList();
    }

    @Override
    public List<Building> findBuildingsWithFreeApartments() {
        String jpql = """
            SELECT b FROM Building b 
            LEFT JOIN b.apartments a 
            GROUP BY b 
            HAVING COUNT(a) < b.totalApartments""";
        return entityManager.createQuery(jpql, Building.class).getResultList();
    }
}