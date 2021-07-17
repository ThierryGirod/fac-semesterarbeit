# Dokumentation

- Ein kommentiertes Demoskript kann unter `/src/main/resources/sample.txt` gefunden werden.
- Die Anleitung, wie das Repository verwendet werden kann, wird im [README](README.md) erklärt.

## Aufbau des Source Codes `/src/main/java/`

- `App.java`: Scannt, parst und führt das Demoskript aus.
- `scanner/`: Beinhaltet die Scanner-Definition in Form einer `.jflex`-Datei und die daraus generierte Java-Klasse.
- `parser/`: Beinhaltet die Parser-Definition in Form einer `.cup`-Datei und die daraus generierte Java-Klasse.
- `runtime/`: Alle Instruktions-Klassen, die den Parsebaum darstellen. Können von einem `Visitor` besucht werden.
- `interpreter/`: Implementation eines Interpreter nach dem Visitor-Pattern. Stellt die Ausführlogik bereit.
- `context/`: Implementation einer Kontext-Klasse für den Interpreter, um definierte Variablen und Funktionen zu speichern.

## Sprachentwurf

### Variablen definieren

Eine Variable definition besteht aus einem Identifikator, einem Zuweisungsoperator und einem Ausdruck, der zugewiesen wird.

Eine Variable soll dabei aus folgenden Ausdrücken bestehen können:

- Arithmetischem Ausdruck
- Nummer
- String
- Andere Variable
- Logischer Ausdruck
- Undefiniert (Wenn Variable keinen Wert hat)

In Code übersetzt, sollte also folgendes möglich sein:

```python
# Arithmetische Ausdrücke (+, -, /, *)
i = 12 + 13;
i = 2 - 5;
i = 3 / 2;
i = i * 5;
i = ((a + c) * d); # etwas komplexer

# Nummern zuweisen
my_variable = 12;

# String zuweisen
x = "Hello World";

# Variable anderer Variable zuweisen
__var = x;

# Logischer Ausdruck
b = 4 < 5;
b = x == 34;
b = x != y;

# Variable deklarieren, aber noch keinen Wert zuweisen

undef = undefined;

```

Um eine Variable zu definieren benötigen wir also 3 Teile:

1. Identifikator
2. Zuweisungsoperator
3. Einen Ausdruck

```python
ASSIGNMENT ::= IDENTIFIER EQUAL EXPRESSION;
```

Identifikator und Zuweisungsoperator sind Terminale, die wie folgt vom Scanner erkannt werden:

```python
identifier ::= [a-zA-z_]+[0-9]*
equal ::= =
```

Eine Variable darf also mit einer beliebigen Kombination aus Buchstaben, Underscore (\_) und Zahlen erfolgen, solange der Name nicht mit einer Zahl beginnt.

```python
_var # gültig
x # gültig
x3 # gültig

9sdp # ungültig
```

### Arithmetische Ausdrücke

Arithmetische Ausdrücke sollten in der ersten Version einfach gehalten werden und lediglich die Standardoperationen +, -, \* und / unterstützen. Um die Punkt vor Strich Regel zu beachten, müssen dies einzelnen Operationen eingeklammert werden. Als Zahlentyp können auch Fliesskommazahlen eingegeben werden.

Wir halten also fest, es sollen folgende Operationen möglich sein:

```python
# Einfache Operationen
1 + 2.5
x - 4
3.234 / 3
x * x

# Komplexere Operationen
# Klammern, um Punkt-vor-Strich-Regel sicherzustellen
(5 * (6 + 3))
((2.5 * (6.444 + 3)) / 10)
```

Um einen arithmetischen Ausdruck zu definieren benötigen wir also 4 Teile:

1. Linker Operand
2. Operator
3. Rechter Operator
4. Klammern um Punkt vor Strich zu gewähren.

```python
EXPRESSION ::=
    EXPRESSION ADD_SUB EXPRESSION |
    BRACKETS_LEFT EXPRESSION MULT_DIV EXPRESSION BRACKETS_RIGTH |
    BRACKETS_LEFT EXPRESSION BRACKETS_RIGTH |
    NUMBER
```

Nummern, die Klammern und die Operatoren sind Terminale, die wie folgt vom Scanner erkannt werden:

```python
number ::= [0-9]+(.[0-9]+)?
left_bracket ::= \(
right_bracket ::= \)
add_sub ::= \+\-]
mul_div ::= [\*\/]
```
