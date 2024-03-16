package profitcalculationpublisher;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;
//import com.mtit.eventscheduleservice.Event;
import profitcalculationpublisher.Profit;
public interface ProfitCalculationServicePublish {

	public void addProfit(int calId, int eventId,String event_date, double income, double budget, double profit, double loss);
    void displayProfit(int eventId,double income,double budget ,double profit, double loss) throws SQLException;
	public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService);
    void displayTotalProfitAndLoss(String inputYearMonth) throws SQLException;


}
