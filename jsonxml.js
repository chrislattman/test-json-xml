const fs = require("node:fs");
const xmldom = require("@xmldom/xmldom"); // no native Node.js support for DOMParser, document, or XMLSerializer

// JSON

const jsonString = fs.readFileSync("data.json").toString();

const data = JSON.parse(jsonString);
const index = data.index;
const firstName = data.firstName;
const lastName = data.lastName;
const phoneNumbers = data.phoneNumbers;
let phoneTypes = new Array();
let numbers = new Array();
phoneNumbers.forEach((element, _) => {
    phoneTypes.push(element.phoneType);
    numbers.push(element.number);
});

let newdata = {}; // newdata is of type Object, not Map
newdata.index = index; // same as newdata["index"] = index;
newdata.firstName = firstName;
newdata.lastName = lastName;
let newphoneNumbers = []; // [] is the same as new Array()
for (let i = 0; i < phoneNumbers.length; i++) {
    let newphone = {};
    newphone.phoneType = phoneTypes[i];
    newphone.number = numbers[i];
    newphoneNumbers.push(newphone);
}
newdata.phoneNumbers = newphoneNumbers;

if (JSON.stringify(data) === JSON.stringify(newdata)) {
    console.log("Equal JSON!");
} else {
    console.log("Unequal JSON.");
}

// mapping JSON object to class object and class object to JSON string
class Data {
    index;
    firstName;
    lastName;
    phoneNumbers;

    constructor(data) {
        Object.assign(this, data);
    }
}

let obj = new Data(data);
console.log(obj);
console.log(JSON.stringify(obj));

// XML

const xmlString = fs.readFileSync("data.xml").toString();

const parser = new xmldom.DOMParser();
const doc = parser.parseFromString(xmlString, "text/xml");
const xmlindex = doc.getElementsByTagName("index").item(0).textContent;
const xmlfirstName = doc.getElementsByTagName("firstName").item(0).textContent;
const xmllastName = doc.getElementsByTagName("lastName").item(0).textContent;
const xmlphoneNumbers = doc.getElementsByTagName("phoneNumbers").item(0).childNodes;
let xmlphoneTypes = [];
let xmlnumbers = [];
for (let i = 0; i < xmlphoneNumbers.length; i++) {
    const xmlphone = xmlphoneNumbers[i];
    if (xmlphone.nodeType === xmlphone.ELEMENT_NODE) {
        const xmlphoneNumber = xmlphone.childNodes;
        xmlphoneTypes.push(xmlphoneNumber[1].textContent);
        xmlnumbers.push(xmlphoneNumber[3].textContent);
    }
}

const newdoc = new xmldom.DOMImplementation().createDocument("", "", null);
const root = newdoc.createElement("data");
const newxmlindex = newdoc.createElement("index");
newxmlindex.appendChild(newdoc.createTextNode(xmlindex));
root.appendChild(newxmlindex);
const newxmlfirstName = newdoc.createElement("firstName");
newxmlfirstName.appendChild(newdoc.createTextNode(xmlfirstName));
root.appendChild(newxmlfirstName);
const newxmllastName = newdoc.createElement("lastName");
newxmllastName.appendChild(newdoc.createTextNode(xmllastName));
root.appendChild(newxmllastName);
const newxmlphoneNumbers = newdoc.createElement("phoneNumbers");
for (let i = 0; i < xmlphoneTypes.length; i++) {
    const newxmlphoneNumber = newdoc.createElement("phoneNumber");
    const newxmlphoneType = newdoc.createElement("phoneType");
    newxmlphoneType.appendChild(newdoc.createTextNode(xmlphoneTypes[i]));
    newxmlphoneNumber.appendChild(newxmlphoneType);
    const newxmlnumber = newdoc.createElement("number");
    newxmlnumber.appendChild(newdoc.createTextNode(xmlnumbers[i]));
    newxmlphoneNumber.appendChild(newxmlnumber);
    newxmlphoneNumbers.appendChild(newxmlphoneNumber);
}
root.appendChild(newxmlphoneNumbers);
newdoc.appendChild(root);

const serializer = new xmldom.XMLSerializer();

// Remove XML declaration from initial XML string and then all whitespace
const lines = xmlString.split("\n");
let oldxmlString = "";
for (let i = 1; i < lines.length; i++) {
    oldxmlString += lines[i];
}
oldxmlString = oldxmlString.replace(/\s/g, "");

if (oldxmlString === serializer.serializeToString(newdoc)) {
    console.log("Equal XML!");
} else {
    console.log("Unequal XML.");
}
