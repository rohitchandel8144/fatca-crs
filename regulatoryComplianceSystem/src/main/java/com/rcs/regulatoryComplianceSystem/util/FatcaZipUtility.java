package com.rcs.regulatoryComplianceSystem.util;

import java.io.FileOutputStream;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FatcaZipUtility {
    public static void zipFile(String filePath, String zipFilePath) throws Exception {
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOut.putNextEntry(zipEntry);
            zipOut.write(java.nio.file.Files.readAllBytes(file.toPath()));
            zipOut.closeEntry();
        }
    }
}

