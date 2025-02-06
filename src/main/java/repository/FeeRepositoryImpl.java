package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.entity.Fee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FeeRepositoryImpl extends GenericRepositoryImpl<Fee, Long> implements FeeRepository {

    public FeeRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Fee.class);
    }

    @Override
    public List<Fee> findByApartmentId(Long apartmentId) {
        TypedQuery<Fee> query = entityManager.createQuery(
                "SELECT f FROM Fee f WHERE f.apartment.id = :apartmentId", Fee.class);
        query.setParameter("apartmentId", apartmentId);
        return query.getResultList();
    }

    @Override
    public List<Fee> findByStatus(Fee.FeeStatus status) {
        TypedQuery<Fee> query = entityManager.createQuery(
                "SELECT f FROM Fee f WHERE f.status = :status", Fee.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<Fee> findByDueDateBefore(LocalDate date) {
        TypedQuery<Fee> query = entityManager.createQuery(
                "SELECT f FROM Fee f WHERE f.dueDate < :date", Fee.class);
        query.setParameter("date", date);
        return query.getResultList();
    }

    @Override
    public List<Fee> findByBuildingId(Long buildingId) {
        String jpql = """
            SELECT f FROM Fee f 
            WHERE f.apartment.building.id = :buildingId""";
        TypedQuery<Fee> query = entityManager.createQuery(jpql, Fee.class);
        query.setParameter("buildingId", buildingId);
        return query.getResultList();
    }

    @Override
    public BigDecimal getTotalFeesByBuildingId(Long buildingId) {
        String jpql = """
            SELECT COALESCE(SUM(f.amount), 0) FROM Fee f 
            WHERE f.apartment.building.id = :buildingId 
            AND f.status = 'PAID'""";
        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class);
        query.setParameter("buildingId", buildingId);
        return query.getSingleResult();
    }

    @Override
    public List<Fee> findOverdueFees() {
        String jpql = """
            SELECT f FROM Fee f 
            WHERE f.status = 'PENDING' 
            AND f.dueDate < :currentDate""";
        TypedQuery<Fee> query = entityManager.createQuery(jpql, Fee.class);
        query.setParameter("currentDate", LocalDate.now());
        return query.getResultList();
    }
}