Feature: New MDU BroadBand order

  @NewMDUBB
    Scenario Outline: Create New MDU BroadBand
    This scenario used create new MDU BroadBand 

     When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      #And call AddServiceAdress format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"    
      And call GetProductDetailsService using promotionCode "<promotionCode>" and get ProductId, PriceList
      And call ApplyProductPromotionService and get quote
      And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      And call BeginConfigurationService using product item name "<ServiceBundle>"
      And call Addproduct to set RelationShipName "OptGrp-IA-speed-1703-1" and Package "IA-Bredband 100/100-1703"
      And call EndConfigurationService and get Quote
      And call SynchronizeQuote to populate the deliveryContract "<Agreement>" on the quote line items for a MDU order
      And call QuoteCheckOutService and get ActiveOrderID
      Then call SubmitOrder Service and Get successful OrderID
  
    Examples: 
      | CustomerType | SSN          | AddressData                                    | promotionCode             | ServiceBundle            | RelationShipName       | Package                  | Agreement    | 
      | B2C          | 194012181618 | MUNKHÄTTEGATAN;30;B;MALMÖ;1505;121961560;21456 | P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 100/100-1703 | 1-9598007958 | 
  
