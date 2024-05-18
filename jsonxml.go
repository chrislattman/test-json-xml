package main

import (
	"encoding/json"
	"encoding/xml"
	"fmt"
	"log"
	"os"
	"strings"
)

type PhoneNumber struct {
	PhoneType string `json:"phoneType"`
	Number    string `json:"number"`
}

type Data struct {
	Index        float64       `json:"index"`
	FirstName    string        `json:"firstName"`
	LastName     string        `json:"lastName"`
	PhoneNumbers []PhoneNumber `json:"phoneNumbers"`
}

type PhoneNumberXML struct {
	XMLName   xml.Name `xml:"phoneNumber"`
	PhoneType string   `xml:"phoneType"`
	Number    string   `xml:"number"`
}

type PhoneNumbersXML struct {
	XMLName     xml.Name         `xml:"phoneNumbers"`
	PhoneNumber []PhoneNumberXML `xml:"phoneNumber"`
}

type DataXML struct {
	XMLName      xml.Name        `xml:"data"`
	Index        int             `xml:"index"`
	FirstName    string          `xml:"firstName"`
	LastName     string          `xml:"lastName"`
	PhoneNumbers PhoneNumbersXML `xml:"phoneNumbers"`
}

func main() {
	// JSON

	jsonbytes, err := os.ReadFile("data.json")
	if err != nil {
		log.Fatal(err)
	}

	var data map[string]interface{}
	json.Unmarshal(jsonbytes, &data)
	index := data["index"].(float64)
	firstName := data["firstName"].(string)
	lastName := data["lastName"].(string)
	phoneNumbers := data["phoneNumbers"].([]interface{})
	var phoneTypes, numbers []string
	for i := 0; i < len(phoneNumbers); i++ {
		phone := phoneNumbers[i].(map[string]interface{})
		phoneTypes = append(phoneTypes, phone["phoneType"].(string))
		numbers = append(numbers, phone["number"].(string))
	}

	var newdata Data
	newdata.Index = index
	newdata.FirstName = firstName
	newdata.LastName = lastName
	for i := 0; i < len(phoneNumbers); i++ {
		var newphone PhoneNumber
		newphone.PhoneType = phoneTypes[i]
		newphone.Number = numbers[i]
		newdata.PhoneNumbers = append(newdata.PhoneNumbers, newphone)
	}
	// While these byte slices may not be exactly identical, they
	// represent the same JS object and are therefore semantically equal
	// (this is due to the fields in data.json not being in alphabetical order)
	// newdatabytes, _ := json.Marshal(newdata)
	// databytes, _ := json.Marshal(data)

	// mapping JSON string to struct and vice versa
	var j Data
	json.Unmarshal(jsonbytes, &j)

	serialized, _ := json.Marshal(j)

	oldjsonstring := string(jsonbytes)
	oldjsonstring = strings.ReplaceAll(oldjsonstring, " ", "")
	oldjsonstring = strings.ReplaceAll(oldjsonstring, "\n", "")
	oldjsonstring = strings.ReplaceAll(oldjsonstring, "\t", "") // just in case

	// need to make lowercase due to Go variable naming conventions requiring
	// first letter to be capitalized
	oldjsonstring = strings.ToLower(oldjsonstring)
	newjsonstring := strings.ToLower(string(serialized))

	if oldjsonstring == newjsonstring {
		fmt.Println("Equal JSON!")
	} else {
		fmt.Println("Unequal JSON.")
	}

	// XML

	xmldata, err := os.ReadFile("data.xml")
	if err != nil {
		log.Fatal(err)
	}

	var x DataXML
	xml.Unmarshal(xmldata, &x)

	newxmldata, _ := xml.Marshal(x)

	// Remove XML declaration from initial XML string and then all whitespace
	oldxmlstring := string(xmldata)
	lines := strings.Split(oldxmlstring, "\n")
	oldxmlstring = strings.Join(lines[1:], "")
	oldxmlstring = strings.ReplaceAll(oldxmlstring, " ", "")
	oldxmlstring = strings.ReplaceAll(oldxmlstring, "\n", "")
	oldxmlstring = strings.ReplaceAll(oldxmlstring, "\t", "") // just in case

	if oldxmlstring == string(newxmldata) {
		fmt.Println("Equal XML!")
	} else {
		fmt.Println("Unequal XML.")
	}
}
