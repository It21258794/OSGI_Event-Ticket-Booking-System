package profitcalculationpublisher;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;

public class ProfitCalculationServiceActivator implements BundleActivator {

    // Declare an object type of ServiceRegistration
    private ServiceRegistration publishProfitCalculateRegistration;
    // Declare an object type of DatabaseConnectionService
    private DatabaseConnectionService databaseConnectionService;
    // Declare an object type of ServiceReference for dbService
    private ServiceReference<DatabaseConnectionService> dbServiceReference;

    public void start(BundleContext context) throws Exception {
        System.out.println("Profit Calculate Publisher Started");

        // Get the DatabaseConnectionService from the OSGi service registry
        dbServiceReference = context.getServiceReference(DatabaseConnectionService.class);
        databaseConnectionService = context.getService(dbServiceReference);

        ProfitCalculationServicePublish profitCalculationServicePublish = new ProfitCalculationServiceImpl();
        // pass the database connection to the method setDatabaseConnection
        profitCalculationServicePublish.setDatabaseConnectionService(databaseConnectionService);

        // Register the ProfitCalculationServicePublish implementation with the service registry
        publishProfitCalculateRegistration = context.registerService(
                ProfitCalculationServicePublish.class.getName(), profitCalculationServicePublish, null);
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("Profit Calculation Publisher Stopped");
        // unregister the ProfitCalculationServicePublish
        if (publishProfitCalculateRegistration != null) {
            publishProfitCalculateRegistration.unregister();
        }
    }
}
