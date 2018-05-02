CREATE TABLE IF NOT EXISTS public.character_list (
  id SERIAL PRIMARY KEY NOT NULL,
  characters_ids integer[] DEFAULT array[]::integer[]
);

CREATE TABLE IF NOT EXISTS public.user (
  id serial PRIMARY KEY NOT NULL,
  username character varying(45) NOT NULL,
  email character varying(45) NOT NULL,
  password CHARACTER(60) NOT NULL,
  character_list_id INTEGER REFERENCES public.character_list(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE IF NOT EXISTS public.character (
  id serial PRIMARY KEY NOT NULL,
  character_json_model JSON NOT NULL
);

CREATE TABLE IF NOT EXISTS public.bag (
  id SERIAL PRIMARY KEY NOT NULL,
  bag_json_model JSON NOT NULL
);

CREATE TABLE IF NOT EXISTS public.item (
  id BIGINT PRIMARY KEY NOT NULL,
  blueprint_json_model JSON NOT NULL
);