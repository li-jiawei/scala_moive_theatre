CREATE TABLE movies (
	mid SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE shows (
	sid SERIAL PRIMARY KEY,
	movie_name VARCHAR(255),
	remaining_ticket INTEGER,
	show_time TIMESTAMP WITHOUT TIME ZONE,
	CONSTRAINT MOVIE_NAME_FK FOREIGN KEY(movie_name) REFERENCES movies(name) MATCH SIMPLE
	ON UPDATE NO ACTION
	ON DELETE NO ACTION,
	CHECK(remaining_ticket >= 0) NOT VALID
);

CREATE TABLE orders(
	oid CHAR(36) PRIMARY KEY,
	sid INTEGER NOT NULL,
	CONSTRAINT SHOW_ID_FK FOREIGN KEY(sid) REFERENCES shows(sid)
);


INSERT INTO movies(name) VALUES ('Test_Movie'), ('Allied'),('The_Edge_Of_Seventeen') ,('Hacksaw_Ridge') ,('Doctor_Strange') ,('Bad_Santa_2');
INSERT INTO shows(movie_name, remaining_ticket, show_time) VALUES
('Test_Movie', 100000 , '2016-12-10 11:00:00' ), 
('Test_Movie', 100000 , '2016-12-10 13:30:00' ), 
('Test_Movie', 100000 , '2016-12-10 15:20:00' ), 
('Test_Movie', 100000 , '2016-12-10 17:25:00' ), 
('Test_Movie', 100000 , '2016-12-10 20:50:00' ), 
('Test_Movie', 100000 , '2016-12-10 22:00:00' );