CREATE TABLE ClubClass (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    startDate DATE,
    endDate DATE,
    startTime TIME,
    duration INT,
    capacity INT
);

CREATE TABLE Booking (
    id BIGINT PRIMARY KEY,
    memberName VARCHAR(255),
    class_id BIGINT,
    participationDate DATE,
    FOREIGN KEY (class_id) REFERENCES ClubClass (id)
);
