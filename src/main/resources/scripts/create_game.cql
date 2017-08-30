CREATE KEYSPACE IF NOT EXISTS gamesdb WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
CREATE TABLE IF NOT EXISTS gamesdb.games (id TimeUUID PRIMARY KEY, title text, city text, rooms list<text>);
CREATE INDEX IF NOT EXISTS gametitleindex ON gamesdb.games(title);
