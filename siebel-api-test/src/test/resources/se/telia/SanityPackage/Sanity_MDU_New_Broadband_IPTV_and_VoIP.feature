Feature: Sanity Test for New MDU BroadBand IPTV VOIP order

  @NewMDUBB
    Scenario Outline: Create New MDU BroadBand IPTV VOIP order
    This scenario used create new MDU BroadBand IPTV VOIP order 

     When call SelfServiceUser to get primary organization id which is used in quote creation
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
      And call AddressAddorupdate with format StreetName,StreetNumber,Entrance,City,Apartnumber,PointID;Postalcode "<AddressData>"
      #And call GetProductDetailsService using promotionCode "<promotionCode>" and get ProductId, PriceList
      #And call ApplyProductPromotionService and get quote
      #And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      #And call BeginConfigurationService using product item name "<ServiceBundle>"
      #And call Addproduct to set RelationShipName "OptGrp-IA-speed-1703-1" and Package "IA-Bredband 100/100-1703"
      #And call EndConfigurationService and get Quote
      #And call GetProductDetailsService using promotionCode "<promotionCodeIPTV>" and get ProductId, PriceList
   #		And call QuoteAddItems and get quote
      #And call BeginConfigurationService using product item name "Service Bundle-TV-IPTV-1704"
      #And call Addproduct to set RelationShipName "<RelationShipTV>" and Package "<PackageTV>"
      #And call EndConfigurationService and get Quote
      #And call BBIPTV SynchronizeQuote to populate the deliveryContract "<Agreement>" Revision "<RevisionNumber>" RowID "<RowID>" and AccessCode "<AccessCode>" on the quote line items for a MDU order
      #And call QuoteCheckOutService and get ActiveOrderID
      #Then call SubmitOrder Service and Get successful OrderID
  #

     Examples: 
      | CustomerType | SSN          | AddressData                                             | promotionCode             | ServiceBundle            | RelationShipName       | Package                  | promotionCodeIPTV       | RelationShipTV               | PackageTV     |  AccessCode | Agreement    |RevisionNumber|RowID 	        | 
      #| B2C          | 199810211939 | BRAHEGATAN;4;A;GÖTEBORG;80128-1213;134908358;41514 | P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 100/100-1703 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 |  300        | 1-2553857551 | 	1						|	1-168I13J			|
      | B2C          | 199809026553   | BRAHEGATAN;4;A;GÖTEBORG;80128-1213;134908358;41514      | P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 100/100-1703 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 |  300        | 1-2553836110 | 	1						|	1-168HKJY			|
      | B2C          | 199809028757  | BRAHEGATAN;4;A;GÖTEBORG;80128-1213;134908358;41514      | P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 100/100-1703 | P-TV-IPTV-PlayPlus-1711 | OptGrp-TV-TeliaPackages-1704 | TV-Lagom-1606 |  300        | 1-2553836110 | 	1						|	1-168HKJY     |
  