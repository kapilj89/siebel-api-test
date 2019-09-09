Feature: Sanity Test for (Secondary) SDU New BB ONLY

@SanitySDUBroadband
  Scenario Outline: 
  Create New Broadband Only
  This scenario used create new broadband Only
  Note:AddressData format-
  
     When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
  		And call AddressAddorupdate with format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"    
      And call GetProductDetailsService using promotionCode "<promotionCode>" and get ProductId, PriceList
      And call ApplyProductPromotionService and get quote
      And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      And call BeginConfigurationService using product item name "<ServiceBundle>"
      And call Addproduct to set RelationShipName "<RelationShipName>" and Package "<Package>"
      And call EndConfigurationService and get Quote
      And call SDU BB SynchronizeQuote to populate the AccessCode "<AccessCode>" on the quote line items for a SDU order
      And call QuoteCheckOutService and get ActiveOrderID
     Then call SubmitOrder Service and Get successful OrderID
  
    Examples: 
      | CustomerType | SSN          | AddressData                                                    | promotionCode             | ServiceBundle            | RelationShipName             | Package                           | AccessCode |
      #| B2C          | 193401124510   | ENGELBREKTSGATAN;27;B;YSTAD;VILLA;112838759;73232         | P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1       | IA-Bredband 100/100-1703          | 200        | 
      #| B2C          | 199810264714 | HASSELKULLEGATAN;98;;TROLLHÄTTAN;VILLA;130396989;46162         | P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1       | IA-Bredband 100/100-1703          | 200        | 
      | B2C          | 199811014654 | SUNNANGRÄND;3;B;ALFTA;201;116502968;82230         | P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1       | IA-Bredband 100/100-1703          | 200        | 
      #| B2B          | 5565603064   | SUNNANGRÄND;10;D;ALFTA;102;116502954;82230         | P-BIA-OF-BB Start-1827    | Bredbandsaccesser        | Bredbandsaccesser alternativ | BIA-OF BredbandStart 100/100-1747 | 200        | 
  #
  
