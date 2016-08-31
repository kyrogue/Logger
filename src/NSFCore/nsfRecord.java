package NSFCore;

import java.text.SimpleDateFormat;

public class nsfRecord {

	private String name;
	private String nric;
	private String location;
	private String contact;
	private String remarks;
	private String timeIn;
	private String timeOut;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(" H:mm:ss");
	public nsfRecord()
	{
		
	}
	public nsfRecord(String name,String nric)
	{
		this.name = name;
		this.nric = nric;
	}
	public nsfRecord(String nric)
	{
		this.nric = nric;
	}

	public nsfRecord(String name, String nric, String location, String contact,
			String remarks, String timeIn, String timeOut) {
		super();
		this.name = name;
		this.nric = nric;
		this.location = location;
		this.contact = contact;
		this.remarks = remarks;
		this.timeIn = timeIn;
		this.timeOut = timeOut;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNric() {
		return nric;
	}
	public void setNric(String nric) {
		this.nric = nric;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getTimeIn() {
		return timeIn;
	}
	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}
	public String getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	
	
}
