-- =============================================
-- Prueba Ufinet Autos - Database Schema
-- SQL Server
-- =============================================

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'ufinet_autos_db')
BEGIN
    CREATE DATABASE ufinet_autos_db;
END
GO

USE ufinet_autos_db;
GO

-- Tabla de usuarios
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'users')
BEGIN
    CREATE TABLE users (
        id         BIGINT IDENTITY(1,1) PRIMARY KEY,
        email      VARCHAR(255)  NOT NULL,
        password   VARCHAR(255)  NOT NULL,
        created_at DATETIME2     NOT NULL DEFAULT GETDATE(),
        updated_at DATETIME2     NOT NULL DEFAULT GETDATE(),

        CONSTRAINT uq_users_email UNIQUE (email)
    );
END
GO

-- Tabla de autos
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'cars')
BEGIN
    CREATE TABLE cars (
        id            BIGINT IDENTITY(1,1) PRIMARY KEY,
        brand         VARCHAR(100)  NOT NULL,
        model         VARCHAR(100)  NOT NULL,
        year          INT           NOT NULL,
        license_plate VARCHAR(20)   NOT NULL,
        color         VARCHAR(50)   NOT NULL,
        photo_url     VARCHAR(500)  NULL,
        deleted       BIT           NOT NULL DEFAULT 0,
        user_id       BIGINT        NOT NULL,
        created_at    DATETIME2     NOT NULL DEFAULT GETDATE(),
        updated_at    DATETIME2     NOT NULL DEFAULT GETDATE(),

        CONSTRAINT uq_cars_license_plate UNIQUE (license_plate),
        CONSTRAINT fk_cars_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

    CREATE INDEX idx_cars_user_id       ON cars(user_id);
    CREATE INDEX idx_cars_license_plate ON cars(license_plate);
    CREATE INDEX idx_cars_brand         ON cars(brand);
    CREATE INDEX idx_cars_model         ON cars(model);
    CREATE INDEX idx_cars_year          ON cars(year);
END
GO

-- Datos precargados (opcional)
-- Insertar un usuario de prueba (password: password123)
-- INSERT INTO users (email, password) VALUES ('demo@ufinet.com', '$2b$10$uhm79rR2g/yC57R.wCtVm.KM.MOWiDxsBFpyB6SGYmcfxkaRMPyQG');
