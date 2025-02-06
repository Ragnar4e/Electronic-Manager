package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.entity.Company;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CompanyRepositoryImpl extends GenericRepositoryImpl<Company, Long> implements CompanyRepository {

    public CompanyRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Company.class);
    }

    @Override
    public List<Company> findByName(String name) {
        TypedQuery<Company> query = entityManager.createQuery(
                "SELECT c FROM Company c WHERE c.name = :name", Company.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public List<Company> findAllSortedByRevenue() {
        String jpql = """
        SELECT c, COALESCE(SUM(CASE WHEN f.status = 'PAID' THEN f.amount ELSE 0 END), 0) as revenue 
        FROM Company c 
        LEFT JOIN c.employees e 
        LEFT JOIN e.managedBuildings b 
        LEFT JOIN b.apartments a 
        LEFT JOIN a.fees f 
        GROUP BY c 
        ORDER BY revenue DESC""";

        List<Object[]> results = entityManager.createQuery(jpql).getResultList();
        List<Company> companies = new ArrayList<>();

        for (Object[] result : results) {
            Company company = (Company) result[0];
            BigDecimal revenue = (BigDecimal) result[1];
            // Store revenue in a transient field or print it
            System.out.printf("Company: %s, Revenue: %.2f%n", company.getName(), revenue);
            companies.add(company);
        }

        return companies;
    }
}