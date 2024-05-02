# JSON and XML decoding and encoding examples

JSON (JavaScript Object Notation) and XML (eXtensible Markup Language) are two widely used formats for storing and transmitting data.

- Despite the name, JSON is not only for JavaScript; it's used as a common API format
- XML is extensible in the way you can define tags (unlike HTML which has predefined tags)
- Other well-known formats include YAML and TOML; however they're mostly used in configuration files

While both are used for the same purpose, there are key differences:

- JSON stores data in a map structure, whereas XML stores data in a tree structure
- JSON is easier to read and parse
- JSON supports just numbers, strings, booleans, and arrays, whereas XML supports many more data types
    - In XML instead of arrays, you have sublists
- JSON takes up less space than XML
- JSON doesn't support comments, but XML does (`<!-- -->`)

This repository demonstrates JSON and XML decoding and encoding in Java, Python, Node.js, and Go. Use the provided Makefile to run the examples.
