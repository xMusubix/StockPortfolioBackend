CREATE USER stock_portfolio;
CREATE DATABASE stock_portfolio;
GRANT ALL PRIVILEGES ON DATABASE stock_portfolio TO stock_portfolio;

\c stock_portfolio;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table if not exists assets_table (cost_value float(27), dividend_amount float4, dividend_yield float4, ex_date date, holding_value float(27), last_price float4, last_price_change float4, last_price_change_percentage float4, mo_price_1 float4, mo_price_3 float4, mo_price_6 float4, payout_ratio float4, percent_year float4, target float4, total_share float(27), wk_price_1 float4, year_high float4, year_low float4, year_price_1 float4, ytd_price float4, last_update_dividend timestamp(6), last_update_history_price timestamp(6), last_update_last_price timestamp(6), id uuid default uuid_generate_v4() not null, market_symbol varchar(20) unique, note varchar(255), primary key (id));
create table if not exists exchange_rate_table (date date not null unique, exchange_rate float(53) not null, last_update date not null, id uuid default uuid_generate_v4() not null, primary key (id));
create table if not exists industry_table (id uuid default uuid_generate_v4() not null, industry_name varchar(50) not null unique, sector_name varchar(50), primary key (id));
create table if not exists sector_table (id uuid default uuid_generate_v4() not null, sector_name varchar(50) not null unique, primary key (id));
create table if not exists transaction_cash_table (date date not null, ex_rate float4 not null, thb float4 not null, usd float4 not null, id uuid default uuid_generate_v4() not null, note varchar(255), type varchar(255) not null, primary key (id));
create table if not exists transaction_saving_table (amount float4 not null, date date not null, id uuid default uuid_generate_v4() not null, application varchar(255) not null, note varchar(255), type varchar(255) not null, primary key (id));
create table if not exists transaction_stock_table (date date not null, fee float4 not null, price float4 not null, share float(27) not null, id uuid default uuid_generate_v4() not null, market_symbol varchar(20), note varchar(255), type varchar(255) not null, primary key (id));
create table if not exists watchlist_table (line float4, score float4, last_update_jitta timestamp(6), market varchar(10) not null, symbol varchar(10) not null, id uuid default uuid_generate_v4() not null, market_symbol varchar(20) not null unique, state varchar(20), industry varchar(50), note varchar(255), primary key (id));
alter table if exists assets_table add constraint FKmthl0biem6agsumebwrf5bkli foreign key (market_symbol) references watchlist_table (market_symbol);
alter table if exists industry_table add constraint FK80dlsy9aly6hfsw49emhepvkn foreign key (sector_name) references sector_table (sector_name);
alter table if exists transaction_stock_table add constraint FKmqa1megh3jitrvaosdkn363vw foreign key (market_symbol) references watchlist_table (market_symbol);
alter table if exists watchlist_table add constraint FKnxurkojt99t7yrvd9kvju9vc0 foreign key (industry) references industry_table (industry_name);
