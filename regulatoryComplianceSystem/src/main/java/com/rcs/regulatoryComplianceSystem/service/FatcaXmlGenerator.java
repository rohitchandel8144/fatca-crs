package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.entity.fatcaEntity.FatcaReport;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.springframework.stereotype.Service;

import java.io.StringWriter;


@Service
public class FatcaXmlGenerator {

    public  String generateFatcaXml(FatcaReport fatcaReport) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(FatcaReport.class);
        Marshaller marshaller = jaxbContext.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter xmlWriter = new StringWriter();
        marshaller.marshal(fatcaReport, xmlWriter);

        return xmlWriter.toString();
    }
}
