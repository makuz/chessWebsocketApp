# chessApp

aplikacja webowa do gry w szachy
z wykorzystaniem technologii websocket, do gry online

Do uruchomienia alikacji musza byc zainstalowane:

1. Java 8
2. Maven
3. Mongo db

## szybkie utuchomienie

aplikacja jest podaczona do bazy danych w chmurze
na serwisie mongolab, aby ja uruchomic lokalnie
nie jest konieczna instalacja bazy danych
link do serwisu https://mongolab.com/login/?user=makuz
z pozycji tego serwisu jest mozliwosc zarzadzania baza danych

login: makuz
haslo: 1MojeFajneAuto

## uruchomienie aplikacji lokalnie

musi byc dostepny port 8080 na localhost
nalezy

## uruchomienie poprzez terminal (zalecane)

- uruchomic terminal
- przejsc do głównego folderu z aplikacją
- wykonac polecenie:
- mvn clean install

(ono zbuduje aplikacje i uruchomi serwer jetty na porcie 8080)
port 8080 musi byc wolny

(jezel serwer sie nie uruchomi mozna jescze wykonac komende mvn jetty:run)

aplikacja będzie działać na adresie:
 localhost:8080

login i haslo do panelu admina:
login: unreal
haslo: AdminNaSzachownicyNa100%

# apliakcje moza podlaczyc pod lokalna baze danych

nalezy zainstalowac mongodb
w klasie MongoDBConnectionConfig w pakiecie com.chessApp.confs

w polu private String mongoLabConnectionString = ChessAppProperties
			.getProperty("db.conn.production");

zamiast "db.conn.production" wpisac "db.conn.localhost"

wprowadzic do bazy konto administratora wykonujac komendy
z pliku init_db.txt w powloce mongodb



