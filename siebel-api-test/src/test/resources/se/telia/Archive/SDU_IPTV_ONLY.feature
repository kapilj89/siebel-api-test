Feature: Sanity Test for(secondary) SDU New IPTV

  @SDUIPTV
  Scenario Outline: 
  Create New IPTV order
  This scenario used create new IPTV order
  Note:AddressData format-
  
     When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      And call AddressAddorupdate with format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"    
      And call GetProductDetailsService using promotionCode "<promotionCodeIPTV>" and get ProductId, PriceList
      And call ApplyProductPromotionService and get quote
      And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      And call BeginConfigurationService using product item name "Service Bundle-TV-IPTV-1704"
      And call Addproduct to set RelationShipName "<RelationShipTV>" and Package "<PackageTV>"
      And call EndConfigurationService and get Quote
   		And call SDU IPTV SynchronizeQuote to populate the AccessCode "<AccessCode>" on the quote line items for a SDU order
      And call QuoteCheckOutService and get ActiveOrderID
      Then call SubmitOrder Service and Get successful OrderID
  
   Examples: 
      | CustomerType | SSN          | AddressData                                          |  promotionCodeIPTV       | RelationShipTV               | PackageTV     | AccessCode |
      | B2C          | 199803237438 | LERBÄCKEVÄGEN;18;;TROLLHÄTTAN;VILLA;114343063;46165  |  P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 | 200        |

 
  
    

