package com.mtit.eventscheduleservice;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;

public class EventScheduleServiceImpl implements EventScheduleServicePublish{

	private DatabaseConnectionService databaseConnectionService;
	@Override
	public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }
	
	@Override
	public List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate){
		
		 List<Event> events = new ArrayList<>();
		
		 //checks the databaseConnectionService is null
		if (databaseConnectionService == null) {
            System.err.println("DatabaseConnectionService is not set.");
        }

		// invoke the getConnection method in db connection service  and get the connection
        Connection connection = databaseConnectionService.getConnection();
        if (connection == null) {
            System.err.println("Database connection is null.");
        }
        
        // create the prepare statement and run the select query
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM events WHERE event_date BETWEEN ? AND ?")) {
        	
        	 Date sqlStartDate = Date.valueOf(startDate);
             Date sqlEndDate = Date.valueOf(endDate);
        	//set parameters
        	preparedStatement.setDate(1, sqlStartDate);
        	preparedStatement.setDate(2, sqlEndDate);
        	
        	//Execute the query
        	ResultSet resultset = preparedStatement.executeQuery();
        	//loop through the output resultset and add attributes to event object
        	while(resultset.next()) {
        		Event event = new Event();
        		event.setId(resultset.getInt("id"));
        		event.setEventName(resultset.getString("name"));
        		event.setEventDate(resultset.getString("event_date"));
        		event.setStartTime(resultset.getString("start_time"));
        		event.setEndTime(resultset.getString("end_time"));
        		event.setVenue(resultset.getString("venue"));
        		event.setTicketPrice(resultset.getDouble("ticket_price"));
        		
        		events.add(event);
        	}
        		
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
	}

	@Override
	public void addEvent(String eventName, String date, String startTime, String endTime, String venue,
			double ticketPrice, double budget) {
		
		 //checks the databaseConnectionService is null
		 if (databaseConnectionService == null) {
	            System.err.println("DatabaseConnectionService is not set.");
	            return;
	        }
		// invoke the getConnection method in db connection service  and get the connection
	        Connection connection = databaseConnectionService.getConnection();
	        if (connection == null) {
	            System.err.println("Database connection is null.");
	            return;
	        }

		String dateTimePattern = "yyyy-MM-dd HH:mm:ss"; // Define the date-time pattern
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);

		// Convert date and time strings to LocalDateTime objects
		LocalDateTime startDateTime = LocalDateTime.parse(date + " " + startTime, formatter);
		LocalDateTime endDateTime = LocalDateTime.parse(date + " " + endTime, formatter);

		// Convert LocalDateTime objects to Timestamp objects
		Timestamp eventStartTime = Timestamp.valueOf(startDateTime);
		Timestamp eventEndTime = Timestamp.valueOf(endDateTime);
		
		try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO events (name, event_date, start_time, end_time, venue, ticket_price,budget) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, eventName);
            statement.setString(2, date);
            statement.setTimestamp(3, eventStartTime);
            statement.setTimestamp(4, eventEndTime);
            statement.setString(5, venue);
            statement.setDouble(6, ticketPrice);
            statement.setDouble(7, budget);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Event added successfully.");
            } else {
                System.err.println("Failed to add event.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding event to database: " + e.getMessage());
            e.printStackTrace();
        }
		
	}

}
