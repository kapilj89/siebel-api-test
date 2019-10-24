  Feature: Modify

  @SanitymodifyBB
    Scenario Outline: Modify single broadband on xDSL
    This scenario is a bit tricky because some steps will not be executed under all circumstances

    Given call QueryAllAsset using SSN "<SSN>" to get asset details AssetNumber and ServiceAccountId for promotionName "<PromotionCode>"
		And call Mobile ModifyAssetToQuote for ServiceBundle "<ServiceBundle>"
		And call GetProductPromotionDetailsService using promotionCode "<HardwarePromotion>" and service "HW-iPhone X 64GB SG-1805" and get ProductId, PriceList
    And call ApplyPromotionOnExistingQuoteService and get quote
    And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
    And call QuoteAddBundleItem to add a serviceBundle and get quote
    And call BeginConfigurationService using product item name "<HardwareProduct>"
    And call HWUpdateConfiguration to SetAttribute "<Attribute>" Value of SIM "<Value>"
    And call EndConfigurationService and get Quote 
    And call SynchronizeQuote for a "<ServiceBundle>" Mobile Modify and HW "<HardwareProduct>" order
    And call QuoteCheckOutService and get ActiveOrderID
    #And call TSChannelSubmitOrder to submit the order

Examples: 
| CustomerType | SSN          | PromotionCode                      |HardwarePromotion           | ServiceBundle                        | HardwareProduct          | Attribute    | Value	  |     
| B2C-Mobile   | 199806166774 | MT-Telia Mobil 30GB-1924           |HW-Standalone Hardware-1725 | MT-Mobile Privat Service Bundle-1743 | HW-iPhone X 64GB SG-1805 | SerialNumber | 90873408 |
| B2C-MBB      | 199806166774 | MT-MBB Privat 20 GB no commit-1741 |HW-Standalone Hardware-1725 | MT-MBB Privat Service Bundle-1741    | HW-iPhone X 64GB SG-1805 | SerialNumber | 90873408 |   


     
    # ORDER-ID 5-8210571756
      