package ticketservicesubscriber;

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
		System.out.println(ticketPublish.publishTickets());
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Thank you, come back later!!!");
		context.ungetService(serviceReference);
	}

}
