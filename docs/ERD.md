erDiagram
    USERS {
        BIGINT id PK "AUTO_INCREMENT"
        VARCHAR_255 email UK "NOT NULL"
        VARCHAR_255 password "NOT NULL"
        VARCHAR_50 nickname "NOT NULL"
        DATETIME created_at "NOT NULL"
    }

    CATEGORY {
        BIGINT id PK "AUTO_INCREMENT"
        BIGINT user_id FK "NOT NULL"
        VARCHAR_50 name "NOT NULL"
        ENUM type "INCOME / EXPENSE, NOT NULL"
        DATETIME created_at "NOT NULL"
    }

    TRANSACTION {
        BIGINT id PK "AUTO_INCREMENT"
        BIGINT user_id FK "NOT NULL"
        BIGINT category_id FK "NOT NULL"
        ENUM type "INCOME / EXPENSE, NOT NULL"
        BIGINT amount "NOT NULL, > 0"
        VARCHAR_255 description "NULL"
        DATE transaction_date "NOT NULL"
        DATETIME created_at "NOT NULL"
        DATETIME updated_at "NOT NULL"
    }

    USERS ||--o{ CATEGORY : "owns"
    USERS ||--o{ TRANSACTION : "records"
    CATEGORY ||--o{ TRANSACTION : "classifies"