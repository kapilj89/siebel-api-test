package se.telia.siebel;


import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class Report {

    public static void main(String args[]) {
        // mvn exec:java -Dexec.mainClass="se.telia.siebel.Report" -Dexec.classpathScope=test
        new Report().createReport();
    }
    public void createReport() {
        File reportOutputDirectory = new File("target/cucumber_report");
        List<String> jsonFiles = new ArrayList<>();
        URL url=getClass().getResource("/");
        String path=url.getPath();
        String fullPath=path+ "/../cucumber_report/siebel-api-test-cucumber_report.json";
        System.out.println("fullPath = " + fullPath);
        File f = new File(fullPath);
        if (f.exists()) {
            jsonFiles.add(fullPath);
        }

        String buildNumber = "1";
        String projectName = "cucumberProject";
        boolean runWithJenkins = false;
        boolean parallelTesting = false;

        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        // optional configuration
        configuration.setParallelTesting(parallelTesting);
        configuration.setRunWithJenkins(runWithJenkins);
        configuration.setBuildNumber(buildNumber);

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);

        Reportable result = reportBuilder.generateReports();

        System.out.println("Report is now here:\n ./target/cucumber_report/cucumber-html-reports/overview-features.html \n");

    }
}