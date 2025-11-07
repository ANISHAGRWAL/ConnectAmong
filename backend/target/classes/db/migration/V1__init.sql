CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(191) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  domain VARCHAR(50) NOT NULL,
  skills VARCHAR(255),
  bio VARCHAR(500),
  avatar_url VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE groups (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  domain VARCHAR(50) NOT NULL,
  description VARCHAR(300),
  visibility ENUM('PUBLIC','PRIVATE') DEFAULT 'PUBLIC',
  created_by BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_groups_creator FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE group_members (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  group_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  role ENUM('MEMBER') DEFAULT 'MEMBER',
  joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (group_id, user_id),
  CONSTRAINT fk_gm_group FOREIGN KEY (group_id) REFERENCES groups(id),
  CONSTRAINT fk_gm_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE posts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  author_id BIGINT NOT NULL,
  domain VARCHAR(50) NOT NULL,
  group_id BIGINT NULL,
  title VARCHAR(150),
  body TEXT NOT NULL,
  media_url VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_posts_author FOREIGN KEY (author_id) REFERENCES users(id),
  CONSTRAINT fk_posts_group FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE comments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  post_id BIGINT NOT NULL,
  author_id BIGINT NOT NULL,
  body VARCHAR(800) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts(id),
  CONSTRAINT fk_comments_author FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE reactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  post_id BIGINT NOT NULL,
  type ENUM('LIKE','INSIGHTFUL') NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(user_id, post_id, type),
  CONSTRAINT fk_react_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_react_post FOREIGN KEY (post_id) REFERENCES posts(id)
);

CREATE TABLE jobs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  domain VARCHAR(50) NOT NULL,
  poster_id BIGINT NOT NULL,
  title VARCHAR(150) NOT NULL,
  company VARCHAR(120),
  location VARCHAR(120),
  type ENUM('FULLTIME','PARTTIME','INTERNSHIP','CONTRACT') DEFAULT 'FULLTIME',
  description TEXT,
  apply_url VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_jobs_poster FOREIGN KEY (poster_id) REFERENCES users(id)
);

CREATE TABLE job_applications (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  job_id BIGINT NOT NULL,
  applicant_id BIGINT NOT NULL,
  cover_note VARCHAR(600),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(job_id, applicant_id),
  CONSTRAINT fk_app_job FOREIGN KEY (job_id) REFERENCES jobs(id),
  CONSTRAINT fk_app_user FOREIGN KEY (applicant_id) REFERENCES users(id)
);
