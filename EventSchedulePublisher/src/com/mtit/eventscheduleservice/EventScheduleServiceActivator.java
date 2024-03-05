package com.mtit.eventscheduleservice;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class EventScheduleServiceActivator implements BundleActivator {

	ServiceRegistration publishEventScheduleRegistration;

	public void start(BundleContext context) throws Exception {
		System.out.println("Event Schedule Publisher Started");
		EventScheduleServicePublish eventScheduleServicePublish = new EventScheduleServiceImpl();
		
		publishEventScheduleRegistration = context.registerService(EventScheduleServicePublish.class.getName(), eventScheduleServicePublish, null);
	}


	public void stop(BundleContext context) throws Exception {
		System.out.println("Event Schedule Publisher Stoped");
		publishEventScheduleRegistration.unregister();
	}

}
