import json
from typing import List
import xml.etree.ElementTree as ET

from pydantic import BaseModel

# JSON

json_string = open("data.json").read()

data = json.loads(json_string)
index = data["index"]
first_name = data["firstName"]
last_name = data["lastName"]
phone_numbers = data["phoneNumbers"]
phone_types = []
numbers = []
for phone in phone_numbers:
    phone_types.append(phone["phoneType"])
    numbers.append(phone["number"])

new_data = {}
new_data["index"] = index
new_data["firstName"] = first_name
new_data["lastName"] = last_name
new_phone_numbers = []
for i in range(len(phone_numbers)):
    new_phone = {}
    new_phone["phoneType"] = phone_types[i]
    new_phone["number"] = numbers[i]
    new_phone_numbers.append(new_phone)
new_data["phoneNumbers"] = new_phone_numbers

# Note: JSON requires double-quotes ("") for strings
if json.dumps(data) == json.dumps(new_data):
    print("Equal JSON!")
else:
    print("Unequal JSON.")

# mapping JSON string to class object and vice versa
class PhoneNumber(BaseModel):
    phone_type: str
    number: str

class Data(BaseModel):
    index: int
    first_name: str
    last_name: str
    phone_numbers: List[PhoneNumber]

obj = Data.model_validate_json(json_string)
print(obj)
print(obj.model_dump_json())

# XML

xml_string = open("data.xml").read()

root = ET.fromstring(xml_string)
xml_index = root[0].text
xml_first_name = root[1].text
xml_last_name = root[2].text
xml_phone_types = []
xml_numbers = []
for child in root[3]:
    xml_phone_types.append(child[0].text)
    xml_numbers.append(child[1].text)

new_root = ET.Element("data")
ET.SubElement(new_root, "index").text = xml_index
ET.SubElement(new_root, "firstName").text = xml_first_name
ET.SubElement(new_root, "lastName").text = xml_last_name
new_xml_phone_numbers = ET.SubElement(new_root, "phoneNumbers")
for i in range(len(root[3])):
    new_xml_phone_number = ET.SubElement(new_xml_phone_numbers, "phoneNumber")
    ET.SubElement(new_xml_phone_number, "phoneType").text = xml_phone_types[i]
    ET.SubElement(new_xml_phone_number, "number").text = xml_numbers[i]

# Remove XML declaration from initial XML string and remove all whitespace
old_xml_string = "".join(xml_string.splitlines()[1:]).replace(" ", "").replace("\t", "")

if old_xml_string == ET.tostring(new_root).decode():
    print("Equal XML!")
else:
    print("Unequal XML.")
