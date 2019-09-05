package se.telia.siebel.apiquerys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    	Date today = new Date();
        Date today1 = new Date(today.getTime());

    	SimpleDateFormat siebelDateFormat = new SimpleDateFormat(FORMAT);
         Calendar cl = Calendar.getInstance();
			cl.setTime(today1);
			cl.add(Calendar.MINUTE,20);
			String newtime = siebelDateFormat.format(cl.getTime());
			System.out.println(newtime); 
			return newtime;
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
