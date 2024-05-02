import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonXml {
    public static void main(String[] args) throws IOException {
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
            System.out.println("Equal!");
        } else {
            System.out.println("Unequal.");
        }

        // mapping JSON string to class object and vice versa
        ObjectMapper mapper = new ObjectMapper();
        Data obj = mapper.readValue(jsonString, Data.class);
        System.out.println(obj);
        System.out.println(mapper.writeValueAsString(obj));
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
