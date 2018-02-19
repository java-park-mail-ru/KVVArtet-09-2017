CREATE EXTENSION IF NOT EXISTS JSONB;

CREATE TABLE IF NOT EXISTS public.character_list (
  id serial PRIMARY KEY NOT NULL,
  characters_ids integer[]
);
CREATE TABLE IF NOT EXISTS public.character (
  id serial PRIMARY KEY NOT NULL,
  character_json_model JSONB NOT NULL
);
CREATE TABLE IF NOT EXISTS public.bag (
  id SERIAL PRIMARY KEY NOT NULL,
  name TEXT NOT NULL,
  description TEXT NOT NULL,
  items_ids integer[]
);
CREATE TABLE IF NOT EXISTS public.item (
  id SERIAL PRIMARY KEY NOT NULL,
  blueprint_json_model JSONB NOT NULL
)