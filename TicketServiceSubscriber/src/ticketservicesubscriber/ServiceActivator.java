package ticketservicesubscriber;

import java.util.Scanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import ticketservicepublisher.TicketPublish;

public class ServiceActivator implements BundleActivator {

	ServiceReference serviceReference;

	public void start(BundleContext context) throws Exception {
		System.out.println("Start Subscriber Service");
		serviceReference = context.getServiceReference(TicketPublish.class.getName());
		TicketPublish ticketPublish = (TicketPublish)context.getService(serviceReference);
		getUserInput(ticketPublish);
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Thank you, come back later!!!");
		context.ungetService(serviceReference);
	}
	
	public void getUserInput(TicketPublish ticketPublish) {
		
        Scanner scanner = new Scanner(System.in);
		
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
        System.out.print("🎫 How many sections do you need for your tickets? ");
        int sections = scanner.nextInt();
        System.out.print("🎫 How many tickets would you like per section? ");
        int tickets = scanner.nextInt();
        
        
		ticketPublish.publishTickets(selectedEvent,sections,tickets);
		
	}

}
