package CLCore;

public class CVDataEntry {

	private String purpose;
	private String name;
	private String nirc;
	private String passport;
	private String nationality;
	private String company;
	private String misc;
	public CVDataEntry() 
	{
		
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

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getMisc() {
		return misc;
	}

	public void setMisc(String misc) {
		this.misc = misc;
	}
	
}
