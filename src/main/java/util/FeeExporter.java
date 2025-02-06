package util;

import org.example.entity.Fee;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FeeExporter {
    private static final String FILE_PATH = "fee_records.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void exportPaidFees(List<Fee> fees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            // Write header if file is empty
            if (fees.size() > 0) {
                writer.println("Payment Date,Company,Employee,Building,Apartment,Amount,Due Date,Status");
            }

            // Write data
            for (Fee fee : fees) {
                String company = fee.getApartment().getBuilding().getEmployee().getCompany().getName();
                String employee = fee.getApartment().getBuilding().getEmployee().getFirstName() + " "
                        + fee.getApartment().getBuilding().getEmployee().getLastName();
                String building = fee.getApartment().getBuilding().getAddress();
                String apartment = fee.getApartment().getApartmentNumber();

                writer.printf("%s,%s,%s,%s,%s,%.2f,%s,%s%n",
                        fee.getPaymentDate().format(DATE_FORMATTER),
                        company,
                        employee,
                        building,
                        apartment,
                        fee.getAmount(),
                        fee.getDueDate().format(DATE_FORMATTER),
                        fee.getStatus()
                );
            }
            System.out.println("Fee data exported successfully to " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error exporting fee data: " + e.getMessage());
        }
    }
}