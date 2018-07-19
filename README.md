# Introduction

`extract-json-tags` is a plugin for GoLand by JetBrains that helps you create JSON request bodies for your Go servers using Go's struct tags.

# Installation

TODO

# Usage

Given a Go `type struct` with JSON tags...

```go
type Person struct {
  FirstName string `json:"first_name"`
  LastName  string `json:"last_name"`
  Age       int    `json:"age"`
}
```

...right-click on `Person` and choose "Extract JSON Tags". The following JSON object will be copied to your clipboard:

```json
{
  "first_name": "",
  "last_name": "",
  "age": ""
}
```

You can now paste this in Postman, or any other application, to build a request using these fields.
