// zainicjowanie bazy danych aplikacji
// z kontem administratora
// po wykonaniu ponizszych polecen
// baza utworzy sie automatycznie
// Dodaje administratora serwisu.
// Login: admin
// Haslo: admin
//db.users.insert({username : "admin", password : "d033e22ae348aeb5660fc2140aec35850c4da997", role : 1, "userId": 0, "isRegistrationConfirmed": true});

// drugi
// Login: makuzadmin
// Haslo: AdminNaSzachownicyNa100%

// komenda uzyca bazy danych :
// use chessapp_db


db.users.insert({username : "makuzadmin", password : "853c388d6efc94dcd00cf6b117e27f02a27e0797", role : 1, "userId": 0, "isRegistrationConfirmed": true});
db.users.find().pretty();

