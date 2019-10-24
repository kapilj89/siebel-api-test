package se.telia.siebel;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/test/resources/se/telia/SanityPackage/Fixed"},
		tags = {"@SanitySDUBroadband,@SanitySDUIPTV,@SanitySDUBroadbandIPTV,@SanitySDUBroadbandVOIP,"
				+ "@SanityNewMDUBBIPTVVoIP,@SanityxDSLBroadband,@SanityxDSLBroadbandIPTVVOIP"},
		        plugin = {"pretty",
		                "json:target/cucumber_report/siebel-api-test-cucumber_report.json"
		                 }
		
//		features = {"src/test/resources/se/telia/SanityPackage",
//				},
//        tags = {"@SanitySDUBroadband","@SanityNewMDUBB"},
////        glue ={"se.telia.siebel.stepdefs"},
//        plugin = {"pretty",
//                "json:target/cucumber_report/siebel-api-test-cucumber_report.json"
//                 }
		)
        //      dryRun = true,
        
        
//tags = {"@one"},
public class RunCukesTest_Sanity {
}
