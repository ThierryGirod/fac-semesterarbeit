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

Generell werden alle Befehle mit einem Semicolon (;) beendet. Dazu gehören auch If Else Zweige, Schleifen oder Funktionsdefinitionen.

Der Scanner erkennt das Semicolon wie folgt:

```python
TERMINATOR ::= ;
```

### Variablen definieren

Eine Variable definition besteht aus einem Identifikator, einem Zuweisungsoperator und einem Ausdruck, der zugewiesen wird.

Eine Variable soll dabei aus folgenden Ausdrücken bestehen können:

- Arithmetischem Ausdruck
- Nummer
- String
- Andere Variable
- Logischer Ausdruck
- Funktionsaufruf
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

# Funktionsaufruf
f = :b(2,1);

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
IDENTIFIER ::= [a-zA-z_]+[0-9]*
EQUAL ::= =
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
NUMBER ::= [0-9]+(.[0-9]+)?
BRACKETS_LEFT ::= \(
BRACKETS_RIGHT::= \)
ADD_SUB ::= \+\-]
MUL_DIV ::= [\*\/]
```

### Bedingte Anweisungen

In der Programmiersprache soll es möglich sein, auf Basis von logischen Anweisungen Entscheidungen zu treffen.

Dafür muss zuerst geklärt sein, wie eine logischer Bedingung zustande kommt. An dieser Stelle habe ich darauf verzichtet, Verkettungen von logischen Operationen einzubauen (and und or). Es ist aber möglich, wie bei den arithmetischen Ausdrücken diese zu verschachteln und komplexere Ausdrücke zu erstellen.

```python
# Einfache Operationen (==, !=, >, <, >=, <=)
1 == 2.5
x != 4
3.234 >= 3
x > y
3.234 <= 3
x < y
True
False

# Komplexere Operationen in der zweiten Ebene nur noch mit (==, !=)
# Klammern, um logische Reihenfolge sicherzustellen
(True != (6 == 3)) -> False
(6 < (5 > 4)) -> Fehler, da Zahl < Boolean Ausdruck nicht unterstütz wird
```

Hier haben wir einen Audruck oder 4 Teile:

1. Linker Operand
2. Operator
3. Rechter Operator
4. Klammern

```python
BOOLEAN_CONDITION ::= BOOLEAN_VALUE |
    EXPRESSION BOOLEAN_OPERATOR EXPRESSION
```

Die boolschen Werte true und false und die Operatoren sind Terminale, die wie folgt vom Scanner erkannt werden:

```python
BOOLEAN_VALUE ::= True | False
DOUBLE_EQUAL ::= ==
NOT_EQUAL ::= \!=
GREATER ::= >
SMALLER ::= <
GREATER_EQUAL_ ::= >=
SMALLER_EQUAL_ ::= <=
```

Um auf Basis dieser logischen Bedingungen Entscheidungen zu treffen, wird ein if-then-else Statement eingebaut, welches wie folgt aufgebaut ist:

```python
if (a < b) then {
 # Ausführbarer Code true block
} else {
    # Ausführbarer Code false block
};

```

In Backus-Naur Form:

```python
IFTHENELSE ::=
    IF BRACKETS_LEFT BOOLEAN_CONDITION BRACKETS_RIGTH THEN CURLY_BRACKETS_LEFT BLOCK CURLY_BRACKETS_RIGTH ELSE CURLY_BRACKETS_LEFT BLOCK CURLY_BRACKETS_RIGTH

```

In Version 1 der Sprache muss zwingend ein Else Block geschrieben werden. Das If, then, else keyword, sowie die geschweiften Klammern sind Terminale, die wie folgt vom Scanner erkennt werden:

```python
IF ::= if
THEN ::= then
ELSE ::= else
CURLY_BRACKETS_LEFT ::= \{
CURLY_BRACKETS_RIGTH ::= \}
```

### Schleifen

Um eine gewisse Automation in die Sprache einzubauen, wurde die While Schleife implementiert.

```python
while (BOOLEAN_CONDITION) {
 # Ausführbarer Code
};
```

In Backus-Naur Form:

```python
LOOP ::=
    WHILE BRACKETS_LEFT BOOLEAN_CONDITION BRACKETS_RIGTH CURLY_BRACKETS_LEFT BLOCK CURLY_BRACKETS_RIGTH
```

Das While-Keyword ist neben den Klammern auch ein Terminal und wird vom Scanner wie folgt erkannt:

```python
WHILE ::= while
```

### Definition von eigenen Funktionen

Eine Funktionsdefinition ist nach folgendem Schema aufgebaut:

```python
# Funktion ohne Parameter
a() => {
    # Ausführbarer Code
};

# Funktion mit Parametern und Rückgabewert
a(x,y,z)  => {
 # Ausführbarer Code
 return ((x + y) * z);
};
```

In Backus-Naur Form:

```python
FUNCTION_DEFINITION ::=
    IDENTIFIER BRACKETS_LEFT BRACKETS_RIGTH ARROW CURLY_BRACKETS_LEFT BLOCK CURLY_BRACKETS_RIGTH |
     IDENTIFIER BRACKETS_LEFT PARAMETER_LIST BRACKETS_RIGTH ARROW CURLY_BRACKETS_LEFT BLOCK CURLY_BRACKETS_RIGTH
```

Das Return-Keyword und der Pfeil ist neben dem Identifier und Klammern auch ein Terminal und wird vom Scanner wie folgt erkannt:

```python
ARROW ::= =>
RETURN ::= return
```

An dieser Stelle sei erwähnt, dass Funktionen überladen werden können und auch mit dem Return-Keyword einen Rückgabewert senden können. Ein einfaches Return gibt undefined zurück, gleich wie eine Funktion ohne Rückgabewert

### Aufruf von Funktionen

Die eigens definierten Funktionen werden über einen "Caller" aufgerufen, der als Doppelpunkt (:) definiert ist.

```python
:my_function();
:funktion_mit_argumenten(2,x,"String", 2 > 3);
```

Als Argument können hierbei direkt beliebige Ausdrücke übergeben werden.

In Backus-Naur Form:

```python
FUNCTION_CALL ::=
    COLON IDENTIFIER BRACKETS_LEFT BRACKETS_RIGTH |
    COLON IDENTIFIER BRACKETS_LEFT ARGUMENT_LIST BRACKETS_RIGTH
```

Als spezielle Funktion kann die :print() Funktion angesehen werden. Diese ist eine Build-In Funktion, die es erlaubt, beliebige Ausdrücke auf der Kommandozeile auszugeben.

Eine komplette Liste aller Projektionen folgt weiter unten.

## Scanner

Komplette Liste aller vom Scanner erkennbaren Tokens und deren Patterns.

### Tokens

| Bedeutung                                       | Pattern            |
| ----------------------------------------------- | ------------------ |
| Variablenzuweisung                              | `=`                |
| Variablenname                                   | `[a-zA-z_]+[0-9]*` |
| Addition                                        | `+`                |
| Subtraktion                                     | `-`                |
| Multiplikation                                  | `*`                |
| Division                                        | `/`                |
| Linke Klammer                                   | `(`                |
| Rechte Klammer                                  | `)`                |
| Zahl                                            | `[0-9]+(.[0-9]+)?` |
| Gleichheit                                      | `==`               |
| Ungleichheit                                    | `!=`               |
| Grösser                                         | `>`                |
| Kleiner                                         | `<`                |
| Grösser gleich                                  | `>=`               |
| Kleiner gleich                                  | `<=`               |
| If Keyword                                      | `if`               |
| Then Keyword                                    | `then`             |
| Else Keyword                                    | `else`             |
| Bool'sche Werte                                 | `True` und `False` |
| Start eines neuen Codeblocks                    | `{`                |
| Ende eines Codeblocks                           | `}`                |
| Schlaufe                                        | `while`            |
| Funktionsname                                   | `[a-zA-z_]+[0-9]*` |
| Trennzeichen zwischen Argumenten und Parametern | `,`                |
| Funktionsdefinition                             | `=>`               |
| Funktionscaller                                 | `:`                |
| Printfunktion Keyword                           | `print`            |
| String                                          | `"[^\"]*"`         |
| Instruktionsende                                | `;`                |
| Rückgabe-Schlüsselwort                          | `return`           |
| Undefinierter Wert                              | `undefined`        |
| Kommentar (kann Zeilenumbrüche beinhalten)      | `"/*"~"*/"`        |
