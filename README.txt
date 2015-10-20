# chessApp - nazwa projektu 


aplikacja webowa do gry w szachy 
z wykorzystaniem technologii websocket, 
do gry online



aplikacja jest osadzona w chmurze pod adresem


http://iboard-games.unicloud.pl/



Do uruchomienia alikacji na lokalnym systemie musza byc zainstalowane:



1. Java 8

2. Maven

3. MongoDb



## szybkie uruchomienie



aplikacja jest podlaczona do bazy danych w chmurze 
na serwisie mongolab, 
aby ja uruchomic lokalnie 
nie jest konieczna instalacja bazy danych


link do serwisu https://mongolab.com/login/?user=makuz

z pozycji tego serwisu jest mozliwosc zarzadzania baza danych


login: makuz

haslo: 1MojeFajneAuto



## uruchomienie aplikacji na lokalnym systemie



- musi byc dostepny port 8080 na localhost


nalezy

uruchomic terminal (linie komend)
 
- przejsc do glównego folderu z aplikacja chessApp

cd chessApp


- wykonac polecenie:


- mvn clean install



(ono zbuduje aplikacje i uruchomi serwer jetty na porcie 8080, serwera jetty nie trzeba instalowac, 
jest on wbudowany do aplikacji w postaci pluginu)



aplikacja bedzie dzialac na adresie:
 localhost:8080



login i haslo do panelu admina:

login: unreal

haslo: AdminNaSzachownicyNa100%

# 

apliakcje mozna podlaczyc pod lokalna baze danych



1. nalezy zainstalowac mongodb

2. w klasie MongoDBConnectionConfig w pakiecie com.chessApp.confs


w polu private String mongoLabConnectionString = ChessAppProperties
.getProperty("db.conn.production");



zamiast "db.conn.production" wpisac "db.conn.localhost"


w powloce mongodb wykonac komendy

z pliku init_db.txt 