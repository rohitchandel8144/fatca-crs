package com.rcs.regulatoryComplianceSystem.util.FileProcessors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.rcs.regulatoryComplianceSystem.entity.fatcaEntity.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfFatcaProcessor {

    public static List<AccountReport> processPDF(MultipartFile file) throws Exception {
        List<AccountReport> accounts = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream(); PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            // ✅ Extract accounts from PDF text
            accounts = parsePdfTextToAccounts(text);
        }
        return accounts;
    }

    private static List<AccountReport> parsePdfTextToAccounts(String text) {
        List<AccountReport> accounts = new ArrayList<>();
        String[] lines = text.split("\n");

        for (String line : lines) {
            String[] record = line.split("\\s+");

            if (record.length < 5) continue; // Ignore invalid rows

            String accountNumber = record[0];
            String accountHolderName = record[1];
            String countryCode = record[2];
            BigDecimal accountBalance = new BigDecimal(record[3]);
            BigDecimal paymentAmount = new BigDecimal(record[4]);

            AccountHolder accountHolder = new AccountHolder(accountHolderName, countryCode);
            Payment payment = new Payment(paymentAmount);

            AccountReport accountReport = new AccountReport(accountNumber, accountBalance, accountHolder, List.of(payment));
            accounts.add(accountReport);
        }
        return accounts;
    }
}
