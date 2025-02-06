package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.entity.Employee;

import java.util.List;

public class EmployeeRepositoryImpl extends GenericRepositoryImpl<Employee, Long> implements repository.EmployeeRepository {

    public EmployeeRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Employee.class);
    }

    @Override
    public List<Employee> findByCompanyId(Long companyId) {
        TypedQuery<Employee> query = entityManager.createQuery(
                "SELECT e FROM Employee e WHERE e.company.id = :companyId", Employee.class);
        query.setParameter("companyId", companyId);
        return query.getResultList();
    }

    @Override
    public List<Employee> findAllSortedByManagedBuildingsCount() {
        String jpql = """
        SELECT e FROM Employee e 
        LEFT JOIN e.managedBuildings b 
        GROUP BY e 
        ORDER BY COUNT(b) DESC""";
        return entityManager.createQuery(jpql, Employee.class).getResultList();
    }

    @Override
    public Employee findEmployeeWithLeastBuildings() {
        String jpql = """
            SELECT e FROM Employee e 
            LEFT JOIN e.managedBuildings b 
            GROUP BY e 
            ORDER BY COUNT(b) ASC""";
        TypedQuery<Employee> query = entityManager.createQuery(jpql, Employee.class);
        query.setMaxResults(1);
        List<Employee> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}