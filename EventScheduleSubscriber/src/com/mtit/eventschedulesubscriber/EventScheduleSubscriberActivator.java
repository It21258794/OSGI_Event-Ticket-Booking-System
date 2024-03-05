package com.mtit.eventschedulesubscriber;

import java.util.Scanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mtit.eventscheduleservice.EventScheduleServicePublish;

public class EventScheduleSubscriberActivator implements BundleActivator {

	//object declarations
	private Scanner scanner;
	ServiceReference eventServiceReference;
	//declare EventScheduleServicePublish object
	private EventScheduleServicePublish eventScheduleServicePublish;
	
	public void start(BundleContext context) throws Exception {
		
		//Initializer Scanner for user input
		this.scanner =  new Scanner(System.in);
		
		System.out.println("Started Event Schedule Subscriber Service ");
		
		//get the reference service/publisher
		eventServiceReference = context.getServiceReference(EventScheduleServicePublish.class.getName());
		eventScheduleServicePublish = (EventScheduleServicePublish)context.getService(eventServiceReference);

		
		if(eventScheduleServicePublish == null) {
            System.out.println(" Event Schedule service available.");
		}else{
			//call the method to get user inputs for event details
			addEventUserInput();
		}
	}

	
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stoped Event Schedule Subscriber Service ");
	}
	
	public void addEventUserInput() {
		System.out.println("Enter event details");
		
		System.out.println("Enter Event Name :");
		String eventName = scanner.nextLine();
		
		System.out.println("Enter Event Date as (YYYY-MM-DD) :");
		String date = scanner.nextLine();
		
		System.out.println("Enter Event Start Time (HH:MM:SS) :");
		String startTime = scanner.nextLine();
		
		System.out.println("Enter Event End Time (HH:MM:SS) :");
		String endTime = scanner.nextLine();
		
		System.out.println("Enter Event Venue :");
		String venue = scanner.nextLine();
		
		System.out.println("Enter Event Ticket Price: ");
		double ticketPrice = scanner.nextDouble();
		scanner.nextLine();// Consume newline character

		System.out.println(eventName);
		// Call addEvent method to add the event
		eventScheduleServicePublish.addEvent(eventName, date, startTime, endTime, venue, ticketPrice);
	}
	
	

}
