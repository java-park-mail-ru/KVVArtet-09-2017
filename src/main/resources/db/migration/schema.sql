DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS forums CASCADE;
DROP TABLE IF EXISTS threads CASCADE;
DROP TABLE IF EXISTS posts  CASCADE;
DROP TABLE IF EXISTS votes CASCADE;

CREATE TABLE IF NOT EXISTS public.user (
  id serial PRIMARY KEY NOT NULL,
  username character varying(45) NOT NULL,
  email character varying(45) NOT NULL,
  password CHARACTER(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.character_list (
  id serial PRIMARY KEY NOT NULL,
  characters_ids integer[]
);

CREATE TABLE IF NOT EXISTS public.character (
  id serial PRIMARY KEY NOT NULL,
  character_json_model JSON NOT NULL
);

CREATE TABLE IF NOT EXISTS public.bag (
  id SERIAL PRIMARY KEY NOT NULL,
  name TEXT NOT NULL,
  description TEXT NOT NULL,
  items_ids integer[]
);

CREATE TABLE IF NOT EXISTS public.item (
  id BIGINT PRIMARY KEY NOT NULL,
  blueprint_json_model JSON NOT NULL
)