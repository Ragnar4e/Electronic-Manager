package repository;

import org.example.entity.Company;

import java.util.List;

public interface CompanyRepository extends GenericRepository<Company, Long> {
    List<Company> findByName(String name);
    // Method to get companies sorted by revenue (collected fees)
    List<Company> findAllSortedByRevenue();
}