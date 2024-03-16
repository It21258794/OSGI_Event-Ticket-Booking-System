package ticketservicepublisher;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;

public interface TicketPublish {
	
	public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService);
	public void publishTickets(int selectedEvent,int sections,int tickets);
	public void purchaseTickets(int selectedEvent, int amount);
	public void getEvents();
	public boolean sectionTicketAvalabiity(int eventId);


}
