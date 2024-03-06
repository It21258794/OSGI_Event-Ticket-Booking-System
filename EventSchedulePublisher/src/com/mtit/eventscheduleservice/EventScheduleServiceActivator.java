package com.mtit.eventscheduleservice;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

public class EventScheduleServiceActivator implements BundleActivator {

	// Declare a object type of ServiceRegistration
	ServiceRegistration publishEventScheduleRegistration;
	// Declare a object type of DatabaseConnectionService
	private DatabaseConnectionService databaseConnectionService;
	// Declare a object type of ServiceReference for dbService
	ServiceReference dbServiceReference;

	public void start(BundleContext context) throws Exception {
		System.out.println("Event Schedule Publisher Started");

		// Get the DatabaseConnectionService from the OSGi service registry
		dbServiceReference = context.getServiceReference(DatabaseConnectionService.class.getName());
		databaseConnectionService = (DatabaseConnectionService) context.getService(dbServiceReference);

		EventScheduleServicePublish eventScheduleServicePublish = new EventScheduleServiceImpl();
		// pass the database connection to the method setDatabaseConnection
		eventScheduleServicePublish.setDatabaseConnectionService(databaseConnectionService);

		// Register the EventScheduleServicePublish implementation with the service
		// registry
		publishEventScheduleRegistration = context.registerService(EventScheduleServicePublish.class.getName(),
				eventScheduleServicePublish, null);

	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Event Schedule Publisher Stoped");
		// unregister the EventScheduleServicePublish
		publishEventScheduleRegistration.unregister();
	}

}
