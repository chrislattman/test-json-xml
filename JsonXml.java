import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonXml {
    public static void main(String[] args) throws IOException,
        ParserConfigurationException, SAXException,
        TransformerFactoryConfigurationError, TransformerException {
        // JSON

        String jsonString = new String(Files.readAllBytes(Paths.get("data.json")));

        JSONObject data = new JSONObject(jsonString);
        Object index = data.get("index"); // could use data.getInt() or type cast
        Object firstName = data.get("firstName"); // could use data.getString() or type cast
        Object lastName = data.get("lastName");
        JSONArray phoneNumbers = data.getJSONArray("phoneNumbers");
        ArrayList<Object> phoneTypes = new ArrayList<>();
        ArrayList<Object> numbers = new ArrayList<>();
        for (int i = 0; i < phoneNumbers.length(); i++) {
            JSONObject phone = phoneNumbers.getJSONObject(i);
            phoneTypes.add(phone.get("phoneType"));
            numbers.add(phone.get("number"));
        }

        JSONObject newdata = new JSONObject();
        newdata.put("index", index);
        newdata.put("firstName", firstName);
        newdata.put("lastName", lastName);
        JSONArray newphoneNumbers = new JSONArray();
        for (int i = 0; i < phoneNumbers.length(); i++) {
            JSONObject newphone = new JSONObject();
            newphone.put("phoneType", phoneTypes.get(i));
            newphone.put("number", numbers.get(i));
            newphoneNumbers.put(newphone);
        }
        newdata.put("phoneNumbers", newphoneNumbers);

        if (data.toString().equals(newdata.toString())) {
            System.out.println("Equal JSON!");
        } else {
            System.out.println("Unequal JSON.");
        }

        // mapping JSON string to class object and vice versa
        ObjectMapper mapper = new ObjectMapper();
        Data obj = mapper.readValue(jsonString, Data.class);
        System.out.println(obj);
        System.out.println(mapper.writeValueAsString(obj));

        // XML

        String xmlString = new String(Files.readAllBytes(Paths.get("data.xml")));

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
        String xmlindex = doc.getElementsByTagName("index").item(0).getTextContent();
        String xmlfirstName = doc.getElementsByTagName("firstName").item(0).getTextContent();
        String xmllastName = doc.getElementsByTagName("lastName").item(0).getTextContent();
        NodeList xmlphoneNumbers = doc.getElementsByTagName("phoneNumbers").item(0).getChildNodes();
        ArrayList<String> xmlphoneTypes = new ArrayList<>();
        ArrayList<String> xmlnumbers = new ArrayList<>();
        for (int i = 0; i < xmlphoneNumbers.getLength(); i++) {
            Node xmlphone = xmlphoneNumbers.item(i);
            if (xmlphone.getNodeType() == Node.ELEMENT_NODE) {
                NodeList xmlphoneNumber = xmlphone.getChildNodes();
                xmlphoneTypes.add(xmlphoneNumber.item(1).getTextContent());
                xmlnumbers.add(xmlphoneNumber.item(3).getTextContent());
            }
        }

        Document newdoc = builder.newDocument();
        Element root = newdoc.createElement("data");
        Element newxmlindex = newdoc.createElement("index");
        newxmlindex.appendChild(newdoc.createTextNode(xmlindex));
        root.appendChild(newxmlindex);
        Element newxmlfirstName = newdoc.createElement("firstName");
        newxmlfirstName.appendChild(newdoc.createTextNode(xmlfirstName));
        root.appendChild(newxmlfirstName);
        Element newxmllastName = newdoc.createElement("lastName");
        newxmllastName.appendChild(newdoc.createTextNode(xmllastName));
        root.appendChild(newxmllastName);
        Element newxmlphoneNumbers = newdoc.createElement("phoneNumbers");
        for (int i = 0; i < xmlphoneTypes.size(); i++) {
            Element newxmlphoneNumber = newdoc.createElement("phoneNumber");
            Element newxmlphoneType = newdoc.createElement("phoneType");
            newxmlphoneType.appendChild(newdoc.createTextNode(xmlphoneTypes.get(i)));
            newxmlphoneNumber.appendChild(newxmlphoneType);
            Element newxmlnumber = newdoc.createElement("number");
            newxmlnumber.appendChild(newdoc.createTextNode(xmlnumbers.get(i)));
            newxmlphoneNumber.appendChild(newxmlnumber);
            newxmlphoneNumbers.appendChild(newxmlphoneNumber);
        }
        root.appendChild(newxmlphoneNumbers);
        newdoc.appendChild(root);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource source = new DOMSource(newdoc);
        StringWriter writer = new StringWriter();
        transformer.transform(source, new StreamResult(writer));

        // Remove XML declaration from initial XML string and then all whitespace
        String[] lines = xmlString.split("\n");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < lines.length; i++) {
            sb.append(lines[i]);
        }
        String oldxmlString = sb.toString().replaceAll("\\s+", "");

        if (oldxmlString.equals(writer.toString())) {
            System.out.println("Equal XML!");
        } else {
            System.out.println("Unequal XML.");
        }

        // TODO: mapping XML string to class object and vice versa
    }
}

class PhoneNumber {
    private String phoneType;
    private String number;

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PhoneNumber(phoneType='");
        sb.append(phoneType);
        sb.append("', number='");
        sb.append(number);
        sb.append("')");
        return sb.toString();
    }
}

class Data {
    private int index;
    private String firstName;
    private String lastName;
    private PhoneNumber[] phoneNumbers;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PhoneNumber[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(PhoneNumber[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("index=");
        sb.append(index);
        sb.append(" firstName='");
        sb.append(firstName);
        sb.append("' lastName='");
        sb.append(lastName);
        sb.append("' phoneNumbers=[");
        for (int i = 0; i < phoneNumbers.length; i++) {
            sb.append(phoneNumbers[i].toString());
            if (i != phoneNumbers.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
