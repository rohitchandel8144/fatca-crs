package com.rcs.regulatoryComplianceSystem.util.FileProcessors;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.rcs.regulatoryComplianceSystem.entity.fatcaEntity.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelFatcaProcessor {

    public static List<AccountReport> processExcel(MultipartFile file) throws Exception {
        List<AccountReport> accounts = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                String accountNumber = getCellValue(row.getCell(0));
                String accountHolderName = getCellValue(row.getCell(1));
                String countryCode = getCellValue(row.getCell(2));
                BigDecimal accountBalance = new BigDecimal(getCellValue(row.getCell(3)));
                BigDecimal paymentAmount = new BigDecimal(getCellValue(row.getCell(4)));

                AccountHolder accountHolder = new AccountHolder(accountHolderName, countryCode);
                Payment payment = new Payment(paymentAmount);

                AccountReport accountReport = new AccountReport(accountNumber, accountBalance, accountHolder, List.of(payment));
                accounts.add(accountReport);
            }
        }
        return accounts;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf(cell.getNumericCellValue());
        return "";
    }
}
