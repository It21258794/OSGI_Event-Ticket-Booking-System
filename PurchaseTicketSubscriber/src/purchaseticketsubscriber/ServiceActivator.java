package purchaseticketsubscriber;

import java.util.Scanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import ticketservicepublisher.TicketPublish;

public class ServiceActivator implements BundleActivator {

	ServiceReference serviceReference;
	
	public void start(BundleContext context) throws Exception {
		System.out.println("Start Subscriber Service");
		
		// Obtain the service reference
		serviceReference = context.getServiceReference(TicketPublish.class.getName());
		
		// Retrieve the TicketPublish service
		TicketPublish ticketPublish = (TicketPublish)context.getService(serviceReference);
		getUserInput(ticketPublish);
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Thank you, come back later!!!");
		context.ungetService(serviceReference);
	}
	
	public void getUserInput(TicketPublish ticketPublish) {
		//scanner object
        Scanner scanner = new Scanner(System.in);
		
        //print welcome screen
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║   Welcome to the Ticketing   ║");
        System.out.println("║           System!            ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  Choose an Available Events  ║");
        System.out.println("╚══════════════════════════════╝");
        ticketPublish.getEvents();
        System.out.println("");
        System.out.print("🎫 Please enter the number corresponding to your desired event: ");
        int selectedEvent = scanner.nextInt();
        
		if(!ticketPublish.sectionTicketAvalabiity(selectedEvent)) {
			System.out.println("Tickets are not available for this event");
			System.exit(0);
		}
        
        System.out.print("🎫 many Tickets: ");
        int amount = scanner.nextInt();
        
        
		ticketPublish.purchaseTickets(selectedEvent,amount);
		
        
		
	}

}
