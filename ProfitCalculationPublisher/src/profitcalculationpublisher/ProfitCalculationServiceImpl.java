package profitcalculationpublisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;

public class ProfitCalculationServiceImpl implements ProfitCalculationServicePublish {

	private DatabaseConnectionService databaseConnectionService;

	@Override
	public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService) {
		this.databaseConnectionService = databaseConnectionService;
	}

	@Override
	public void addProfit(int calId, int eventId, double income, double budget, double profit) {

		if (databaseConnectionService == null) {
			System.err.println("DatabaseConnectionService is not set.");
			return;
		}

		Connection connection = databaseConnectionService.getConnection();
		if (connection == null) {
			System.err.println("Database connection is null.");
			return;
		}

		double seatCount = 0;
		double ticketCount = 0;
		double seatPrice = 0; // Declare seatPrice variable
		double ticketPrice = 0; // Declare ticketPrice variable
		int actualEventId = 0;
		double actualBudget = 0;

		try (PreparedStatement eventStatement = connection
				.prepareStatement("SELECT id, budget, seat_price, ticket_price FROM events WHERE id = ?")) {
			eventStatement.setInt(1, eventId);

			ResultSet eventResult = eventStatement.executeQuery();

			if (eventResult.next()) {
				actualEventId = eventResult.getInt("id");
				actualBudget = eventResult.getDouble("budget");
				seatPrice = eventResult.getDouble("seat_price"); // Assign seat_price value
				ticketPrice = eventResult.getDouble("ticket_price"); // Assign ticket_price value

				try (PreparedStatement ticketStatement = connection.prepareStatement(
						"SELECT TotalCount, Available, Type FROM ticketcount WHERE EventID = ? GROUP BY Type")) {
					ticketStatement.setInt(1, actualEventId);
					ResultSet ticketResult = ticketStatement.executeQuery();

					while (ticketResult.next()) {
						double totalCount = ticketResult.getDouble("TotalCount");
						double available = ticketResult.getInt("Available");
						String type = ticketResult.getString("Type");

						if ("seat".equals(type)) {
							seatCount = totalCount - available;
						} else if ("ticket".equals(type)) {
							ticketCount = totalCount - available;
						}
					}
				} catch (SQLException e) {
					System.err.println("Error retrieving ticket details: " + e.getMessage());
					e.printStackTrace();
				}

				income = (seatCount * seatPrice) + (ticketCount * ticketPrice);
				profit = income - actualBudget;

				eventResult = eventStatement.executeQuery("SELECT * FROM profits WHERE eventId = '" + eventId + "'");

				// Check if exists
				if (eventResult.next()) {
					System.out.print("Profit alredy calculated. ");
					System.out.print("Profit is: " + profit);
				} else {
					try (PreparedStatement statement = connection.prepareStatement(
							"INSERT INTO profits (calId, eventId, income, budget, profit) VALUES (?, ?, ?, ?, ?)")) {
						statement.setInt(1, calId);
						statement.setInt(2, actualEventId);
						statement.setDouble(3, income);
						statement.setDouble(4, actualBudget);
						statement.setDouble(5, profit);

						int rowsInserted = statement.executeUpdate();
						if (rowsInserted > 0) {
							System.out.println("Calculate profit successfully.");
							System.out.println("Profit is: " + profit);

						} else {
							System.err.println("Failed to calculate profit.");
						}
					} catch (SQLException e) {
						System.err.println("Error adding calculated profit to the database: " + e.getMessage());
						e.printStackTrace();
					}
				}
			} else {
				System.err.println("Event with ID " + eventId + " not found.");
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving event details: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
