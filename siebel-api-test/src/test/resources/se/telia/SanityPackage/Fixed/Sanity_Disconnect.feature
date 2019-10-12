Feature: BulkDisconnect

  @Disconnect
  Scenario Outline: Disconnect Feature
    This scenario is a bit tricky because some steps will not be executed under all circumstances

    Given call QueryMainAsset using SSN "<SSN>" to get asset details AssetNumber and ServiceAccountId for promotionName "<PromotionCode>"
    And call DisconnectAssetToQuote
    And call QueryQuote to fetch the quote from database
    And call SynchronizeQuote for a Disconnect order
   	And call QuoteCheckOutService and get ActiveOrderID
    #And call TSChannelSubmitOrder to submit the order

    Examples: 
      | CustomerType | SSN          | PromotionCode                                                                                                              | ServiceBundle            | RelationShipName       | CurrentPackage           | TobeModifiedPackage  |
      | B2B-BB       | 198203233237 | P-IA-Fiber-Broadband-1743;P-IA-xDSL-Broadband-1711;P-BIA-OF-BB Start-1827;P-TV-IPTV-PlayPlus-1711;VoIP SE_Promo_Offer_1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 100/100-1703 | IA-Bredband 250-1703 |
