package ticketservicepublisher;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ServiceActivator implements BundleActivator {

	ServiceRegistration publishServiceRegistration;
	
	public void start(BundleContext context) throws Exception {
		
		System.out.println("Publisher Start");
		TicketPublish ticketPublish = new TicketPublishImpl();
		publishServiceRegistration = context.registerService(TicketPublish.class.getName(),ticketPublish,null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("Publisher Stop");
		publishServiceRegistration.unregister();
	}

}
