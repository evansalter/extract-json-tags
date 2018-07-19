# extract-json-tags
Extracts a JSON request object from a GoLang struct type in GoLand

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
