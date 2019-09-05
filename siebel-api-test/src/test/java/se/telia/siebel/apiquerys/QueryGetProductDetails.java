package se.telia.siebel.apiquerys;

import java.util.List;
import com.siebel.ordermanagement.catalog.CatalogWebService;
import com.siebel.ordermanagement.catalog.GetProductDetailsInput;
import com.siebel.ordermanagement.catalog.GetProductDetailsOutput;
import com.siebel.ordermanagement.catalog.data.productdetails.ListOfProduct;
import com.siebel.ordermanagement.catalog.data.productdetails.Product;

import se.telia.siebel.data.DataStorage;

public class QueryGetProductDetails {
	DataStorage dataStorage;
	CatalogWebService service;
	com.siebel.ordermanagement.catalog.CatalogPort catalogport;

	public QueryGetProductDetails(DataStorage dataStorage) {
		this.dataStorage = dataStorage;
		CatalogWebService service = new CatalogWebService();
		catalogport = service.getCatalogPort();
		new SiebelSoapCommunication(dataStorage).setupSoapCommunication(catalogport);
	}

	public Product getProductsDetails(String prodName) {

		Product prd = new Product();
		prd.setName(prodName);

		ListOfProduct LstProd = new ListOfProduct();
		List<Product> prod = LstProd.getProduct();
		prod.add(prd);

		GetProductDetailsInput getProddetailsIP = new GetProductDetailsInput();
		getProddetailsIP.setListOfProduct(LstProd);
		
		GetProductDetailsOutput prodDetailsOP = catalogport.getProductDetails(getProddetailsIP);
		Product product = prodDetailsOP.getListOfProduct().getProduct().get(0);
		return product;

	}
}
