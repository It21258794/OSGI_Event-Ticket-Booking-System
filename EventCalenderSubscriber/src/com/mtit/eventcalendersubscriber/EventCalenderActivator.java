package com.mtit.eventcalendersubscriber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mtit.eventscheduleservice.Event;
import com.mtit.eventscheduleservice.EventScheduleServicePublish;

public class EventCalenderActivator implements BundleActivator {
	// object declarations
	private Scanner scanner;
	ServiceReference eventServiceReference;
	// declare EventScheduleServicePublish object
	private EventScheduleServicePublish eventScheduleServicePublish;
	private DateTimeFormatter dateFormatter;

	public void start(BundleContext context) throws Exception {
		// Initializer Scanner for user input
		this.scanner = new Scanner(System.in);
		this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		System.out.println("Started Event Calender Service ");

		// get the reference service/publisher
		eventServiceReference = context.getServiceReference(EventScheduleServicePublish.class.getName());
		eventScheduleServicePublish = (EventScheduleServicePublish) context.getService(eventServiceReference);

		if (eventScheduleServicePublish == null) {
			System.out.println(" Event Schedule service available.");
		} else {
			// call the method to get user inputs for date range
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
		// Ask for viewing preference
		System.out.println("Choose how you want to view events:");
		System.out.println("1. View events with calendar");
		System.out.println("2. View events without calendar");
		int choice = Integer.parseInt(scanner.nextLine());

		if (choice == 1) {
			printCreativeCalendar(startDate, endDate, events);
		} else {
			printEventCards(events);
		}

	}

	private void printCreativeCalendar(LocalDate startDate, LocalDate endDate, List<Event> events) {
		// Calculate starting weekday and number of weeks
		int startDay = startDate.getDayOfWeek().getValue(); // 1 (Monday) - 7 (Sunday)
		long numDays = endDate.toEpochDay() - startDate.toEpochDay() + 1;
		int numWeeks = (int) Math.ceil((double) numDays / 7);

		// Print calendar header
		System.out.println("Event Calendar (" + startDate + " - " + endDate + ")");
		System.out.println("Su  Mo  Tu  We  Th  Fr  Sa");
		System.out.println("----------------------------------");

		int maxDayWidth = String.valueOf(endDate.getDayOfMonth()).length() + 1; // Calculate the maximum day width

		for (int week = 0; week < numWeeks; week++) {
			// Print day of month with leading spaces
			for (int day = 0; day < 7; day++) {
				LocalDate currentDate = startDate.plusDays((week * 7) + day - startDay + 1);
				String dateStr = String.format("%2d", currentDate.getDayOfMonth());
				// Highlight dates with events
				boolean hasEvent = events.stream()
						.anyMatch(event -> event.getEventDate().equals(currentDate.toString()));
				String formattedDate = hasEvent ? "[" + dateStr + "]" : dateStr;
				// Adjust spacing to maintain fixed width for each day
				int padding = maxDayWidth - formattedDate.length();
				String paddingStr = (padding > 0) ? String.format("%" + padding + "s", "") : "";
				formattedDate += paddingStr;

				// Adjust spacing for the last day of the week
				if (day == 6) {
					System.out.println(formattedDate);
				} else {
					System.out.print(formattedDate + " ");
				}
			}
		}

		// Print event details below the calendar
		if (!events.isEmpty()) {
			System.out.println("\nEvents:");
			for (Event event : events) {
				// Parse event start and end times (assuming they're in "YYYY-MM-DD HH:MM:SS"
				// format)
				LocalTime startTime = LocalTime.parse(event.getStartTime().split(" ")[1]); // Extract time part
				LocalTime endTime = LocalTime.parse(event.getEndTime().split(" ")[1]);

				// Format times using DateTimeFormatter
				DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
				String formattedStartTime = startTime.format(timeFormatter);
				String formattedEndTime = endTime.format(timeFormatter);

				int ticketPrice = (int) event.getTicketPrice();
				int seatPrice = (int) event.getSeatPrice();

				System.out.println("* " + event.getEventName() + " (" + event.getEventDate() + ")");
				System.out.println(" - Time: " + formattedStartTime + " - " + formattedEndTime);
				System.out.println(" - Venue: " + event.getVenue());
				System.out.println(" - Ticket Price: LKR " + ticketPrice);
				System.out.println(" - Seat Price: LKR " + seatPrice);
				System.out.println();
			}
		} else {
			System.out.println("No events found within the specified date range.");
		}
	}


	private void printEventCards(List<Event> events) {
		if (!events.isEmpty()) {
		    System.out.println("======Upcoming Events=====");

		    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy (hh:mm a)");

		    for (Event event : events) {
		        System.out.println("=============================================");

		        // Event name (adjust spacing as needed)
		        System.out.println("   " + event.getEventName() + "   ");

		        // Parse date, start time, and end time separately
		        LocalDate eventDate = LocalDate.parse(event.getEventDate());
		        LocalTime startTime = LocalTime.parse(event.getStartTime().split(" ")[1]); // Extract start time part
		        LocalTime endTime = LocalTime.parse(event.getEndTime().split(" ")[1]); // Extract end time part

		        // Combine date and time into LocalDateTime
		        LocalDateTime startDateTime = LocalDateTime.of(eventDate, startTime);
		        LocalDateTime endDateTime = LocalDateTime.of(eventDate, endTime);

		        // Format the combined datetime objects
		        String formattedStartDateTime = startDateTime.format(dateTimeFormatter);
		        String formattedEndDateTime = endDateTime.format(dateTimeFormatter);

		        System.out.println("  - Start Time: " + formattedStartDateTime);
		        System.out.println("  - End Time: " + formattedEndDateTime);

		        // Venue
		        System.out.println("  - Venue: " + event.getVenue());

		        // Ticket Information
		        int ticketPrice = (int) event.getTicketPrice();
		        if (ticketPrice > 0) {
		            System.out.println("  - Ticket Price: LKR " + ticketPrice);
		        } else {
		            System.out.println("  - Ticket Information: Check event details");
		        }

		        int seatPrice = (int) event.getSeatPrice();
		        if (seatPrice > 0) {
		            System.out.println("  - Seat Price: LKR " + seatPrice);
		        } else {
		            System.out.println("  - Seat Information: Check event details");
		        }

		        System.out.println("=============================================");
		        System.out.println(); // Add a blank line between event cards
		    }
		} else {
			System.out.println("No events found within the specified date range.");
		}
	}

}
