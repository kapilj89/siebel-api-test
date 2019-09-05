package se.telia.siebel.apiquerys;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerateQuoteNumber {

	public static String getGeneratedQuoteNumber()  {
		// String quoteNUmber = "APITEST"+ new
		// SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());

		String my_file_name = "\\\\131.116.164.238\\e\\API_Orderlog.txt";
		// create instance of Random class
		int startupOrder = 5;
		long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		System.out.println(number);
		final String ordernumber = startupOrder + "-" + number;
		System.out.println(ordernumber);
		try {
			FileInputStream fstream = new FileInputStream(my_file_name);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;

			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
			  // Print the content on the console
//			  System.out.println (strLine);
			}
			fstream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(my_file_name, true), "UTF-8"));
			String s = new Timestamp(System.currentTimeMillis()) + "  API ORDER  " + ordernumber + System.lineSeparator();
			writer.append(s);
			// output.append(ordernumber);
			writer.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ordernumber;
		// return quoteNUmber;
	}
}
