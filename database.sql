CREATE TABLE CHAMBRE(
	idChambre INTEGER,
	user1Id INTEGER,
	user2Id INTEGER
);

CREATE TABLE SALON(
	idSalon INTEGER,
	salonName VARCHAR(255),
	createTimestamp TIME
);

CREATE TABLE USER(
	username VARCHAR(255),
	surname VARCHAR(255),
	login VARCHAR(255),
	email VARCHAR(255),
	password VARCHAR(255)
);
select * from USER;


CREATE TABLE VOD(
	timeInSeconds INTEGER,
	loginAuthor VARCHAR(255),
	title VARCHAR(255)
);