Feature: Sanity Test for (Primary&Secondary) SDU New BB ONLY

  @xDSLBroadbandIPTVVOIP
  Scenario Outline: 
  Create New XDSL Broadband IPTV and VOIP
  This scenario used create new broadband IPTV and VOIP
  Note:AddressData format-  
     When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      And call AddressAddorupdate with format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"
      #And call AddServiceAdress format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"    
      And call GetProductDetailsService using promotionCode "<promotionCode>" and get ProductId, PriceList
      And call ApplyProductPromotionService and get quote
      And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      And call BeginConfigurationService using product item name "<ServiceBundle>"
      And call Addproduct to set RelationShipName "<RelationShipName>" and Package "<Package>"
      And call EndConfigurationService and get Quote
      And call GetProductDetailsService using promotionCode "<promotionCodeIPTV>" and get ProductId, PriceList
   		And call QuoteAddItems and get quote
      And call BeginConfigurationService using product item name "Service Bundle-TV-IPTV-1704"
      And call Addproduct to set RelationShipName "<RelationShipTV>" and Package "<PackageTV>"
      And call EndConfigurationService and get Quote
      And call GetProductDetailsService using promotionCode "<promotionCodeVOIP>" and get ProductId, PriceList
   		And call QuoteAddItems and get quote
      And call BeginConfigurationService using product item name "<ServiceBundleVOIP>"
      And call EndConfigurationService and get Quote
      And call BBVOIPIPTV xDSLSynchronizeQuote to populate the AccessCode "<AccessCode>" and ConnRef number "<FbNumber>" on the quote line items for a xDSL order
      And call QuoteCheckOutService and get ActiveOrderID
      Then call SubmitOrder Service and Get successful OrderID
  
      Examples:

      | CustomerType | SSN          | AddressData                                         | promotionCode            | ServiceBundle            | RelationShipName       | Package                  | promotionCodeIPTV       | RelationShipTV               | PackageTV     | promotionCodeVOIP        | ServiceBundleVOIP           | AccessCode | FbNumber   |
      | B2B          | 5563363356 | FALKGRÄND;B;A;SÄTER;71102;113850745;78331 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 30-ADSL-1722 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 | VoIP SE_Promo_Offer_1743 | VoIP SE Service bundle_1606 | 100        | FB26098323 |
      #| B2C          | 199810100157 | STORGATAN;39;B;MALMÖ;308-024-01;112782736;21142     | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 30-VDSL-1722 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 | VoIP SE_Promo_Offer_1743 | VoIP SE Service bundle_1606 | 100        | FB26098324 |
      #| B2C          | 199810100397 | FRIISGATAN;8;A;MALMÖ;2296-1021;112800948;21146 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 30-VDSL-1722 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 | VoIP SE_Promo_Offer_1743 | VoIP SE Service bundle_1606 | 100        | FB26097617 |
      #| B2C          | 199810100736 | GÖKBLOMSTERVÄGEN;7;B;BEDDINGESTRAND;VILLA;112794060;24651 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 30-VDSL-1722 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 | VoIP SE_Promo_Offer_1743 | VoIP SE Service bundle_1606 | 100        | FB26097619 |  
