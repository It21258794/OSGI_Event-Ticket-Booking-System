package profitcalculationsubscriber;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import profitcalculationpublisher.ProfitCalculationServicePublish;

import java.util.Scanner;

public class ProfitCalculationActivator implements BundleActivator {

    private Scanner scanner;
    private ServiceReference profitServiceReference;
    private ProfitCalculationServicePublish profitCalculationServicePublish;

    @Override
    public void start(BundleContext context) throws Exception {
        this.scanner = new Scanner(System.in);

        System.out.println("Started profit calculation Subscriber Service ");

        profitServiceReference = context.getServiceReference(ProfitCalculationServicePublish.class.getName());
        profitCalculationServicePublish = (ProfitCalculationServicePublish) context.getService(profitServiceReference);

        if (profitCalculationServicePublish == null) {
            System.out.println("Profit calculation service not available.");
        } else {
            inputProfitDetails();
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Stopped Profit calculation Subscriber Service ");
    }

    private void inputProfitDetails() {
        System.out.println("Enter Event Name:");
        String eventName = scanner.nextLine();

        System.out.println("Enter Sold Tickets:");
        int soldTickets = scanner.nextInt();

        System.out.println("Enter Ticket Price:");
        double ticketPrice = scanner.nextDouble();

        System.out.println("Enter Cost:");
        double cost = scanner.nextDouble();
        double income= soldTickets*ticketPrice;
   	 double profit = income - cost  ;

        // Call addProfit method to calculate and add the profit
        profitCalculationServicePublish.addProfit( 0,  eventName,  soldTickets,  ticketPrice,  income,  cost, profit) ;
        }
}
