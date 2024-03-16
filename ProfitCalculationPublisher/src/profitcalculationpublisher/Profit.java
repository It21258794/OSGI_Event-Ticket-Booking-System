package profitcalculationpublisher;

public class Profit {

	private int calId;
	private int eventId;
	private String event_date;
	private double income;
	private double budget;
	private double profit;
	private double loss;
	/**
	 * @return the calId
	 */
	public int getCalId() {
		return calId;
	}
	/**
	 * @param calId the calId to set
	 */
	public void setCalId(int calId) {
		this.calId = calId;
	}
	/**
	 * @return the eventId
	 */
	public int getEventId() {
		return eventId;
	}
	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String getEvent_date() {
		return event_date;
	}
	/**
	 * @param eventId the eventId to set
	 */
	public void setEventDate(String event_date) {
		this.event_date = event_date;
	}
	/**
	 * @return the income
	 */
	public double getIncome() {
		return income;
	}
	/**
	 * @param income the income to set
	 */
	public void setIncome(double income) {
		this.income = income;
	}
	/**
	 * @return the budget
	 */
	public double getBudget() {
		return budget;
	}
	/**
	 * @param budget the budget to set
	 */
	public void setBudget(double budget) {
		this.budget = budget;
	}
	/**
	 * @return the profit
	 */
	public double getProfit() {
		return profit;
	}
	/**
	 * @param profit the profit to set
	 */
	public void setProfit(double profit) {
		this.profit = profit;
	}
	
	public double getLoss() {
		return loss;
	}
	/**
	 * @param profit the profit to set
	 */
	public void setLoss(double loss) {
		this.loss = loss;
	}
		
	
	}
