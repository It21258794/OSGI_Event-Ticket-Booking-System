package seat_reservationsubscriber;

import java.util.Scanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import seat_reservationpublisher.SeatServicePublish;
import seat_reservationpublisher.ServicePublishImpl;


public class Activator implements BundleActivator {

	private ServiceReference<SeatServicePublish> serviceReference;

    public void start(BundleContext context) throws Exception {
        System.out.println("Start subscriber");
        serviceReference = context.getServiceReference(SeatServicePublish.class);
        SeatServicePublish servicePublish = context.getService(serviceReference);
        Scanner scanner = new Scanner(System.in);

        int choice = 0;
        while (choice != -4) {
            System.out.println("\n***** Welcome To Event Management *****");
            System.out.println("***** Seat Reservation *****");
            System.out.println("1. See available events");
            System.out.println("2. Book seat for a person");
            System.out.println("3. Cancel booking");
            System.out.println("***************************************");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n***** Available Events *****");
                    System.out.println("***************************");
                    System.out.print("Enter any key to continue: ");
                    int eventNumber = scanner.nextInt();
                    servicePublish.getEvent(eventNumber);
                    break;

                case 2:
                    System.out.println("\n***** Book Event For a Person *****");
                    System.out.println("Enter event: ");
                    int eventid = scanner.nextInt();
                    System.out.println("Enter name: ");
                    String name = scanner.next();
                    System.out.println("Enter contact : ");
                    int contact = scanner.nextInt();
                    System.out.println("Enter seat type: ");
                    String type = scanner.next();
                    System.out.println("Enter seat count: ");
                    int count = scanner.nextInt();
                    servicePublish.bookSeatForPerson(name, contact, type, count ,eventid);
                    break;

                
                
                
                case 3:
                    System.out.println("\n***** Cancel Booking *****");
                    System.out.println("Enter booking ID: ");
                    String bookingId = scanner.next();
                    servicePublish.cancelBooking(bookingId);
                    break;


                default:
                    System.out.println("Invalid choice! Please enter a valid option.");
                    break;
            }
        }
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("Goodbye!");
        context.ungetService(serviceReference);
    }

}
