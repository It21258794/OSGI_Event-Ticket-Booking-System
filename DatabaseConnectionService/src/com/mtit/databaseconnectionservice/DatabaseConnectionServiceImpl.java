package com.mtit.databaseconnectionservice;

import java.sql.Connection;

public class DatabaseConnectionServiceImpl implements DatabaseConnectionService {

	private Connection connection;

	// constructor initializing connection object
	public DatabaseConnectionServiceImpl(Connection connection) {
		super();
		this.connection = connection;
	}

	// return the connection object
	@Override
	public Connection getConnection() {
		return connection;
	}

}
