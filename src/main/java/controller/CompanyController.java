package controller;

import org.example.entity.Company;
import service.CompanyService;
import java.util.List;

public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Create operations
    public Company createCompany(String name, String email, String contactNumber, String address) {
        Company company = new Company();
        company.setName(name);
        company.setEmail(email);
        company.setContactNumber(contactNumber);
        company.setAddress(address);
        return companyService.save(company);
    }

    // Read operations
    public Company getCompanyById(Long id) {
        return companyService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with ID: " + id));
    }

    public List<Company> getAllCompanies() {
        return companyService.findAll();
    }

    public List<Company> getCompaniesByName(String name) {
        return companyService.findByName(name);
    }

    public List<Company> getCompaniesSortedByRevenue() {
        return companyService.getAllCompaniesSortedByRevenue();
    }

    // Update operations
    public Company updateCompany(Long id, String name, String email, String contactNumber, String address) {
        Company company = getCompanyById(id);
        company.setName(name);
        company.setEmail(email);
        company.setContactNumber(contactNumber);
        company.setAddress(address);
        return companyService.save(company);
    }

    // Delete operations
    public void deleteCompany(Long id) {
        companyService.deleteById(id);
    }

    // Business operations
    public void reassignEmployeesAfterDismissal(Long dismissedEmployeeId) {
        companyService.reassignEmployeesAfterEmployeeDismissal(dismissedEmployeeId);
    }

    public void distributeNewBuilding(Long buildingId) {
        companyService.distributeNewBuildingToLeastLoadedEmployee(buildingId);
    }
}