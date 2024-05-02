import json
from typing import List

from pydantic import BaseModel

jsonstring = open("data.json").read()

data = json.loads(jsonstring)
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
    print("Equal!")
else:
    print("Unequal.")

# mapping JSON string to class object and vice versa
class PhoneNumber(BaseModel):
    phoneType: str
    number: str

class Data(BaseModel):
    index: int
    firstName: str
    lastName: str
    phoneNumbers: List[PhoneNumber]

obj = Data.model_validate_json(jsonstring)
print(obj)
print(obj.model_dump_json())
