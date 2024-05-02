package main

import (
	"encoding/json"
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

func main() {
	jsondata, err := os.ReadFile("data.json")
	if err != nil {
		log.Fatal(err)
	}

	var d data
	json.Unmarshal(jsondata, &d)

	newdata, _ := json.Marshal(d)

	oldjsonstring := string(jsondata)
	oldjsonstring = strings.ReplaceAll(oldjsonstring, " ", "")
	oldjsonstring = strings.ReplaceAll(oldjsonstring, "\n", "")
	oldjsonstring = strings.ReplaceAll(oldjsonstring, "\t", "") // just in case

	// need to make lowercase due to Go variable naming conventions requiring
	// first letter to be capitalized
	oldjsonstring = strings.ToLower(oldjsonstring)
	newjsonstring := strings.ToLower(string(newdata))

	if oldjsonstring == newjsonstring {
		fmt.Println("Equal!")
	} else {
		fmt.Println("Unequal.")
	}
	fmt.Println("Go already maps JSON bytes to a struct")
}
