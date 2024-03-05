package com.mtit.eventschedulesubscriber;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.mtit.eventscheduleservice.EventScheduleServicePublish;

public class EventScheduleSubscriberActivator implements BundleActivator {

	ServiceReference eventServiceReference;
	
	public void start(BundleContext context) throws Exception {
		System.out.println("Started Event Schedule Subscriber Service ");
		
		eventServiceReference = context.getServiceReference(EventScheduleServicePublish.class.getName());
		EventScheduleServicePublish eventScheduleServicePublish = (EventScheduleServicePublish)context.getService(eventServiceReference);
		System.out.println(eventScheduleServicePublish.getEventSchedule());
	}

	
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stoped Event Schedule Subscriber Service ");
		context.ungetService(eventServiceReference);
	}

}
