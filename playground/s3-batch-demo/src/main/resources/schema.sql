/*
-- Date: 2023-03-04 23:12
*/
DROP TABLE IF EXISTS movie;

CREATE TABLE movie (
  movie_id int(10) PRIMARY KEY NOT NULL,
  title varchar(1000),
  budget int(10),
  homepage varchar(1000),
  overview varchar(1000),
  popularity decimal(12,6),
  release_date date,
  revenue bigint(20),
  runtime int(5),
  movie_status varchar(50),
  tagline varchar(1000),
  vote_average decimal(4,2),
  vote_count int(10)
);