-- Insert initial classes
INSERT INTO ClubClass (id, name, startDate, endDate, startTime, duration, capacity)
VALUES (1, 'Yoga Class', '2025-01-01', '2025-01-10', '10:00:00', 60, 10),
       (2, 'Zumba Class', '2025-01-05', '2025-01-15', '12:00:00', 45, 15);

-- Insert initial bookings
INSERT INTO Booking (id, memberName, class_id, participationDate)
VALUES (1, 'John Doe', 1, '2025-01-02'),
       (2, 'Jane Smith', 2, '2025-01-06');
