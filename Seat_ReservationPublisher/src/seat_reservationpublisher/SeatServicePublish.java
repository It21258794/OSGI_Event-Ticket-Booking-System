package seat_reservationpublisher;

import java.sql.SQLException;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;

public interface SeatServicePublish {
	String publishService();
    void getEvent(int id) throws SQLException;
    void bookSeatForPerson(String name, int contact, String type, int count , int event);
    void cancelBooking(int bookingId);
    public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService);
	

}
