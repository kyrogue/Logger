package CLCore;

public class CVEntry{
	
	private String name;
	private String nirc;
	private String passport;
	private String passNo;
	private String vehPassNo;
	private String timeIn;
	private String timeOut;
	public CVEntry()
	{
		
	}
	public CVEntry(String n,String nr,String p)
	{
		
		this.name = n;
		this.nirc = nr;
		this.passport = p;
	}

	public CVEntry(String n,String nr,String p,String pNo,String vPNo,String tIn,String tOut)
	{
		
		this.name = n;
		this.nirc = nr;
		this.passport = p;
		this.passNo = pNo;
		this.vehPassNo = vPNo;
		this.timeIn = tIn;
		this.timeOut = tOut;
	}
	
	public String getPassNo() {
		return passNo;
	}
	public void setPassNo(String passNo) {
		this.passNo = passNo;
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

	public String getVehPassNo() {
		return vehPassNo;
	}
	public void setVehPassNo(String vehPassNo) {
		this.vehPassNo = vehPassNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNirc() {
		return nirc;
	}
	public void setNirc(String nirc) {
		this.nirc = nirc;
	}
	public String getPassport() {
		return passport;
	}
	public void setPassport(String passport) {
		this.passport = passport;
	}
	
}
