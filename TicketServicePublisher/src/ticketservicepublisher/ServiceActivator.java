package ticketservicepublisher;
import com.mtit.databaseconnectionservice.DatabaseConnectionService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;

public class ServiceActivator implements BundleActivator {

	private DatabaseConnectionService databaseConnectionService;
	ServiceReference dbServiceReference;
	ServiceRegistration publishServiceRegistration;
	
	public void start(BundleContext context) throws Exception {
		
		System.out.println("Publisher Start");
		
		// Get the DatabaseConnectionService from the OSGi service registry
		dbServiceReference = context.getServiceReference(DatabaseConnectionService.class.getName());
		databaseConnectionService = (DatabaseConnectionService) context.getService(dbServiceReference);
		
		TicketPublish ticketPublish = new TicketPublishImpl();
		
		ticketPublish.setDatabaseConnectionService(databaseConnectionService);
		
		publishServiceRegistration = context.registerService(TicketPublish.class.getName(),ticketPublish,null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("Publisher Stop");
		publishServiceRegistration.unregister();
	}

}
