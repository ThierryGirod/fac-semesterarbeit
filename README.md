# FAC Semesterarbeit

Autor: Thierry Girod\
Modul: Formale Sprachen, Automaten und Compilerbau, INF-W-AF009, BE-Sa-1, FS21\
Dozenten: Matthias Dehmer, Urs-Martin Künzi

Das Ziel des Kurses war eine einfache Programmiersprache, einen dafür geigneten Scanner und Parser, sowie einen Interpreter zu bauen.

Die Sprache muss folgendes unterstützen können:

- Variablen erstellen und lesen
- Einfache mathematische Berechnungen durchführen
- Logische Verzweigungen
- Schleifen
- Eigene Funktionen definieren und aufrufen

Ein detailierter Beschrieb kann in der
[Dokumentation](Dokumentation.md) nachgelesen werden.

## Projektinteraktion

Um das Projekt zu starten, müssen folgende Tools installiert sein:

- Git
- Java (openjdk11)
- Maven
- IDE nach Wahl (meine ist [VS Code](https://code.visualstudio.com/))

### Git-Repository klonen

```sh
$ git clone https://github.com/ThierryGirod/fac-semesterarbeit.git
```

### Build yourself

```sh
$ cd fac-semesterarbeit
$ mvn compile assembly:assembly
```

- Kompiliert und verpackt den Code inkl. Dependencies in ein JAR-Dokument.

Die Jar Datei kann danach mit folgendem Befehl ausgeführt werden:

```sh
$ java -jar target/fac-1.0-SNAPSHOT-jar-with-dependencies.jar
```

- Hierbei wird das Standardsamplefile durchlaufen.

Alternativ kann die Jar Datei auch unter dem [letzten Release](https://github.com/thierrygirod/fac-semesterarbeit/releases/latest) heruntergeladen werden.

Möchte eine eigene Datei ausgeführt werden, kann diese via Kommandozeilen-Parameter mitgegeben werden.

```sh
$ java -jar target/fac-1.0-SNAPSHOT-jar-with-dependencies.jar /path/to/my/file.txt false
```

Die Kommandozeile unterstützt zwei Parameter:

1. Pfad zur Datei
2. True or false für die komplette Ausgabe von Tokens, die gescannt werden und den generierten Parsebaum (Der Standardwert ist true).

Innerhalb des Projektes können mit folgenden Befehlen Dateien generiert werden.

## Projektbefehle

### Scanner generieren

```sh
$ mvn jflex:generate
```

### Parser generieren

```sh
$ mvn cup:generate
```
