import json
from typing import List
import xml.etree.ElementTree as ET

from pydantic import BaseModel

# JSON

jsonString = open("data.json").read()

data = json.loads(jsonString)
index = data["index"]
firstName = data["firstName"]
lastName = data["lastName"]
phoneNumbers = data["phoneNumbers"]
phoneTypes = []
numbers = []
for phone in phoneNumbers:
    phoneTypes.append(phone["phoneType"])
    numbers.append(phone["number"])

newdata = {}
newdata["index"] = index
newdata["firstName"] = firstName
newdata["lastName"] = lastName
newphoneNumbers = []
for i in range(len(phoneNumbers)):
    newphone = {}
    newphone["phoneType"] = phoneTypes[i]
    newphone["number"] = numbers[i]
    newphoneNumbers.append(newphone)
newdata["phoneNumbers"] = newphoneNumbers

# Note: JSON requires double-quotes ("") for strings
if json.dumps(data) == json.dumps(newdata):
    print("Equal JSON!")
else:
    print("Unequal JSON.")

# mapping JSON string to class object and vice versa
class PhoneNumber(BaseModel):
    phoneType: str
    number: str

class Data(BaseModel):
    index: int
    firstName: str
    lastName: str
    phoneNumbers: List[PhoneNumber]

obj = Data.model_validate_json(jsonString)
print(obj)
print(obj.model_dump_json())

# XML

xmlString = open("data.xml").read()

root = ET.fromstring(xmlString)
xmlindex = root[0].text
xmlfirstName = root[1].text
xmllastName = root[2].text
xmlphoneTypes = []
xmlnumbers = []
for child in root[3]:
    xmlphoneTypes.append(child[0].text)
    xmlnumbers.append(child[1].text)

newroot = ET.Element("data")
ET.SubElement(newroot, "index").text = xmlindex
ET.SubElement(newroot, "firstName").text = xmlfirstName
ET.SubElement(newroot, "lastName").text = xmllastName
newxmlphoneNumbers = ET.SubElement(newroot, "phoneNumbers")
for i in range(len(root[3])):
    newxmlphoneNumber = ET.SubElement(newxmlphoneNumbers, "phoneNumber")
    ET.SubElement(newxmlphoneNumber, "phoneType").text = xmlphoneTypes[i]
    ET.SubElement(newxmlphoneNumber, "number").text = xmlnumbers[i]

# Remove XML declaration from initial XML string and remove all whitespace
oldxmlstring = "".join(xmlString.splitlines()[1:]).replace(" ", "").replace("\t", "")

if oldxmlstring == ET.tostring(newroot).decode():
    print("Equal XML!")
else:
    print("Unequal XML.")
