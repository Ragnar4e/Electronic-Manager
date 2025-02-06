package service;

import org.example.entity.Company;
import java.util.List;

public interface CompanyService extends GenericService<Company, Long> {
    List<Company> findByName(String name);
    List<Company> getAllCompaniesSortedByRevenue();
    void reassignEmployeesAfterEmployeeDismissal(Long dismissedEmployeeId);
    void distributeNewBuildingToLeastLoadedEmployee(Long buildingId);
}