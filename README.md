View on JetBrains: [![](https://img.shields.io/jetbrains/plugin/d/10943-extract-json-tags.svg)](https://plugins.jetbrains.com/plugin/10943-extract-json-tags)

# Introduction

`extract-json-tags` is a plugin for GoLand by JetBrains that helps you create JSON request bodies for your Go servers using Go's struct tags.

# Installation

Download the plugin from the [JetBrains Plugin Repository](https://plugins.jetbrains.com/plugin/10943-extract-json-tags) or search for "Extract JSON Tags" GoLand's plugin settings.

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

```js
{
    "first_name": "",
    "last_name": "",
    "age": ""
}
```

You can now paste this in Postman, or any other application, to build a request using these fields.
