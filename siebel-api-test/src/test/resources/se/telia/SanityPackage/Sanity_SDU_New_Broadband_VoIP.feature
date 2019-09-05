Feature: Sanity Test for(Primary&Secondary) SDU New BB+VOIP

@SanitySDUBroadbandVOIP
  Scenario Outline: 
  Create New Broadband and VOIP order
  This scenario used create new broadband and VOIP order
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
      And call GetProductDetailsService using promotionCode "<promotionCodeVOIP>" and get ProductId, PriceList
      And call QuoteAddItems and get quote
      And call BeginConfigurationService using product item name "<ServiceBundleVOIP>"
      And call EndConfigurationService and get Quote
      And call SDU BBVOIP SynchronizeQuote to populate the AccessCode "<AccessCode>" on the quote line items for a SDU order
      And call QuoteCheckOutService and get ActiveOrderID
     Then call SubmitOrder Service and Get successful OrderID
  
    Examples: 
      | CustomerType | SSN          | AddressData                                              | promotionCode | ServiceBundle | RelationShipName | Package | promotionCodeVOIP | ServiceBundleVOIP | AccessCode | 
		  | B2C          | 199811012567 | BÅBERG;414;;TROLLHÄTTAN;VILLA;145863005;46191        | P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1       | IA-Bredband 100/100-1703          | VoIP SE_Promo_Offer_1743             | VoIP SE Service bundle_1606     | 200        | 
#		  | B2B          | 5565555009   | BÅBERG;250;;TROLLHÄTTAN;VILLA;150781154;46191    | P-BIA-OF-BB Start-1827    | Bredbandsaccesser        | Bredbandsaccesser alternativ | BIA-OF BredbandStart 100/100-1747 | VoIP SE_B2B_0Months_Promo_Offer_1649 | VoIP SE_B2B Service bundle_1649 | 200        | 
      #| B2B          | 5565082756   | KÅLFJÄRILSVÄGEN;8;;TROLLHÄTTAN;VILLA;143882806;46165    | P-BIA-OF-BB Start-1827 | Bredbandsaccesser | Bredbandsaccesser alternativ | BIA-OF BredbandStart 100/100-1747 | VoIP SE_B2B_0Months_Promo_Offer_1649 | VoIP SE_B2B Service bundle_1649 | 200 | 
  
  
