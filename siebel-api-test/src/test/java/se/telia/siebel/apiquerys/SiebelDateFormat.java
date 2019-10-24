package se.telia.siebel.apiquerys;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

public class SiebelDateFormat {

    private static String FORMAT = "MM/dd/yyyy HH:mm:ss";
//    private static String FORMAT1 = "yyyy-MM-dd HH:mm:ss";
    public static String siebelDateFormat(String d) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(d);
            SimpleDateFormat siebelDateFormat = new SimpleDateFormat(FORMAT);
            return siebelDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String siebelDateFormat(Date date) {
        SimpleDateFormat siebelDateFormat = new SimpleDateFormat(FORMAT);
        return siebelDateFormat.format(date);
    }

    public static String getTomorrowsDate() {
    	Date today = new Date();
        Date today1 = new Date(today.getTime());

    	SimpleDateFormat siebelDateFormat = new SimpleDateFormat(FORMAT);
         Calendar cl = Calendar.getInstance();
			cl.setTime(today1);
			cl.add(Calendar.MINUTE,-210);
			String newtime = siebelDateFormat.format(cl.getTime());
			System.out.println(newtime); 
			return newtime;
    }
    
//    public static String getDate() {
//    	SimpleDateFormat siebelDateFormat = new SimpleDateFormat(FORMAT);
//        Calendar currentDate = Calendar.getInstance();
//		String dueDate = siebelDateFormat.format(currentDate.getTime());
//        return dueDate;
//    }
	public static String getCETtime() {

		Instant zulu = Instant.now(); // GMT, UTC+0
		ZonedDateTime zdt = zulu.atZone(ZoneId.of("Europe/Paris"));
		Date date1 = Timestamp.valueOf(zdt.toLocalDateTime());
		System.out.println(date1);
		// Date D=;
		SimpleDateFormat siebelDateFormat = new SimpleDateFormat(FORMAT);
		String finalCETDate = siebelDateFormat.format(date1);
		System.out.println(finalCETDate);
		return finalCETDate;
	}
    public static String getCurentCETtime() {
    	Date today = new Date();
        Date today1 = new Date(today.getTime());

    	SimpleDateFormat siebelDateFormat = new SimpleDateFormat(FORMAT);
         Calendar cl = Calendar.getInstance();
			cl.setTime(today1);
			cl.add(Calendar.MINUTE,5);
			String newtime = siebelDateFormat.format(cl.getTime());
			System.out.println(newtime); 
			return newtime;
			}

    
    public static String BBDueDate() {
        SimpleDateFormat siebelDateFormat = new SimpleDateFormat(FORMAT);
        Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 15);
		String BBDueDate = siebelDateFormat.format(cal.getTime());
        
        return BBDueDate;
    }
    public static String MobileDueDate() {
    	
    	DateTimeZone timeZone = DateTimeZone.forID( "Europe/Paris" );
    	DateTime now = new DateTime( timeZone ); // Or, for default time zone: new DateTime()
    	DateTime firstDayofNextMonth = now.plusMonths(1).dayOfMonth().withMinimumValue().withTimeAtStartOfDay();
    	Date date = firstDayofNextMonth.toDate();

    	SimpleDateFormat siebelDateFormat = new SimpleDateFormat(FORMAT);
		String mobileDueDate = siebelDateFormat.format(date);
		System.out.println(mobileDueDate);
		return mobileDueDate;
	}

    
    public static Date converteddate() throws Exception{
    	Date myDate = new Date();
    	System.out.println(myDate);
    	System.out.println(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(myDate));
//    	System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(myDate));
    	String D=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(myDate);
    	Date d1=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(D); 
    	System.out.println(d1);
		return d1;
    	
    }
}
