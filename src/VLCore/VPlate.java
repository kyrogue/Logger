package VLCore;

public final class VPlate {

	private final String plateNo;
	
public String getPlate() {
		return plateNo;
	}

public VPlate(String p)
{
	this.plateNo = p;
}

public static VPlate valueOf(String s)
{
	return new VPlate(s);
}

public String toString()
{
	return plateNo;
}

}
