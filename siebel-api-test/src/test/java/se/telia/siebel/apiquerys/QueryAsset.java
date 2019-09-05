package se.telia.siebel.apiquerys;

import com.siebel.service.fs.assets.AssetManagementComplex;
import com.siebel.service.fs.assets.AssetManagementComplexPort;
import com.siebel.service.fs.assets.AssetManagementComplexQueryPageInput;
import com.siebel.service.fs.assets.AssetManagementComplexQueryPageOutput;
import com.siebel.xml.asset_management_complex_io.data.AssetMgmtAssetHeaderData;
import com.siebel.xml.asset_management_complex_io.query.AssetMgmtAssetHeaderQuery;
import com.siebel.xml.asset_management_complex_io.query.ListOfAssetQuery;
import com.siebel.xml.asset_management_complex_io.query.QueryType;
import se.telia.siebel.data.DataStorage;

import java.util.List;

public class QueryAsset {
    DataStorage dataStorage ;
    AssetManagementComplex service;
    AssetManagementComplexPort port;
    public QueryAsset(DataStorage dataStorage){
        this.dataStorage=dataStorage;

         service = new AssetManagementComplex();
         port=service.getAssetManagementComplexPort();
         // Add an outgoing interceptor (that will add login/session-information to the SOAP requests)
         new SiebelSoapCommunication(dataStorage).setupSoapCommunication(port);

    }

    public AssetMgmtAssetHeaderData getAssetMgmtAssetHeaderData(String ssn, String promotionName){
        AssetManagementComplexQueryPageInput assetManagementComplexQueryPageInput = new AssetManagementComplexQueryPageInput();
        assetManagementComplexQueryPageInput.setLOVLanguageMode("LDC");
        assetManagementComplexQueryPageInput.setExecutionMode("ForwardOnly");
        assetManagementComplexQueryPageInput.setViewMode("All");
        AssetMgmtAssetHeaderQuery assetMgmtAssetHeaderQuery = new AssetMgmtAssetHeaderQuery();
        assetMgmtAssetHeaderQuery.setAccountLocation(string2QueryType("='"+ssn+"'"));
        assetMgmtAssetHeaderQuery.setStatus(string2QueryType("='Active'"));
        assetMgmtAssetHeaderQuery.setAssetNumber(string2QueryType("")); // Need to send in empty fields or else they will not be listed in the response
        assetMgmtAssetHeaderQuery.setServiceAccountId(string2QueryType(""));
        assetMgmtAssetHeaderQuery.setProductId(string2QueryType(""));
        assetMgmtAssetHeaderQuery.setProductName(string2QueryType(""));
        ListOfAssetQuery listOfAssetQuery = new ListOfAssetQuery();
        listOfAssetQuery.setAssetMgmtAssetHeader(assetMgmtAssetHeaderQuery);
        assetManagementComplexQueryPageInput.setListOfAsset(listOfAssetQuery);
        AssetManagementComplexQueryPageOutput assetOut =
                port.assetManagementComplexQueryPage(assetManagementComplexQueryPageInput);

        List<AssetMgmtAssetHeaderData> assetMgmtAssetHeader =assetOut.getListOfAsset().getAssetMgmtAssetHeader();

        for (AssetMgmtAssetHeaderData assetMgmtAssetHeaderData: assetMgmtAssetHeader) {
            if (promotionName.equals(assetMgmtAssetHeaderData.getProductName())) {
                return assetMgmtAssetHeaderData;
            }
        }
        System.out.println("No assets found in getAssetMgmtAssetHeaderData.");
        return null;
    }
    public static QueryType string2QueryType(String s) {
        QueryType q = new QueryType();
        q.setValue(s);
        return q;
    }

}
