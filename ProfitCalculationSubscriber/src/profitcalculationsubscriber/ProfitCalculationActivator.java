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
        System.out.println("Enter Event Id:");
        int eventId = scanner.nextInt();

        // Call addProfit method to calculate and add the profit
        profitCalculationServicePublish.addProfit( 0,  eventId,  0,  0, 0) ;
        }
}
