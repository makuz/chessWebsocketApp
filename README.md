# chessApp - nazwa projektu

aplikacja webowa do gry w szachy
z wykorzystaniem technologii websocket, do gry online

aplikacja jest osadzona w chmurze pod adresem

http://iboard-games.unicloud.pl/

Do uruchomienia alikacji musza byc zainstalowane:

1. Java 8
2. Maven
3. Mongo db

## uruchomienie

aplikacja jest podaczona do bazy danych w chmurze
na serwisie mongolab, aby ja uruchomic lokalnie
nie jest konieczna instalacja bazy danych
link do serwisu https://mongolab.com/login/?user=makuz
z pozycji tego serwisu jest mozliwosc zarzadzania baza danych

login: makuz
haslo: 1MojeFajneAuto

## uruchomienie aplikacji na lokalnym systemie

musi byc dostepny port 8080 na localhost
nalezy

- uruchomic terminal (linie komend)
- przejsc do głównego folderu z aplikacją chessApp
cd chessApp
- wykonac polecenie:
- mvn clean install

(ono zbuduje aplikacje i uruchomi serwer jetty na porcie 8080, serwera jetty nie trzeba instalowac, jest on wbudowany do aplikacji w postaci pluginu)

aplikacja będzie działać pod adresem:
localhost:8080

login i haslo do panelu admina:
login: unreal
haslo: AdminNaSzachownicyNa100%

# apliakcje moza podlaczyc pod lokalna baze danych

1. nalezy zainstalowac mongodb
2. w klasie MongoDBConnectionConfig w pakiecie com.chessApp.confs
w polu private String mongoLabConnectionString = ChessAppProperties
.getProperty("db.conn.production");

zamiast "db.conn.production" wpisac "db.conn.localhost"
w powloce mongodb wykonac komendy
z pliku init_db.txt 

- nastepnie wykonac polecenia z punktu uruchomienie aplikacji na lokalnym systemie



