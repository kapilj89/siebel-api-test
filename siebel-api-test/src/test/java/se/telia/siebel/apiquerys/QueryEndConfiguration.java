package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.configurator.*;
import com.siebel.ordermanagement.configurator.cfginteractrequest.*;
import com.siebel.ordermanagement.quote.data.Quote;
import se.telia.siebel.data.DataStorage;

public class QueryEndConfiguration {
    EndConfigurationPort endConfigurationPort;
    public QueryEndConfiguration(DataStorage dataStorage){
        ProductConfigurator productConfigurator = new ProductConfigurator();
        endConfigurationPort=productConfigurator.getEndConfigurationPort();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(endConfigurationPort);
    }

    public Quote endConfiguration() {
        EndConfigurationOutput endConfigurationOutput = endConfigurationPort.endConfiguration(buildEndConfigurationRequest());

        if (endConfigurationOutput.getErrorSpcCode() != null && endConfigurationOutput.getErrorSpcCode().length() > 0) {
            System.out.println("Error updating configuration\n"+
                    endConfigurationOutput.getErrorSpcCode() + "\n"+
                    endConfigurationOutput.getErrorSpcMessage());
            return null;
        }
        if (endConfigurationOutput.getListOfQuote().getQuote().size()!=1) {
            System.out.println("Wrong number of quotes returned from endConfiguration");
            return null;
        }
        return endConfigurationOutput.getListOfQuote().getQuote().get(0);
    }


    private EndConfigurationInput buildEndConfigurationRequest() {
        final EndConfigurationInput endConfigurationInput = new EndConfigurationInput();
        endConfigurationInput.setSaveInstanceFlag("Y");
        return endConfigurationInput;
    }

}
