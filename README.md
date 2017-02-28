# flatjson

A fast [Json](https://json.org) parser (and builder), written in Java.

![uyubi salt flats, photo CC-BY yoann supertramp ](flat.jpg "https://500px.com/photo/172664473/")

### Features

* **memory-efficient** &mdash; flatjson does not build a parse tree, but just an in-memory index ("overlay"). json nodes are constructed on demand, when you first access them.
* **easy to use** &mdash; super simple api, inspired by [minimal-json](https://github.com/ralfstx/minimal-json)
* **clean** &mdash; source code is very readable and fully unit-tested.
* **fast** &mdash; like a bat out of hell!


### Usage

let's parse a json string:

```
Json json = Json.parse("[42, true, \"hello\"]");
```
this call returns a `Json` object. we can check what json entity it represents:

```
System.out.println(json.isNumber()); // false
System.out.println(json.isObject()); // false
System.out.println(json.isArray()); // true
```
aha, it's an array &mdash; so let's use it!

```
List<Json> array = json.asArray();
```
json arrays are represented by lists of `Json` objects.

```
System.out.println(array.size()); // --> 3
System.out.println(array.get(0).asLong()); // --> 42
System.out.println(array.get(1).asBoolean()); // --> true
System.out.println(array.get(2).asString()); // --> "hello"
```

TODO: describe builder methods

### License

[MIT](LICENSE.txt)



