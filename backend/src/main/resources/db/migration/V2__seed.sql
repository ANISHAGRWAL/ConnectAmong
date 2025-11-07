-- Seed users (password for all: Pass@123)
INSERT INTO users(email, password_hash, full_name, domain, skills, bio, avatar_url)
VALUES
 ('alice@student.com',  '$2a$10$F7H5P2SgY6o0q7CkWc7V2u6rja7kQ9q6Z0vXr8m5yUeG8P3zQb0f.', 'Alice Student', 'STUDENT', 'Java,SQL', 'CS undergrad', NULL),
 ('bob@teacher.com',   '$2a$10$F7H5P2SgY6o0q7CkWc7V2u6rja7kQ9q6Z0vXr8m5yUeG8P3zQb0f.', 'Bob Teacher',  'TEACHER', 'Pedagogy', 'Teaches CS', NULL),
 ('dev@developer.com', '$2a$10$F7H5P2SgY6o0q7CkWc7V2u6rja7kQ9q6Z0vXr8m5yUeG8P3zQb0f.', 'Dev Builder',  'DEVELOPER', 'Spring,React', 'Backend dev', NULL);

-- Seed groups
INSERT INTO groups(name, domain, description, visibility, created_by)
VALUES ('Java Learners', 'STUDENT', 'Learn Java together', 'PUBLIC', 1),
       ('Spring Boot Starters', 'DEVELOPER', 'All about Spring Boot', 'PUBLIC', 3);

-- Seed group members
INSERT INTO group_members(group_id, user_id) VALUES (1,1),(2,3);

-- Seed posts
INSERT INTO posts(author_id, domain, group_id, title, body)
VALUES
 (1, 'STUDENT', 1, 'How to learn OOP fast?', 'Share your best tips.'),
 (3, 'DEVELOPER', 2, 'Best way to structure services?', 'Service vs Manager?'),
 (3, 'DEVELOPER', NULL, 'DDD in small projects', 'Worth it for MVP?');

-- Seed comments
INSERT INTO comments(post_id, author_id, body) VALUES
 (1,1,'Focus on examples'), (2,3,'Keep it simple'), (3,3,'Depends on scope');

-- Seed reactions
INSERT INTO reactions(user_id, post_id, type) VALUES
 (1,1,'LIKE'), (3,2,'INSIGHTFUL'), (3,3,'LIKE');

-- Seed jobs
INSERT INTO jobs(domain, poster_id, title, company, location, type, description, apply_url)
VALUES
 ('STUDENT', 1, 'Java Intern', 'Acme', 'Kolkata', 'INTERNSHIP', '3-month internship learning Spring', 'https://apply.example.com/java-intern'),
 ('DEVELOPER', 3, 'Backend Engineer', 'TechCorp', 'Remote', 'FULLTIME', 'Own APIs in Spring Boot', NULL);
