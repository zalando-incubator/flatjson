# flatjson

A fast [json](https://json.org) parser (and builder), written in java.

![uyubi salt flats, photo CC-BY yoann supertramp ](flat.jpg "https://500px.com/photo/172664473/")

### Features

* **memory-efficient** &mdash; allocates as few objects as possible
* **easy to use** &mdash; simple api, inspired by [minimal-json](https://github.com/ralfstx/minimal-json)
* **fast** &mdash; like a bat out of hell!


### Performance

the following chart shows benchmark results for parsing a 72K sample file on my macbook pro (2,7 ghz intel core i5).

![benchmark chart](chart_parse.png)

flatjson outperforms some popular json parsers (gson, jackson) by 2x to 3x, and is even faster than boon (which is known to be pretty fast).

you can run this benchmark yourself with `gradle jmh`.


### So, what's the secret?

flatjson does not build a parse tree, but just a parse index ("overlay"), which is stored in an integer array. json nodes are constructed on demand (= on first access). this way, thousands of objects allocations are saved.

flatjson is best suited for cases where the full json document is not used, only parts of it.

### Usage

ok, let's parse some json:

```
Json json = Json.parse("[42, true, \"hello\"]");
```
this call returns a `Json` object. we can check what json entity it represents:

```
json.isNumber(); // --> false
json.isObject(); // --> false
json.isArray(); // --> true
```
aha, it's an array &mdash; so let's use it!

```
List<Json> array = json.asArray();
```
json arrays are represented by lists of `Json` objects.

```
array.size(); // --> 3
array.get(0).asLong(); // --> 42
array.get(1).asBoolean(); // --> true
array.get(2).asString(); // --> "hello"
```

TODO: describe builder methods

### License

[MIT](LICENSE.txt)



