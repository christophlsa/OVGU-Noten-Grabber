# OVGU Noten Grabber

Ein kleines Konsolenprogramm, welches von der Prüfungsnotenverwaltung "Hisqis"
von der Otto-von-Guericke Universität Magdeburg die Noten holt und sie
darstellt.

<http://blog.cgiesel.de/hisqis-noten-grabber>


## Benutzung

### Download

Als aller erstes wird die Jar benötigt. Lade dazu entweder die aktuellste
Version von <https://github.com/christophlsa/OVGU-Noten-Grabber/downloads>
herunter und entpacke diese. Dort ist die **HisqisNoten.jar** enthalten.

Oder hole dir den aktuellsten Quellcode mit dem Versionsverwaltungstool **git**:

    git clone git://github.com/christophlsa/OVGU-Noten-Grabber.git

Wenn du letzte Variante bevorzugst, so musst du noch den Quellcode kompilieren
(siehe Abschnitt ***Kompilieren***).

### Ausführen

Im selben Verzeichnis wie die Jar-Datei, rufst du entweder

    java -jar HisqisNoten.jar <user> <pass>

auf, wobei du **user** und **pass** durch die Hisqis Login Daten ersetzt
(ohne <>). Oder lasse die Login Daten weg und du wirst beim Ausführen
aufgefordert diese anzugeben.


## Entwickeln

Dies ist ein Eclipse Java Projekt. Wer dies in einer anderen IDE entwickeln
möchte, muss die Quelldateien aus dem **src** nehmen.


## Kompilieren

**Voraussetzungen:**

* Java JDK 1.6 oder höher
  (<http://www.oracle.com/technetwork/java/javase/downloads/index.html>)
  
* Apache Ant
  (<http://ant.apache.org>)
***oder***
* Eclipse
  (<http://www.eclipse.org>)

### Eclipse

Einfach das Projekt importieren und erstellen lassen.

### ANT Build

Im Projekt Verzeichnis **ant** ausführen. Daraufhin wird im Ordner **dist** die
Datei **HisqisNoten.jar** erstellt.

**Build Targets:**

* **init**    :: erstellt die benötigten Ordner
* **compile** :: erstellt im Ordner *build* die class Dateien
* **build**   :: erstellt die Jar Datei im Ordner *dist*
* **doc**     :: erstellt die Javadoc Dateien im Ordner *doc*
* **dist**    :: kopiert die erstellten Dateien und den Quellcode in den Ordner
                 *dist* und erstellt eine komprimiertes Tar Archiv.
* **clean**   :: löscht alle erstellten Ordner


## Probleme

### SSL Exception

Derzeit gibt es noch Probleme mit den SSL Zertifikaten. Wer von diesem Problem
betroffen ist, bekommt eine unbehandelte Exception.

**UPDATE:** Wahrscheinlich ist der Bug jetzt gefixt. Bitte melden, falls der
Fehler noch auftritt.


## Lizenz

Copyright (C) 2010-2011 Christoph Giesel

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
