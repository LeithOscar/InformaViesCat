Database: informaViesCat

-- DROP DATABASE IF EXISTS "informaViesCat";

CREATE DATABASE "informaViesCat"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Catalan_Spain.1252'
    LC_CTYPE = 'Catalan_Spain.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
	
	-- Create table Rol
	CREATE TABLE Rol (
		id SERIAL PRIMARY KEY,
		RolName VARCHAR(255) NOT NULL
	);

	--  Create table user with restrictions
	CREATE TABLE Users (
		id SERIAL PRIMARY KEY,
		RolId INT REFERENCES Rol(id),
		Name VARCHAR(255),
		LastName VARCHAR(255),
		UserName VARCHAR(255),
		Password VARCHAR(255),
		Email VARCHAR(255),
		IsLogged BOOLEAN DEFAULT false
	);
	
	-- Data --
	-- Rol table
	INSERT INTO Rol (RolName) VALUES
		('Administrador'),
		('Técnico'),
		('Usuario');
		
	-- Insert into user table
	INSERT INTO Users (RolId, Name, LastName, UserName, Password, Email) VALUES
		(1, 'Juan', 'Pérez', 'juanperez', '1234', 'juan@example.com'), -- Rol: Administrador
		(2, 'María', 'González', 'mariagonzalez', '1234', 'maria@example.com'), -- Rol: Técnico
		(3, 'Pedro', 'Sánchez', 'pedrosanchez', '1234', 'pedro@example.com'), -- Rol: Usuario
		(1, 'Ana', 'López', 'analopez', '1234', 'ana@example.com'), -- Rol: Administrador
		(2, 'Luis', 'Martínez', 'luismartinez', '1234', 'luis@example.com'), -- Rol: Técnico
		(3, 'Elena', 'Rodríguez', 'elenarodriguez', '1234', 'elena@example.com'); -- Rol: Usuario


	-- Exemple Querys
	-- Get user by username and pass
		SELECT u.*, r.RolName
	FROM Users u
	JOIN Rol r ON u.RolId = r.id
	WHERE u.UserName = 'juanperez' AND u.Password = '1234';
