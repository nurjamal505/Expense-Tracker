DROP TABLE IF EXISTS public.expenses CASCADE;
DROP TABLE IF EXISTS public.users CASCADE;
DROP TABLE IF EXISTS public.categories CASCADE;

CREATE TABLE public.categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE public.users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE public.expenses (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255),
    amount DOUBLE PRECISION NOT NULL,
    category_id INTEGER NOT NULL REFERENCES public.categories (id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES public.users (id),
    date DATE NOT NULL
);

INSERT INTO public.categories (name) VALUES
('Food'),
('Transport'),
('Entertainment'),
('Utilities'),
('Health'),
('Education'),
('Others');

INSERT INTO public.users (username, password, email) VALUES
('john_doe', 'password123', 'john@example.com'),
('jane_smith', 'password456', 'jane@example.com');
