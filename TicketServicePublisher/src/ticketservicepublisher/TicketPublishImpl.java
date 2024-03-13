package ticketservicepublisher;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.mtit.databaseconnectionservice.DatabaseConnectionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TicketPublishImpl implements TicketPublish{

	//Initialising the variables 
	private DatabaseConnectionService databaseConnectionService;
	private Connection connection;
	
	//Array lists to get relevant data 
	ArrayList<Integer> eventIdList = new ArrayList<>();
	ArrayList<String> eventNameList = new ArrayList<>();
	ArrayList<Integer> sectionIdList = new ArrayList<>();
	ArrayList<Integer> sectionTicketList = new ArrayList<>();
	ArrayList<String> sectionNameList = new ArrayList<>();

	//establish database connection
	@Override
	public void setDatabaseConnectionService(DatabaseConnectionService databaseConnectionService) {
		this.databaseConnectionService = databaseConnectionService;
	}
	
	//get events
	@Override
	public void getEvents() {
		
		int count = 0;
		
		if (databaseConnectionService == null) {
			System.err.println("DatabaseConnectionService is not set.");
		}

		connection = databaseConnectionService.getConnection();
		if (connection == null) {
			System.err.println("Database connection is null.");
		}
		
		//get events from DB
		try (PreparedStatement preparedStatement = connection
				.prepareStatement("SELECT * FROM events")) {
						
			ResultSet resultset = preparedStatement.executeQuery();
			
			//loop through the events
			while (resultset.next()) {
			    count++;
			    String eventName = resultset.getString("name");
			    int eventId = resultset.getInt("id");

			    eventIdList.add(eventId);
			    eventNameList.add(eventName);

			    System.out.printf("%-3d. %-12s | Event ID: %-5d%n", count, eventName, eventId);
			}

			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	//Ticket publish function
	@Override
	public void publishTickets(int selectedEvent, int sections, int tickets) {
		
		//check if the event already has tickets
		if(sectionTicketAvalabiity(selectedEvent)) {
			System.out.println("Tickets have already been assigned to this event");
			System.exit(0);
		}
		    
		 if (selectedEvent >= 1 && selectedEvent <= eventNameList.size()) {
			 System.out.printf("%nYou have selected: %s%n", eventNameList.get(selectedEvent - 1));
			 System.out.printf("Available Tickets:%n");

			 //loop through the sections
			 for (int j = 0; j < sections; j++) {
			     char letter = (char) ('A' + j);
			     System.out.printf("Section %c with %d Tickets", letter, tickets);
			     addSection("Section " + letter, tickets, eventIdList.get(selectedEvent - 1));
			 }
		     
			 //show total amount of tickets
			 int totalTickets = tickets * sections;
			 System.out.println("Total Tickets Available: " + totalTickets);
			 updateTicketCount(1, tickets * sections, tickets * sections, eventIdList.get(selectedEvent - 1));
		                    
		 } else {
			 System.out.println("Invalid input. Please enter a valid event.");
		 }
		      
	}
	
	//Function to purchase tickets
	@Override
	public void purchaseTickets(int selectedEvent, int amount) {
		//get section details and related event details
		try (PreparedStatement preparedStatement = connection
				.prepareStatement("select * from osgi_event_ticket_booking_system.section s left join osgi_event_ticket_booking_system.events t ON s.EventID = t.id WHERE s.EventID = ?")) {
			
			preparedStatement.setInt(1, eventIdList.get(selectedEvent - 1));
			
			//execute the query
			ResultSet resultset = preparedStatement.executeQuery();
			Scanner scanner = new Scanner(System.in);
			Random random = new Random();
			
			while (resultset.next()) {
				//getting related data
			    String sectionName = resultset.getString("Section");
			    int sectionId = resultset.getInt("SectionId");
			    int noOfTickets = resultset.getInt("NoOfTickets");
			    int price = resultset.getInt("ticket_prize");
			    
			    //check if the section has enough tickets
			    if(noOfTickets >= amount) {
			    	
			    	//print availability
			    	System.out.println("Tickets are available");
			    	System.out.println("Ticket Price: " + price);
			    	System.out.println("Total Price: " + price * amount);
			    	
			    	//get confirmation
			    	System.out.println("Do you wish to continue (y/n)?");
			    	String tickets = scanner.next();
			    	
			    	if (tickets.equals("n")) {
			    	    System.out.println("Thank you, have a nice day!");
			    	    System.exit(0);
			    	} else if (!tickets.equals("y")) {
			    	    System.out.println("Invalid input, please try again. ");
			    	    System.exit(0);
			    	}
			    	
			    	//update tickets tables
					try (PreparedStatement updateStatement = connection
							.prepareStatement("Update section SET NoOfTickets = ? Where SectionId = ?")) {
						
							updateStatement.setInt(1, noOfTickets - amount);
							updateStatement.setInt(2, sectionId);
							
							//execute statement
							int rowsUpdated = updateStatement.executeUpdate();
							
							//print ticket id's
							if (rowsUpdated > 0) {
								updateTicketCount(2, 0, amount,eventIdList.get(selectedEvent - 1));
								System.out.println("=======================================");
								System.out.println("        Transaction Successful        ");
								System.out.println("=======================================");
								System.out.println("           Your tickets are:           ");
								for(int i = 0; i < amount; i++) {
									//generating ticket id's
									String ticketId = sectionName.charAt(sectionName.length() - 1) + " " + (10000 + random.nextInt(90000));
									System.out.printf("           Ticket %d: %-20s%n", (i + 1), ticketId);
									
									//insert ticket id's to ticket table
									try (PreparedStatement insertStatement = connection
											.prepareStatement("INSERT INTO tickets(TicketID,EventID) VALUE(?,?)")) {
										
										insertStatement.setString(1, ticketId);
										insertStatement.setInt(2, eventIdList.get(selectedEvent - 1));
										int rowsInserted = insertStatement.executeUpdate();
	
										
									} catch (SQLException e) {
										e.printStackTrace();
									}
									
								}
								
								System.out.println("=======================================");
							
							} else {
								System.err.println("Transaction unsuccessful");
							}
							
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					break;
			    	
			    }

			}
			
			
		}  catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	//check section availability
	@Override
	public boolean sectionTicketAvalabiity(int eventId) {
		try (PreparedStatement preparedStatement = connection
				.prepareStatement("SELECT * FROM section WHERE EventID = ?")) {
			
			preparedStatement.setInt(1, eventId);
			
			//execute statement
		    try (ResultSet resultSet = preparedStatement.executeQuery()) {
		       return resultSet.next();
		     }
			
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	//add sections
	public void addSection( String sectionName, int noOfTickets, int eventId){
			    
		try (PreparedStatement preparedStatement = connection
				.prepareStatement("INSERT INTO Section(Section,NoOfTickets,EventID) VALUE(?,?,?)")) {
			
			preparedStatement.setString(1, sectionName);
			preparedStatement.setInt(2, noOfTickets);
			preparedStatement.setInt(3, eventId);
			
			//execute statement
			int rowsInserted = preparedStatement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("✅︎");
			} else {
				System.err.println("❎");
			}
			
		}  catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	//Update ticket count
	public void updateTicketCount(int type, int total, int available, int eventId) {
		
		//if needed to be insert will insert else will update
		if(type == 1) {
			try (PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO ticketcount(TotalCount,Available,EventID,Type) VALUE(?,?,?,?)")) {
				
				preparedStatement.setInt(1, total);
				preparedStatement.setInt(2, available);
				preparedStatement.setInt(3, eventId);
				preparedStatement.setString(4, "Ticket");
				
				//execute statement
				int rowsInserted = preparedStatement.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("Tickets added successfully.");
				} else {
					System.err.println("Failed to add Tickets.");
				}
				
			}  catch (SQLException e) {
				e.printStackTrace();
			}	
			
		} else {
			try (PreparedStatement preparedStatement = connection
					.prepareStatement("Update ticketcount Set Available = Available - ? Where EventID = ?")) {
				
				preparedStatement.setInt(1, available);
				preparedStatement.setInt(2, eventId);
				int rowsInserted = preparedStatement.executeUpdate();
				
				//execute statement
				if (rowsInserted > 0) {
					System.out.println("Updated successfully.");
				} else {
					System.err.println("Failed to add Update.");
				}
				
			}  catch (SQLException e) {
				e.printStackTrace();
			}	
			
		}
		
	}
	
}
