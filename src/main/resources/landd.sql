DROP TABLE IF EXISTS public.user;

CREATE TABLE public.user (
  id integer PRIMARY KEY NOT NULL,
  username character varying(45) NOT NULL,
  email character varying(45) NOT NULL,
  password character varying(45) NOT NULL
);