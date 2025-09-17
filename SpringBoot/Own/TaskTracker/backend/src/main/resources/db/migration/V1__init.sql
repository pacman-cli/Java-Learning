CREATE TABLE IF NOT EXISTS app_changelog (
                                             id INT PRIMARY KEY AUTO_INCREMENT,
                                             note VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO app_changelog(note) VALUES ('Baseline migration applied');
