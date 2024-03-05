package com.mtit.eventscheduleservice;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;

public interface EventScheduleServicePublish {
	
	public String getEventSchedule();
	public void addEvent(String eventName,String date, String startTime, String endTime, String venue, double ticketPrice );
	public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService);

}
