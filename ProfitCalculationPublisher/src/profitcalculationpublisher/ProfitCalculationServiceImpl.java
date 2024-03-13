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
	public void addProfit(int calId, int eventId,String event_date, double income, double budget, double profit, double loss) {

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
				.prepareStatement("SELECT id, event_date, budget, seat_price, ticket_price FROM events WHERE id = ?")) {
			eventStatement.setInt(1, eventId);

			ResultSet eventResult = eventStatement.executeQuery();

			if (eventResult.next()) {
				actualEventId = eventResult.getInt("id");
				event_date = eventResult.getString("event_date");
				actualBudget = eventResult.getDouble("budget");
				seatPrice = eventResult.getDouble("seat_price"); // Assign seat_price value
				ticketPrice = eventResult.getDouble("ticket_price"); // Assign ticket_price value

				try (PreparedStatement ticketStatement = connection.prepareStatement(
						"SELECT TotalCount, Available, Type FROM ticketcount WHERE event_id = ? GROUP BY Type")) {
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

				if(income > actualBudget){
					profit = income - actualBudget;
					loss = 0;

				}else{
					loss =  (-1)* (income-actualBudget);
					profit = 0;
				}

				eventResult = eventStatement.executeQuery("SELECT * FROM profits WHERE eventId = '" + eventId + "'");

				// Check if exists
				if (eventResult.next()) {
					System.out.println("Profit and Loss alredy calculated. ");
					System.out.println("Profit is: " + profit);
					System.out.println("Loss is: " + loss);
				} else {
					try (PreparedStatement statement = connection.prepareStatement(
							"INSERT INTO profits (calId, eventId,event_date, income, budget, profit,loss) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
						statement.setInt(1, calId);
						statement.setInt(2, actualEventId);
						statement.setString(3, event_date);
						statement.setDouble(4, income);
						statement.setDouble(5, actualBudget);
						statement.setDouble(6, profit);
						statement.setDouble(7, loss);

						int rowsInserted = statement.executeUpdate();
						if (rowsInserted > 0) {
							System.out.println("Calculate successfully.");
							System.out.println("Profit is: " + profit);
							System.out.println("Loss is: " + loss);

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
	
	public void displayProfit(int eventId,double income,double budget ,double profit, double loss) throws SQLException {
    	// checks the databaseConnectionService is null
    			if (databaseConnectionService == null) {
    				System.err.println("DatabaseConnectionService is not set.");
    			}

    			// invoke the getConnection method in db connection service and get the
    			// connection
    			Connection connection = databaseConnectionService.getConnection();
    			if (connection == null) {
    				System.err.println("Database connection is null.");
    			}

    			// create the prepare statement and run the select query
    			try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM profits")) {
    			    // Execute the query and get the result set
    			    try (ResultSet resultSet = preparedStatement.executeQuery()) {
    			        // Iterate through the result set and print each row
    			    	System.out.println(" ");
						System.out.println("-----------------------------------------------------");
    			    	System.out.println("Event ID  " + "Event Date   "  + "Income   " + "Budget    " + "Profit  " + "  Loss" );
//						System.out.println("-------------------------------------------------------|");
    			        while (resultSet.next()) {
    			            // Retrieve data from the current row
    			            int id = resultSet.getInt("eventId"); // Assuming "eventId" is a column name
    			            String date = resultSet.getString("event_date");
    			            double income1 = resultSet.getDouble("income");
    			            double budget1 = resultSet.getDouble("budget");
    			            double profit1 = resultSet.getDouble("profit");
    			            double loss1 = resultSet.getDouble("loss");
    			            
    			            System.out.println(id + "         " + date + "   " + income1 + "   " + budget1 + "    " +profit1 + "     " + loss1);
//    						System.out.println("-------------------------------------------------------|");
						// You can retrieve and print other columns similarly
    			        }
						System.out.println("-----------------------------------------------------");

    			    }
    			} catch (SQLException e) {
    			    System.out.println("try again");
    			}
	  }
	// Add this method to the ProfitCalculationServiceImpl class
	public void displayTotalProfitAndLoss(String inputYearMonth) throws SQLException {
	    if (databaseConnectionService == null) {
	        System.err.println("DatabaseConnectionService is not set.");
	        return;
	    }

	    Connection connection = databaseConnectionService.getConnection();
	    if (connection == null) {
	        System.err.println("Database connection is null.");
	        return;
	    }

	    System.out.println("");
	    try (PreparedStatement preparedStatement = connection.prepareStatement(
	            "SELECT SUM(profit) as totalProfit, SUM(loss) as totalLoss FROM profits WHERE SUBSTRING(event_date, 1, 7) = ?")) {
	        // Set the user-input year and month as a parameter
	        preparedStatement.setString(1, inputYearMonth);

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                double totalProfit = resultSet.getDouble("totalProfit");
	                double totalLoss = resultSet.getDouble("totalLoss");

	                System.out.println("Total Profit for " + inputYearMonth + ": " + totalProfit);
	                System.out.println("Total Loss for " + inputYearMonth + ": " + totalLoss);
	            } else {
	                System.out.println("No data found for the specified year and month: " + inputYearMonth);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error displaying total profit and loss: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	
}
