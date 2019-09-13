Feature: Sanity Test for (Secondary) SDU New BB ONLY

  @SanityxDSLBroadband
  Scenario Outline: 
  Create New XDSL Broadband Only
  This scenario used create new broadband Only
  Note:AddressData format-  
     When call SelfServiceUser to get primary organization id which is used in quote creation
      #And call QueryCustomer using SSN "<SSN>" to get account and billing details
      #And call QuerySelfServiceAddress Add/Update for XDSL format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"
      #And call GetProductDetailsService using promotionCode "<promotionCode>" and get ProductId, PriceList
      #And call ApplyProductPromotionService and get quote
      #And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      #And call BeginConfigurationService using product item name "<ServiceBundle>"
      #And call Addproduct to set RelationShipName "<RelationShipName>" and Package "<Package>"
      #And call EndConfigurationService and get Quote
      #And call BB xDSLSynchronizeQuote to populate the AccessCode "<AccessCode>" and ConnRef number "<FbNumber>" on the quote line items for a xDSL order
      #And call QuoteCheckOutService and get ActiveOrderID
      #Then call SubmitOrder Service and Get successful OrderID
  #
    Examples: 
      | CustomerType | SSN          | AddressData                                    | promotionCode            | ServiceBundle            | RelationShipName             | Package                             | AccessCode | FbNumber   | 
      | B2B          | 199811085589    |RASMUSGATAN;3;B;MALMÖ;763-260-01;112936009;21446 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1   | OptGrp-IA-speed-1703-1 | IA-Bredband 10-1722 | 100        | FB26098780 | 
  
  
