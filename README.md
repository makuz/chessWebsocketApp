# chessApp

aplikacja webowa do gry w szachy
z wykorzystaniem technologii websocket, do gry online

Do uruchomienia alikacji musza byc zainstalowane:

1. Java 8
2. Maven
3. Mongo db

## inicjalizacja bazy danych

- zainstalowac baze mongodb
- otworzyc terminal i wlaczys shell mongodb
- na linuxie robi sie to komendami: 
mongod 
mongo
pierwszy to demon mongodb, drugi to uruchomienie powlowki dla mongo
- na windowsie trzeba uruchomic pliki
mongod.exe
mongo.exe
pierwszy to demon mongodb, drugi to uruchomienie powlowki dla mongo
- wykonac komendy zapisane w pliku  init_db.txt w monog shell (powloka mongo)

### Uruchomienie w systemie linux:

- uruchomic terminal
- wpisac 

## uruchomienie za pomoca maven

- przejsc do głównego folderu z aplikacją
- wykonac polecenie:

## mvn clean install

ta komenda zaciaga z intenretu zaleznosci zdefiniowane 

w pliku pom.xml, po tym aplikacja powinna juz dzialac

jezeli nie to trzeba wykonac jeszcze komende:

## mvn jetty:run

aplikacja będzie działać na adresie:

## localhost:8080

### Uruchomienie w systemie Windows:

- otworzyc aplikacje w srodowsku eclipse 
- kliknac prawym przyciskiem myszy na projekt
- kliknac kolejno na polecenia pod sciezka

### Run Us> Maven Clean
### Run Us> Maven Build
### Run Us> Maven Install

aplikacja powinna być dostepna pod adresem

## localhost:8080

login i haslo do panelu admina:
login: unreal
haslo: AdminNaSzachownicyNa100%



