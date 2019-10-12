package se.telia.siebel.apiquerys;

import java.util.List;
import com.siebel.ordermanagement.catalog.CatalogWebService;
import com.siebel.ordermanagement.catalog.GetProductDetailsInput;
import com.siebel.ordermanagement.catalog.GetProductDetailsOutput;
import com.siebel.ordermanagement.catalog.data.productdetails.ListOfProduct;
import com.siebel.ordermanagement.catalog.data.productdetails.Product;

import se.telia.siebel.data.DataStorage;

public class QueryGetProductPromotionDetails {
	DataStorage dataStorage;
	CatalogWebService service;
	com.siebel.ordermanagement.catalog.CatalogPort catalogport;

	public QueryGetProductPromotionDetails(DataStorage dataStorage) {
		this.dataStorage = dataStorage;
		CatalogWebService service = new CatalogWebService();
		catalogport = service.getCatalogPort();
		new SiebelSoapCommunication(dataStorage).setupSoapCommunication(catalogport);
	}

	public List<Product> getProductsDetails(String prodName, String additionalService) {

		ListOfProduct LstProd = new ListOfProduct();

		Product prd = new Product();
		prd.setName(prodName);
		List<Product> prod = LstProd.getProduct();
		prod.add(prd);

		Product additionalProduct = new Product();
		additionalProduct.setName(additionalService);
		List<Product> addtionalProd = LstProd.getProduct();
		addtionalProd.add(additionalProduct);

		GetProductDetailsInput getProddetailsIP = new GetProductDetailsInput();
		getProddetailsIP.setListOfProduct(LstProd);

		GetProductDetailsOutput prodDetailsOP = catalogport.getProductDetails(getProddetailsIP);
		List<Product> product = prodDetailsOP.getListOfProduct().getProduct();
		return product;

	}
}
