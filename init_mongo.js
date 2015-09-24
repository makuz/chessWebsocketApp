// zainicjowanie bazy danych aplikacji
// z kontem administratora
// po wykonaniu ponizszych polecen
// baza utworzy sie automatycznie
// Dodaje administratora serwisu.
// Login: admin
// Haslo: admin


// komenda uzyca bazy danych :
// use chessapp_db

db.users.insert({username : "admin", password : "d033e22ae348aeb5660fc2140aec35850c4da997", role : 1, "userId": 0, "isRegistrationConfirmed": true});
db.users.find().pretty();

