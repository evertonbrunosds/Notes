CREATE TABLE user_profile (
    id uuid NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    user_name character varying(32) NOT NULL UNIQUE CHECK (user_name::text ~ '^(?!_)(?!.*__)[a-z0-9_]+(?<!_)$'::text),
    email character varying(256) NOT NULL UNIQUE,
    display_name character varying(32) NOT NULL CHECK (display_name::text ~ '^(?! )(?!.*  )[a-zA-Z0-9 ]+(?<! )$'::text),
    description text,
    birthday date NOT NULL CHECK (EXTRACT(year FROM age(CURRENT_DATE::timestamp with time zone, birthday::timestamp with time zone)) >= 18::numeric),
    password character(60) NOT NULL CHECK(LENGTH(password) = 60),
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);
