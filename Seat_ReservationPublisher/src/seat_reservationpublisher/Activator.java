package seat_reservationpublisher;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;



public class Activator implements BundleActivator {
	
	// Declare a object type of DatabaseConnectionService
	private DatabaseConnectionService databaseConnectionService;
	// Declare a object type of ServiceReference for dbService
	ServiceReference dbServiceReference;

	ServiceRegistration publishServiceRegistration;

	public void start(BundleContext context ) throws Exception {
		// Get the DatabaseConnectionService from the OSGi service registry
		dbServiceReference = context.getServiceReference(DatabaseConnectionService.class.getName());
		databaseConnectionService = (DatabaseConnectionService) context.getService(dbServiceReference);
		
		
		System.out.println("Publisher start");
		SeatServicePublish seatpublishService= new ServicePublishImpl();
		// pass the database connection to the method setDatabaseConnection
		seatpublishService.setDatabaseConnectionService(databaseConnectionService);
	    publishServiceRegistration = context.registerService(SeatServicePublish.class.getName(),seatpublishService, null);
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Publisher stop");
		publishServiceRegistration.unregister();
	}

}
