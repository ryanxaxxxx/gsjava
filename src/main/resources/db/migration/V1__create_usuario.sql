-- Drop tables for a clean baseline (SQL Server)
IF OBJECT_ID('dbo.endereco', 'U') IS NOT NULL
    DROP TABLE dbo.endereco;

IF OBJECT_ID('dbo.users', 'U') IS NOT NULL
    DROP TABLE dbo.users;

