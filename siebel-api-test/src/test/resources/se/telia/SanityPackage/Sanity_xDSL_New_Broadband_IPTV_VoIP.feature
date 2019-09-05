Feature: Sanity Test for (Primary&Secondary) SDU New BB ONLY

  @SanityxDSLBroadbandIPTVVOIP
  Scenario Outline: 
  Create New XDSL Broadband IPTV and VOIP
  This scenario used create new broadband IPTV and VOIP
  Note:AddressData format-  
     When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      #And call QuerySelfServiceAddress Add/Update for XDSL format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"
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
      | CustomerType | SSN          | AddressData                                    | promotionCode            | ServiceBundle            | RelationShipName       | Package                  | promotionCodeIPTV       | RelationShipTV               | PackageTV     | promotionCodeVOIP        | ServiceBundleVOIP           | AccessCode     | FbNumber   | 
      | B2C          | 199902073239 | VÄSTANVÄG;51;B;LIMHAMN;652-110-01;113014309;21615 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 10-1722 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 | VoIP SE_Promo_Offer_1743 | VoIP SE Service bundle_1606 | 100        | FB26098454 | 
      #| B2C          | 199902074807 | PILÅKERSVÄGEN;11;A;MALMÖ;411-098-01;113008932;21747 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 30-VDSL-1722 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 | VoIP SE_Promo_Offer_1743 | VoIP SE Service bundle_1606 | 100     | FB26098457 | 
      #| B2C          | 199902062174 | PILÅKERSVÄGEN;11;A;MALMÖ;411-098-01;113008932;21747 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 30-VDSL-1722 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 | VoIP SE_Promo_Offer_1743 | VoIP SE Service bundle_1606 | 100        | FB26098463 |
      #| B2C          | 199902063784 | KORSÖRVÄGEN;23;A;MALMÖ;566-181-01;112821013;21456 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 30-VDSL-1722 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 | VoIP SE_Promo_Offer_1743 | VoIP SE Service bundle_1606 | 100        | FB26098464 |
      #| B2C          | 199902068403 | VÄSTANVÄG;51;B;LIMHAMN;652-110-01;113014309;21615 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 10-1722  | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 | VoIP SE_Promo_Offer_1743 | VoIP SE Service bundle_1606 | 100        | FB26098465 |
      #| B2C          | 199902070631 | VÄSTANVÄG;51;B;LIMHAMN;652-110-01;113014309;21615 | P-IA-xDSL-Broadband-1711 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 30-ADSL-1722 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 | VoIP SE_Promo_Offer_1743 | VoIP SE Service bundle_1606 | 100        | FB26098459 | 
  
  
