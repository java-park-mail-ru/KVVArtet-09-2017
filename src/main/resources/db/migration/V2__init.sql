CREATE TABLE public.character_list (
  id serial PRIMARY KEY NOT NULL,
  characters_ids integer[]
);
CREATE TABLE public.character (
  id serial PRIMARY KEY NOT NULL,
  character_json_model jsonb NOT NULL
);
CREATE TABLE public.bag (
  id SERIAL PRIMARY KEY NOT NULL,
  name text NOT NULL,
  description text NOT NULL,
  items_ids integer[]
);
CREATE TABLE public.item (
  id SERIAL PRIMARY KEY NOT NULL,
  blueprint_json_model jsonb NOT NULL
)