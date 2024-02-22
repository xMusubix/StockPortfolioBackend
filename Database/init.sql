CREATE USER stock_portfolio;
CREATE DATABASE stock_portfolio;
GRANT ALL PRIVILEGES ON DATABASE stock_portfolio TO stock_portfolio;

\c stock_portfolio;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";