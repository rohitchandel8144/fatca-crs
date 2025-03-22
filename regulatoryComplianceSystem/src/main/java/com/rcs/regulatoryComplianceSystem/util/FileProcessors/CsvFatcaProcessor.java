package com.rcs.regulatoryComplianceSystem.util.FileProcessors;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.rcs.regulatoryComplianceSystem.entity.fatcaEntity.AccountReport;
import com.rcs.regulatoryComplianceSystem.entity.fatcaEntity.AccountHolder;
import com.rcs.regulatoryComplianceSystem.entity.fatcaEntity.Payment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvFatcaProcessor {

    public static List<AccountReport> processCSV(MultipartFile file) throws Exception {
        List<AccountReport> accounts = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> records = csvReader.readAll();

            if (records.isEmpty()) {
                throw new IllegalArgumentException("CSV file is empty.");
            }

            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);

                if (record.length < 5) {
                    System.err.println("⚠ Skipping row " + i + ": Not enough columns.");
                    continue;
                }

                try {
                    String accountNumber = record[0].trim();
                    String accountHolderName = record[1].trim();
                    String countryCode = record[2].trim();
                    BigDecimal accountBalance = parseBigDecimal(record[3], "AccountBalance", i);
                    BigDecimal paymentAmount = parseBigDecimal(record[4], "PaymentAmount", i);

                    AccountHolder accountHolder = new AccountHolder(accountHolderName, countryCode);
                    Payment payment = new Payment(paymentAmount);
                    AccountReport accountReport = new AccountReport(accountNumber, accountBalance, accountHolder, List.of(payment));

                    accounts.add(accountReport);
                } catch (Exception e) {
                    System.err.println(" Error processing row " + i + ": " + e.getMessage());
                }
            }
        } catch (CsvException e) {
            throw new RuntimeException("Error reading CSV file: " + e.getMessage(), e);
        }
        return accounts;
    }

    // ✅ Method to safely parse BigDecimal values
    private static BigDecimal parseBigDecimal(String value, String fieldName, int rowIndex) {
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(" Invalid number format for '" + fieldName + "' at row " + rowIndex + ": " + value);
        }
    }
}
