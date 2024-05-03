package main

import (
	"encoding/json"
	"encoding/xml"
	"fmt"
	"log"
	"os"
	"strings"
)

type phoneNumber struct {
	PhoneType string
	Number    string
}

type data struct {
	Index        int
	FirstName    string
	LastName     string
	PhoneNumbers []phoneNumber
}

type phoneNumberXML struct {
	XMLName   xml.Name `xml:"phoneNumber"`
	PhoneType string   `xml:"phoneType"`
	Number    string   `xml:"number"`
}

type phoneNumbersXML struct {
	XMLName     xml.Name         `xml:"phoneNumbers"`
	PhoneNumber []phoneNumberXML `xml:"phoneNumber"`
}

type dataXML struct {
	XMLName      xml.Name        `xml:"data"`
	Index        int             `xml:"index"`
	FirstName    string          `xml:"firstName"`
	LastName     string          `xml:"lastName"`
	PhoneNumbers phoneNumbersXML `xml:"phoneNumbers"`
}

func main() {
	// JSON

	jsondata, err := os.ReadFile("data.json")
	if err != nil {
		log.Fatal(err)
	}

	var j data
	json.Unmarshal(jsondata, &j)

	newdata, _ := json.Marshal(j)

	oldjsonstring := string(jsondata)
	oldjsonstring = strings.ReplaceAll(oldjsonstring, " ", "")
	oldjsonstring = strings.ReplaceAll(oldjsonstring, "\n", "")
	oldjsonstring = strings.ReplaceAll(oldjsonstring, "\t", "") // just in case

	// need to make lowercase due to Go variable naming conventions requiring
	// first letter to be capitalized
	oldjsonstring = strings.ToLower(oldjsonstring)
	newjsonstring := strings.ToLower(string(newdata))

	if oldjsonstring == newjsonstring {
		fmt.Println("Equal JSON!")
	} else {
		fmt.Println("Unequal JSON.")
	}
	fmt.Println("Go already maps JSON bytes to a struct")

	// XML

	xmldata, err := os.ReadFile("data.xml")
	if err != nil {
		log.Fatal(err)
	}

	var x dataXML
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
