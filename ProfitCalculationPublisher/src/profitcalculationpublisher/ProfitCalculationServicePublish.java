package profitcalculationpublisher;

import java.time.LocalDate;
import java.util.List;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;
//import com.mtit.eventscheduleservice.Event;
import profitcalculationpublisher.Profit;
public interface ProfitCalculationServicePublish {

	public void addProfit(int calId, String eventName, int soldTickets, double ticketPrice, double income, double cost,
			double profit);
	public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService);
	

}
