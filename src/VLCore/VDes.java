package VLCore;

public final class VDes {

	private final String description;
	


public VDes(String d)
{
	this.description = d;
}

public String getDescription() {
	return description;
}

public static VDes valueOf(String s)
{
	return new VDes(s);
}

public String toString()
{
	return description;
}

}
