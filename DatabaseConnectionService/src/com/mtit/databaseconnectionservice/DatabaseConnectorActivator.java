package com.mtit.databaseconnectionservice;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DatabaseConnectorActivator implements BundleActivator {

	// object declaration
	private Connection connection;
	ServiceRegistration dbServiceRegistration;

	public void start(BundleContext context) throws Exception {

		try {

			// Load the MySQL JDBC driver class
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish the DB connection
			String url = "jdbc:mysql://localhost:3306/osgi_event_ticket_booking_system";
			String userName = "root";
			String password = "2769";

			// Register the DatabaseConnectionService
			connection = DriverManager.getConnection(url, userName, password);
			DatabaseConnectionService databaseConnectionService = new DatabaseConnectionServiceImpl(connection);
			// Register the DatabaseConnectionService
			dbServiceRegistration = context.registerService(DatabaseConnectionService.class.getName(),
					databaseConnectionService, null);

		} catch (ClassNotFoundException e) {
			System.err.println("Error: MySQL JDBC driver class not found.");
			e.printStackTrace(); // Print stack trace for debugging
			throw new Exception("MySQL JDBC driver class not found.", e); // Re-throw the exception to signal bundle
																			// activation failure
		} catch (SQLException e) {
			System.err.println("Error: Failed to establish DB connection.");
			e.printStackTrace(); // Print stack trace for debugging
			throw new Exception("Failed to establish DB connection.", e); // Re-throw the exception to signal bundle
																			// activation failure
		}

	}

	public void stop(BundleContext context) throws Exception {

		System.out.println("DB service Stoped");
		// unregister the EventScheduleServicePublish
		dbServiceRegistration.unregister();
	}
}
