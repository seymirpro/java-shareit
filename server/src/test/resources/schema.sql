DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS item_requests;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS item_requests (
                                             id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
                                             description VARCHAR(1000),
    requestor_id BIGINT,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY(requestor_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS items (
                                     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                     name VARCHAR(512) NOT NULL,
    description TEXT NOT NULL,
    available BOOLEAN NOT NULL DEFAULT FALSE,
    owner_id BIGINT,
    request_id   BIGINT,
    CONSTRAINT pk_item PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (request_id) REFERENCES item_requests(id) ON DELETE CASCADE ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS bookings (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        start_dt TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        end_dt TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        item_id BIGINT,
                                        booker_id BIGINT,
                                        status VARCHAR(20),
    FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE,
    FOREIGN KEY(booker_id) REFERENCES users(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        text VARCHAR(500),
    item_id BIGINT,
    author_id BIGINT,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY(item_id) REFERENCES items(id) ON DELETE CASCADE,
    FOREIGN KEY(author_id) REFERENCES users(id) ON DELETE CASCADE
    );
