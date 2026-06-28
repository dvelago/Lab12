IF DB_ID('lab12') IS NULL
BEGIN
    CREATE DATABASE lab12;
END;
GO

USE lab12;
GO

IF OBJECT_ID('dbo.CompraVideojuego', 'U') IS NOT NULL
    DROP TABLE dbo.CompraVideojuego;
GO

IF OBJECT_ID('dbo.Videojuego', 'U') IS NOT NULL
    DROP TABLE dbo.Videojuego;
GO

IF OBJECT_ID('dbo.Distribuidor', 'U') IS NOT NULL
    DROP TABLE dbo.Distribuidor;
GO

CREATE TABLE dbo.Distribuidor (
    IdDistribuidor INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Nombre NVARCHAR(45) NOT NULL
);
GO

CREATE TABLE dbo.Videojuego (
    IdVideojuego INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    Consola NVARCHAR(45) NOT NULL,
    Nombre NVARCHAR(45) NOT NULL,
    Genero NVARCHAR(45) NOT NULL,
    Clasificacion NVARCHAR(45) NOT NULL,
    Descripcion NVARCHAR(255) NOT NULL,
    IDdesarrollador INT NOT NULL,
    IDdistribuidor INT NOT NULL,
    Precio DECIMAL(10,2) NOT NULL,
    Stock INT NOT NULL CONSTRAINT DF_Videojuego_Stock DEFAULT 0,
    CONSTRAINT CK_Videojuego_Precio CHECK (Precio > 0),
    CONSTRAINT CK_Videojuego_Stock CHECK (Stock >= 0),
    CONSTRAINT FK_Videojuego_Distribuidor
        FOREIGN KEY (IDdistribuidor) REFERENCES dbo.Distribuidor(IdDistribuidor)
);
GO

CREATE TABLE dbo.CompraVideojuego (
    IdCompra INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    IdVideojuego INT NOT NULL,
    NombreVideojuego NVARCHAR(45) NOT NULL,
    Comprador NVARCHAR(100) NOT NULL,
    Correo NVARCHAR(120) NULL,
    Cantidad INT NOT NULL,
    PrecioUnitario DECIMAL(10,2) NOT NULL,
    Total DECIMAL(10,2) NOT NULL,
    MetodoPago NVARCHAR(30) NOT NULL,
    FechaCompra DATETIME2 NOT NULL CONSTRAINT DF_CompraVideojuego_Fecha DEFAULT SYSDATETIME(),
    CONSTRAINT CK_CompraVideojuego_Cantidad CHECK (Cantidad > 0),
    CONSTRAINT CK_CompraVideojuego_Total CHECK (Total > 0),
    CONSTRAINT FK_CompraVideojuego_Videojuego
        FOREIGN KEY (IdVideojuego) REFERENCES dbo.Videojuego(IdVideojuego)
);
GO

INSERT INTO dbo.Distribuidor (Nombre)
VALUES
    (N'Nintendo'),
    (N'Sony'),
    (N'Microsoft');
GO

INSERT INTO dbo.Videojuego
    (Consola, Nombre, Genero, Clasificacion, Descripcion, IDdesarrollador, IDdistribuidor, Precio, Stock)
VALUES
    (N'Switch', N'Super Mario Odyssey', N'Aventura', N'E', N'Aventura 3D de Mario con exploracion libre.', 101, 1, 199.90, 12),
    (N'PS5', N'Spider-Man 2', N'Accion', N'T', N'Nueva entrega con Peter Parker y Miles Morales.', 102, 2, 259.90, 8),
    (N'PC', N'Forza Horizon 5', N'Carreras', N'E', N'Conduccion de mundo abierto con gran variedad de autos.', 103, 3, 149.90, 15);
GO
