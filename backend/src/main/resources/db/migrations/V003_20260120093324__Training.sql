CREATE TABLE training(
    training_id SERIAL PRIMARY KEY,
    athlete_id INT4 REFERENCES athlete(athlete_id) NOT NULL,
    sport_id INT4 REFERENCES sport(sport_id) NOT NULL,
    date TIMESTAMPTZ NOT NULL,
    metric REAL NOT NULL
);