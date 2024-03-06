package com.mtit.eventscheduleservice;

import java.time.LocalDate;
import java.util.List;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;

public interface EventScheduleServicePublish {

	List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate);

	public void addEvent(String eventName, String date, String startTime, String endTime, String venue,
			double ticketPrice, double budget);

	public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService);

}
