package NSFCore;

import java.io.File;

public final class nsfConstantStrings {
	public static final String WINDOWS_HOME_FOLDER =  System.getProperty("user.home")+File.separator;
	public static final String NSF_FOLDER =  WINDOWS_HOME_FOLDER+"NSFLogs"+File.separator;
	public static final String NSF_DB_ACESS_FILE =  WINDOWS_HOME_FOLDER+File.separator+"NSFDatabase.mdb";
	public static final String LOCATIONS_TXT_FILE = NSF_FOLDER+"locations.txt";
	public static final String REMARKS_TXT_FILE = NSF_FOLDER+"remarks.txt";
}
