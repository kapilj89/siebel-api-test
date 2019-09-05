Feature: Sanity Test for (Secondary) SDU New BB ONLY

  @xDSLBroadband
  Scenario Outline: 
  Create New XDSL Broadband Only
  This scenario used create new broadband Only
  Note:AddressData format-  
     When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      #And call AddServiceAdress format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"    
      And call GetProductDetailsService using promotionCode "<promotionCode>" and get ProductId, PriceList
      And call ApplyProductPromotionService and get quote
      And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      And call BeginConfigurationService using product item name "<ServiceBundle>"
      And call Addproduct to set RelationShipName "<RelationShipName>" and Package "<Package>"
      And call EndConfigurationService and get Quote
      And call BB xDSLSynchronizeQuote to populate the AccessCode "<AccessCode>" and ConnRef number "<FbNumber>" on the quote line items for a xDSL order
      And call QuoteCheckOutService and get ActiveOrderID
      Then call SubmitOrder Service and Get successful OrderID
  
     Examples:

      | CustomerType | SSN          | AddressData                                    | promotionCode            | ServiceBundle            | RelationShipName             | Package                             | AccessCode | FbNumber   |
      | B2B          | 199902068510   | KORSÖRVÄGEN;7;C;MALMÖ;530-071-01;112820707;21747 | P-BIA-xDSL-BB Start-1827 | Bredbandsaccesser        | Bredbandsaccesser alternativ | BIA-xDSL BredbandStart 30-ADSL-1747 | 100        | FB26098428 |
  
