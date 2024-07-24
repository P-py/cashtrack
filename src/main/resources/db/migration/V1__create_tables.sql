CREATE TABLE user_cashtrack(
    id SERIAL PRIMARY KEY,
    username VARCHAR(40),
    email VARCHAR(100)
);

CREATE TYPE expense_type AS ENUM('MONTHLY_ESSENTIAL', 'ENTERTAINMENT',
                                'INVESTMENTS', 'LONGTIME_PURCHASE');

CREATE TYPE income_type AS ENUM('SALARY', 'GIFT', 'EXTRA');

CREATE TABLE expense(
    id SERIAL PRIMARY KEY,
    expense_label VARCHAR(30) NOT NULL,
    value REAL NOT NULL,
    type expense_type NOT NULL,
    date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

ALTER TABLE expense
    ADD COLUMN user_cashtrack_id INT
    CONSTRAINT user_expense_fk_user_id
    REFERENCES user_cashtrack(id)
    ON UPDATE CASCADE ON DELETE CASCADE;

CREATE TABLE income(
  id SERIAL PRIMARY KEY,
  income_label VARCHAR(30) NOT NULL,
  value REAL NOT NULL,
  type income_type NOT NULL,
  date_created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

ALTER TABLE income
    ADD COLUMN user_cashtrack_id INT
    CONSTRAINT user_income_fk_user_id
    REFERENCES user_cashtrack(id)
    ON UPDATE CASCADE ON DELETE CASCADE;