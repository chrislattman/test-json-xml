const fs = require("node:fs");

const jsonstring = fs.readFileSync("data.json").toString();

const data = JSON.parse(jsonstring);
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
    console.log("Equal!");
} else {
    console.log("Unequal.");
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
