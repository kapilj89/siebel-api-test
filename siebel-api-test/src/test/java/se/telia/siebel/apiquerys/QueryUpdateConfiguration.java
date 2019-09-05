package se.telia.siebel.apiquerys;

import com.siebel.ordermanagement.configurator.UpdateConfigurationInput;
import com.siebel.ordermanagement.configurator.UpdateConfigurationOutput;
import com.siebel.ordermanagement.configurator.UpdateConfigurationPort;
import com.siebel.ordermanagement.configurator.ProductConfigurator;
import com.siebel.ordermanagement.configurator.cfginteractrequest.Attribute;
import com.siebel.ordermanagement.configurator.cfginteractrequest.AttributeValue;
import com.siebel.ordermanagement.configurator.cfginteractrequest.Item;
import com.siebel.ordermanagement.configurator.cfginteractrequest.ListOfRequest;
import com.siebel.ordermanagement.configurator.cfginteractrequest.Request;
import com.siebel.ordermanagement.configurator.cfginteractrequest.Requests;
import se.telia.siebel.data.DataStorage;

import java.util.Map;

import static se.telia.siebel.apiquerys.SiebelUpdateConfigurationHelper.buildListOfDataStructure;


public class QueryUpdateConfiguration {

    // Update Configuration parameters
    public static final String FINISH_CONFIGURATION_IN_FLAG = "N";
    public static final String REPRICE_IN_FLAG = "Y";
    public static final String SAVE_INSTANCE_FLAG = "N";
    public static final String VERIFY_IN_FLAG = "N";
    public static final String CONFLICT_AUTO_RESOLVE = "Undo";
    public static final String GetExplanation="Y";
    
    UpdateConfigurationPort updateConfigurationPort;
    public QueryUpdateConfiguration(DataStorage dataStorage){
        ProductConfigurator productConfigurator = new ProductConfigurator();
        updateConfigurationPort=productConfigurator.getUpdateConfigurationPort();
        new SiebelSoapCommunication(dataStorage).setupSoapCommunication(updateConfigurationPort);
    }
    public boolean AddItem(String integrationId,String RelationshipID,String ProductID){
              UpdateConfigurationInput updateConfigurationInput = new UpdateConfigurationInput();
              ListOfRequest listOfRequest = new ListOfRequest();
              Requests requests = new Requests();
//            for (String key : attributeMap.keySet()){
//             String value = attributeMap.get(key);
             requests.getRequest().add(createRequestAdditem(integrationId,RelationshipID,ProductID));

//         }
//           requests.setType("AddItem");
         listOfRequest.setRequests(requests);
         updateConfigurationInput.setListOfRequest(listOfRequest);
         
         updateConfigurationInput.setListOfData(buildListOfDataStructure());
         updateConfigurationInput.setGetExplanation(GetExplanation);
         updateConfigurationInput.setFinishConfigurationInFlag(FINISH_CONFIGURATION_IN_FLAG);
         updateConfigurationInput.setRepriceInFlag(REPRICE_IN_FLAG);
         updateConfigurationInput.setSaveInstanceInFlag(SAVE_INSTANCE_FLAG);
         updateConfigurationInput.setVerifyInFlag(VERIFY_IN_FLAG);
         updateConfigurationInput.setConflictAutoResolve(CONFLICT_AUTO_RESOLVE);

         UpdateConfigurationOutput updateConfigurationOutput = updateConfigurationPort.updateConfiguration(updateConfigurationInput);
         if (updateConfigurationOutput.getErrorSpcCode() != null && updateConfigurationOutput.getErrorSpcCode().length() > 0) {
             System.out.println("Error updating configuration\n"+
                     updateConfigurationOutput.getErrorSpcCode() + "\n"+
                     updateConfigurationOutput.getErrorSpcMessage());
             return false;
         }
         return true;
    }
    public boolean UpdateItem(String integrationId,String RelationshipID,String ProductID,String ModifiedProductID){
        UpdateConfigurationInput updateConfigurationInput = new UpdateConfigurationInput();
        ListOfRequest listOfRequest = new ListOfRequest();
        Requests requests = new Requests();
//      for (String key : attributeMap.keySet()){
//       String value = attributeMap.get(key);
       requests.getRequest().add(createRequestReplaceItem(integrationId,RelationshipID,ProductID,ModifiedProductID));

//   }
//     requests.setType("AddItem");
   listOfRequest.setRequests(requests);
   updateConfigurationInput.setListOfRequest(listOfRequest);
   
   updateConfigurationInput.setListOfData(buildListOfDataStructure());
   updateConfigurationInput.setGetExplanation(GetExplanation);
   updateConfigurationInput.setFinishConfigurationInFlag(FINISH_CONFIGURATION_IN_FLAG);
   updateConfigurationInput.setRepriceInFlag(REPRICE_IN_FLAG);
   updateConfigurationInput.setSaveInstanceInFlag(SAVE_INSTANCE_FLAG);
   updateConfigurationInput.setVerifyInFlag(VERIFY_IN_FLAG);
   updateConfigurationInput.setConflictAutoResolve(CONFLICT_AUTO_RESOLVE);

   UpdateConfigurationOutput updateConfigurationOutput = updateConfigurationPort.updateConfiguration(updateConfigurationInput);
   if (updateConfigurationOutput.getErrorSpcCode() != null && updateConfigurationOutput.getErrorSpcCode().length() > 0) {
       System.out.println("Error updating configuration\n"+
               updateConfigurationOutput.getErrorSpcCode() + "\n"+
               updateConfigurationOutput.getErrorSpcMessage());
       return false;
   }
   return true;
}
    public boolean setAttributes(String integrationId,  Map<String,String> attributeMap) {
        UpdateConfigurationInput updateConfigurationInput = new UpdateConfigurationInput();

        ListOfRequest listOfRequest = new ListOfRequest();
        Requests requests = new Requests();

        for (String key : attributeMap.keySet()){
            String value = attributeMap.get(key);
            requests.getRequest().add(createRequest(integrationId, key, value));

        }
        listOfRequest.setRequests(requests);
        updateConfigurationInput.setListOfRequest(listOfRequest);
        updateConfigurationInput.setListOfData(buildListOfDataStructure());
        updateConfigurationInput.setFinishConfigurationInFlag(FINISH_CONFIGURATION_IN_FLAG);
        updateConfigurationInput.setRepriceInFlag(REPRICE_IN_FLAG);
        updateConfigurationInput.setSaveInstanceInFlag(SAVE_INSTANCE_FLAG);
        updateConfigurationInput.setVerifyInFlag(VERIFY_IN_FLAG);
        updateConfigurationInput.setConflictAutoResolve(CONFLICT_AUTO_RESOLVE);

        UpdateConfigurationOutput updateConfigurationOutput = updateConfigurationPort.updateConfiguration(updateConfigurationInput);
        if (updateConfigurationOutput.getErrorSpcCode() != null && updateConfigurationOutput.getErrorSpcCode().length() > 0) {
            System.out.println("Error updating configuration\n"+
                    updateConfigurationOutput.getErrorSpcCode() + "\n"+
                    updateConfigurationOutput.getErrorSpcMessage());
            return false;
        }
        return true;
    }

    private Request createRequest(String integrationId, String name, String displayValue) {
        Request request = new Request();
        request.setType("SetAttribute");
//        request.setType("AddItem");
        Item item = new Item();
        item.setIntegrationId(integrationId);
        request.getItem().add(item);
        Attribute attribute = new Attribute();
        attribute.setName(name);
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setDisplayValue(displayValue);
        attribute.getAttributeValue().add(attributeValue);
        request.getAttribute().add(attribute);




        return request;
    }

    private Request createRequestAdditem(String integrationId, String RelationshipId, String AddProductId) {
              String Quantity="1";
        Request request = new Request();
//        request.setType("SetAttribute");

            request.setType("AddItem");
        Item item = new Item();
        item.setIntegrationId(integrationId);
        item.setRelationshipId(RelationshipId);
        item.setQuantity(Quantity);
        item.setAddProductId(AddProductId);
        request.getItem().add(item);
        return request;
    }

    private Request createRequestReplaceItem(String integrationId, String RelationshipId, String RemoveProductId,String AddProductId) {
        String Quantity="1";
  Request request = new Request();
//  request.setType("SetAttribute");
  request.setType("ReplaceItem");
  Item item = new Item();
  item.setIntegrationId(integrationId);
  item.setRelationshipId(RelationshipId);
  item.setRemoveProductId(RemoveProductId);
  item.setQuantity(Quantity);
  item.setAddProductId(AddProductId);
  request.getItem().add(item);
  return request;
}
}
