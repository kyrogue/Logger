package VLCore;

public class CarEntry {

	private String plate ="";
	private String description="";
	private String timeIn="";
	private String timeOut="";

	public CarEntry()
	{
		
	}
public CarEntry(String no,String des,String in,String out)
{
	
this.plate = no;
this.description = des;
this.timeIn = in;
this.timeOut = out;

}

public String getPlate() {
	return plate;
}

public void setPlate(String plate) {
	
	this.plate = plate;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
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
