package seat_reservationpublisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;

public class ServicePublishImpl implements SeatServicePublish {
	
	
	private DatabaseConnectionService databaseConnectionService;
	
    public String publishService() {
        return "Event Management Service is published.";
    }

    public void getEvent(int id) throws SQLException {
    	// checks the databaseConnectionService is null
    			if (databaseConnectionService == null) {
    				System.err.println("DatabaseConnectionService is not set.");
    			}

    			// invoke the getConnection method in db connection service and get the
    			// connection
    			Connection connection = databaseConnectionService.getConnection();
    			if (connection == null) {
    				System.err.println("Database connection is null.");
    			}

    			// create the prepare statement and run the select query
    			try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM osgi_event_ticket_booking_system.events")) {
    			    // Execute the query and get the result set
    			    try (ResultSet resultSet = preparedStatement.executeQuery()) {
    			        // Iterate through the result set and print each row
    			    	System.out.println("Event ID "  + " Event Name ");
    			        while (resultSet.next()) {
    			            // Retrieve data from the current row
    			            int id1 = resultSet.getInt("id"); // Assuming "eventId" is a column name
    			            String eventname = resultSet.getString("eventName");
    			            
    			            System.out.println(id1 + "          " + eventname );
    			            // You can retrieve and print other columns similarly
    			        }
    			    }
    			} catch (SQLException e) {
    			    System.out.println("try again");
    			}

    			} 


    public void bookSeatForPerson(String name, int contact, String type, int count, int event) {
        try (Connection connection = databaseConnectionService.getConnection()) {
            // Check available seats before booking
            int availableSeats = getAvailableSeats(connection, event, type);
            if (availableSeats < count) {
                System.out.println("Not enough available seats of type " + type + " for booking.");
                return;
            }

            // Insert booking into bookperson table
            String insertSql = "INSERT INTO osgi_event_ticket_booking_system.bookperson (name, contact, type, count, event) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                insertStatement.setString(1, name);
                insertStatement.setInt(2, contact);
                insertStatement.setString(3, type);
                insertStatement.setInt(4, count);
                insertStatement.setInt(5, event);
                insertStatement.executeUpdate();

                System.out.println("Booking seat for " + name + ", Contact: " + contact + ", Seat Type: " + type + ", Count: " + count);
            }

            // Update available seats in TicketCount table
            String updateSql = "UPDATE osgi_event_ticket_booking_system.TicketCount SET available = available - ? WHERE EventID = ? AND type = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                updateStatement.setInt(1, count);
                updateStatement.setInt(2, event);
                updateStatement.setString(3, type);
                updateStatement.executeUpdate();
                System.out.println("Updated available seats for event with ID " + event);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Display updated seat reservations after booking
        //displaySeatReservation(event);
    }


    // Method to get available seats of a specific type for an event from the database
    private int getAvailableSeats(Connection connection, int EventID, String Type) throws SQLException {
        String query = "SELECT available FROM osgi_event_ticket_booking_system.TicketCount WHERE EventID = ? AND type = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, EventID);
            statement.setString(2, Type);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("available"); // Return the available seat count
                }
            }
        }
        return 0; // Return 0 if no seats are available
    }






    public void cancelBooking(int bookingId) {
        try (Connection connection = databaseConnectionService.getConnection()) {
            // Retrieve booking details
            String selectSql = "SELECT * FROM osgi_event_ticket_booking_system.bookperson WHERE bookpersonid = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setInt(1, bookingId);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    // Display booking details
                    if (resultSet.next()) {
                        String type = resultSet.getString("type");
                        int count = resultSet.getInt("count");

                        // Update ticketcount table based on canceled seat type
                        String updateSql = "UPDATE osgi_event_ticket_booking_system.ticketcount SET available = Available + ? WHERE eventid IN (SELECT event FROM osgi_event_ticket_booking_system.bookperson WHERE bookpersonid = ?)";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                            updateStatement.setInt(1, count);
                            updateStatement.setInt(2, bookingId);
                            int rowsAffected = updateStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Canceled booking for " + count + " " + type + " seats. Available seats updated.");
                            } else {
                                System.out.println("No booking found with ID " + bookingId);
                            }
                        }

                        // Update booking status to 'Canceled'
                        String updateBookingStatusSql = "UPDATE osgi_event_ticket_booking_system.bookperson SET status = 'Canceled' WHERE bookpersonid = ?";
                        try (PreparedStatement updateBookingStatusStatement = connection.prepareStatement(updateBookingStatusSql)) {
                            updateBookingStatusStatement.setInt(1, bookingId);
                            updateBookingStatusStatement.executeUpdate();
                            System.out.println("Booking status updated to 'Canceled'.");
                        }
                    } else {
                        System.out.println("Booking with ID " + bookingId + " not found.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error cancelling booking: " + e.getMessage());
        }
    }

   
    
    

        public void displaySeatReservation(int eventId) {
            // Fetch seat reservation data from the database based on the event ID
            Map<String, Integer> seatCounts = getSeatCountsFromDatabase(eventId);

            // Display VIP seat reservations
            System.out.println("******************************************");
            System.out.println("*             VIP Seat Reservation        *");
            System.out.println("******************************************");
            displaySeats(seatCounts.get("SEAT"));

       
        }

        private Map<String, Integer> getSeatCountsFromDatabase(int EventID) {
            // Fetch seat counts for VIP and normal seats from the database based on the event ID
            // Replace the following placeholder code with your actual database logic
            int seat = 20; // Example: Fetch from the database

            Map<String, Integer> seatCounts = new HashMap<>();
            seatCounts.put("seat", seat);
            return seatCounts;
        }

        private void displaySeats(int count) {
            System.out.println("       A    B    C    D    E    F    G    H");
            System.out.println("------------------------------------------");
            for (int i = 1; i <= 10; i++) {
                System.out.print("Row " + i + "  |");
                for (int j = 1; j <= 8; j++) {
                    if (count > 0) {
                        System.out.print(" [X] "); // Seat is reserved
                        count--;
                    } else {
                        System.out.print(" [ ] "); // Seat is available
                    }
                }
                System.out.println("|");
                if (count <= 0) break; // Break if all seats are reserved
            }
            System.out.println("------------------------------------------");
        }

    


	@Override
	public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService) {
		this.databaseConnectionService = databaseConnectionService;
		
	}

	



}
