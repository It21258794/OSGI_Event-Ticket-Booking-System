package com.mtit.eventcalendersubscriber;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mtit.eventscheduleservice.Event;
import com.mtit.eventscheduleservice.EventScheduleServicePublish;

public class EventCalenderActivator implements BundleActivator {
	//object declarations
	private Scanner scanner;
	ServiceReference eventServiceReference;
	//declare EventScheduleServicePublish object
	private EventScheduleServicePublish eventScheduleServicePublish;
	 private DateTimeFormatter dateFormatter;
		
	public void start(BundleContext context) throws Exception {
		//Initializer Scanner for user input
		this.scanner =  new Scanner(System.in);
		this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		System.out.println("Started Event Calender Service ");
				
		//get the reference service/publisher
		eventServiceReference = context.getServiceReference(EventScheduleServicePublish.class.getName());
		eventScheduleServicePublish = (EventScheduleServicePublish)context.getService(eventServiceReference);
		
		if(eventScheduleServicePublish == null) {
            System.out.println(" Event Schedule service available.");
		}else{
			//call the method to get user inputs for date range
			addUserInput();
		}
	}

	
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stoped Event Calender Service ");
	}
	
	public void addUserInput() {
		
		System.out.println("Enter the start date (YYYY-MM-DD) :");
		String startDateStr = scanner.nextLine();
		
		System.out.println("Enter the End date (YYYY-MM-DD) :");
		String endDateStr = scanner.nextLine();
		
		LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
        LocalDate endDate = LocalDate.parse(endDateStr, dateFormatter);
		
		List<Event> events = eventScheduleServicePublish.getEventsByDateRange(startDate, endDate);
		
	    // Print the events
	    if (events.isEmpty()) {
	          System.out.println("No events found for the specified date range.");
	    } else {
	         System.out.println("Events for the specified date range:");
	         for (Event event : events) {
	              System.out.println("Event Name: " + event.getEventName());
	                System.out.println("Date: " + event.getEventDate());
	                System.out.println("Time: " + event.getStartTime() + " - " + event.getEndTime());
	                System.out.println("Venue: " + event.getVenue());
	                System.out.println("Ticket Price: " + event.getTicketPrice());
	                System.out.println();
	       }
	   }
	}
	

}
