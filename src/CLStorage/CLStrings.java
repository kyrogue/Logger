package CLStorage;

import java.io.File;
import java.util.Calendar;

public final class CLStrings {

	public static final String HOME_FOLDER =  System.getProperty("user.home")+File.separator;
	public static final String CL_FOLDER = HOME_FOLDER+"CivilianLogs"+File.separator;
	public static final String CL_FILE = "contractors.xlsx";

	public static final Calendar now = Calendar.getInstance();
	public static final String months[] ={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	public static final String yearNow = Integer.toString(now.get(Calendar.YEAR));
	public static final String mthNow = months[now.get(Calendar.MONTH)];
	public static final String dayNow = Integer.toString(now.get(Calendar.DAY_OF_MONTH));
	public static final String yearlyFolder = CLStrings.CL_FOLDER+yearNow+File.separator;
	public static final String monthlyFolder = CLStrings.CL_FOLDER+yearNow+File.separator+mthNow+File.separator;
	public static final String dailyExcelFile = monthlyFolder+dayNow+".xls";
}
