package profitcalculationpublisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;

public class ProfitCalculationServiceImpl implements ProfitCalculationServicePublish {

    private DatabaseConnectionService databaseConnectionService;

    @Override
    public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService) {
        this.databaseConnectionService = databaseConnectionService;
    }

    @Override
    public void addProfit(int calId, String eventName, int soldTickets, double ticketPrice, double income, double cost,
            double profit) {

        if (databaseConnectionService == null) {
            System.err.println("DatabaseConnectionService is not set.");
            return;
        }

        Connection connection = databaseConnectionService.getConnection();
        if (connection == null) {
            System.err.println("Database connection is null.");
            return;
        }
        
        

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO profits (calId, eventName, soldTickets,ticketPrice, income, cost,profit ) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            
        	
        	statement.setInt(1, calId);
            statement.setString(2, eventName);
            statement.setInt(3, soldTickets);
            statement.setDouble(4, ticketPrice);
            statement.setDouble(5, income);
            statement.setDouble(6, cost);
            statement.setDouble(7, profit);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Calculate profit successfully.");
            } else {
                System.err.println("Failed to calculate profit.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding calculated profit to database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
