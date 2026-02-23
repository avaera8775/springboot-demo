-- Seed data — loaded automatically at startup via spring.sql.init.mode=always
-- Great for demos: the API has data the moment the app starts!

INSERT INTO books (title, author, published_year, description) VALUES
  ('The Hobbit', 'J.R.R. Tolkien', 1937, 'A fantasy novel about Bilbo Baggins and his unexpected adventure.'),
  ('1984', 'George Orwell', 1949, 'A dystopian novel set in a totalitarian society under constant surveillance.'),
  ('Clean Code', 'Robert C. Martin', 2008, 'A handbook of agile software craftsmanship with practical coding techniques.'),
  ('Designing Data-Intensive Applications', 'Martin Kleppmann', 2017, 'A deep dive into the principles of building reliable, scalable systems.'),
  ('The Pragmatic Programmer', 'David Thomas', 1999, 'Practical advice for software developers to improve their craft.');
