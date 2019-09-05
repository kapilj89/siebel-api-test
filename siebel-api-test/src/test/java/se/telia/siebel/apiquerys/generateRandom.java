package se.telia.siebel.apiquerys;

import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;

public class generateRandom {
              public static void main(String args[]) throws IOException 
    { 
                             String my_file_name="\\131.116.164.238\\e\\API_Orderlog.txt";
                             // create instance of Random class
                             int startupOrder=5;
        long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
//        System.out.println(number);
        final String ordernumber=startupOrder+"-"+number;
        System.out.println(ordernumber);
        Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(my_file_name, true), "UTF-8"));
        String s = new Timestamp(System.currentTimeMillis())+"  API ORDER  "+ordernumber+System.lineSeparator() ;
        writer.append(s);
//        output.append(ordernumber);
        writer.close();

    }
}
