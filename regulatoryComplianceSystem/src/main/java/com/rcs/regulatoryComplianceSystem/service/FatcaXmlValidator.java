package com.rcs.regulatoryComplianceSystem.service;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import java.io.StringReader;


@Service
public class FatcaXmlValidator {

    public static boolean validateXml(String xmlData) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(FatcaXmlValidator.class.getClassLoader().getResource("oecdtypes_v4.2.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlData)));
            return true;
        } catch (SAXException | java.io.IOException e) {
            System.out.println("XML Validation Failed: " + e.getMessage());
            return false;
        }
    }
}
