# bmir-coding-test

### Problem 1

**Requirement**: Design an interface that holds information about an OWL class
(i.e., class IRI, display name and referencing axioms). The interface has to be
documented in JavaDoc. Instances of this interface should be able to be sorted
by the display name.

**Source code**: [here](https://github.com/johardi/bmir-coding-test/tree/master/src/main/java/io/github/johardi/bmir/problem1) and [test here](https://github.com/johardi/bmir-coding-test/tree/master/src/test/java/io/github/johardi/bmir/problem1)

#### Compile and run
```
$ git clone https://github.com/johardi/bmir-coding-test.git
$ cd bmir-coding-test
$ mvn compile
$ mvn test -q
```

### Problem 2

**Requirement**: Write a program using OWL-API that is able to:

1. Load ontology from a file,
2. Print to stdout classes that have rdfs:label in Portuguese. The print output
format: `<IRI> "label"`
3. Construct new labels (use skos:prefLabel) using class IRI local name. And
then add these labels to the ontology and save it as `out.owl`

**Source code**: [here](https://github.com/johardi/bmir-coding-test/tree/master/src/main/java/io/github/johardi/bmir/problem2)

#### Run
```
$ mvn exec:java -Dexec.mainClass="io.github.johardi.bmir.problem2.Main" -q
```
