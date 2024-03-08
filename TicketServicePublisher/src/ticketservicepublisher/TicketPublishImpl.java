package ticketservicepublisher;

import java.util.Scanner;

public class TicketPublishImpl implements TicketPublish{

	@Override
	public String publishTickets() {
		
        String[] events = {"Event 1", "Event 2", "Event 3"};

        // Print the menu
        System.out.println("Select an event to 'click':");
        for (int i = 0; i < events.length; i++) {
            System.out.println((i + 1) + ". " + events[i]);
        }

        // Get user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select Event: ");
        
        if (scanner.hasNextInt()) {
            int selectedEvent = scanner.nextInt();

            // Check if the selected index is valid
            if (selectedEvent >= 1 && selectedEvent <= events.length) {
                System.out.println("You Have Selected " + events[selectedEvent - 1]);
                
                scanner = new Scanner(System.in);
                System.out.print("How many sections are needed: ");
                
                int sections = scanner.nextInt();
                
                scanner = new Scanner(System.in);
                System.out.print("How many Tickets per section: ");
                
                int Tickets = scanner.nextInt();
                
                System.out.println("Available Tickets");
                
                for (int j = 0; j < sections; j++) {
                    char letter = (char) ('A' + j);
                    System.out.println("Section " + letter + ": " + Tickets);
                }
                
                System.out.println("Total " + Tickets * sections);
                    
            } else {
                System.out.println("Invalid input. Please enter a valid event.");
            }
        } else {
            System.out.println("Invalid input. Please enter a valid number.");
        }

        // Close the scanner
        scanner.close();
    
		
		return "Excute Ticket Publishersss";
	}

	@Override
	public String purchaseTickets() {
		
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many Tickets: ");
        
        int sections = 3;
        int currentSection = 0;
        int availableTicketsPerSection = 5;
        int totalTickets = scanner.nextInt();
        int ticketCount = 1;
        char letter = 'A';
        
        for (int j = 0; j < totalTickets; j++) {
        if(availableTicketsPerSection<ticketCount){
            currentSection++;
            ticketCount = 1;
        }
                        
        letter = (char) ('A' + currentSection);
        int randomNumber = (int) (Math.random() * 10000) + 1;
        System.out.println(letter + "" + randomNumber);
        ticketCount++;
               
        }
		
		return null;
	}
	
	
	

}
