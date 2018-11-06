package com.kis.validator;

import com.kis.view.TreeNodeView;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class XmlValidator {

    private static Logger logger = Logger.getLogger(TreeNodeView.class.getName());

    public static void validate(File file) throws SAXException {

        logger.info("Validating file " + file.getName() + "...");

        URL xsdSchema = XmlValidator.class.getClassLoader().getResource("com/kis/validator/projectValidator.xsd");

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Source xmlFile = new StreamSource(file);

        try {
            Schema schema = schemaFactory.newSchema(xsdSchema);
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }
}
