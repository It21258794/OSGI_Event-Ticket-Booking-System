package profitcalculationsubscriber;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import profitcalculationpublisher.ProfitCalculationServicePublish;

import java.sql.SQLException;
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
        scanner.close(); // Close the scanner to prevent resource leaks
    }

    private void inputProfitDetails() throws SQLException {
        System.out.println("Enter Event Id:");
        int eventId = scanner.nextInt();

        // Call addProfit method to calculate and add the profit
        profitCalculationServicePublish.addProfit(0, eventId, " ", 0, 0, 0, 0);

        profitCalculationServicePublish.displayProfit(0, 0, 0, 0, 0);

        System.out.print("Do you want to view monthly profit and loss? (yes/no)");
        scanner.nextLine(); // Consume the newline character left after nextInt()
        String decision = scanner.nextLine();
        if (decision.equals("yes")) {
            // Prompt user for input month
            System.out.println("Enter Month (format: YYYY-MM):");
            String inputYearMonth = scanner.nextLine();

            // Call the new method to display total profit and total loss for the user-input month
            profitCalculationServicePublish.displayTotalProfitAndLoss(inputYearMonth);

        } else if (decision.equals("no")) {
            System.out.println("Good Bye!");

        } else {
            System.out.println("Invalid input. Good Bye!");

        }
    }

}
