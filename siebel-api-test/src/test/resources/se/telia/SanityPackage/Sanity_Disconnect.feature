Feature: Modify

  @modifyBB
    Scenario Outline: Modify single broadband on xDSL
    This scenario is a bit tricky because some steps will not be executed under all circumstances

    Given call QueryAsset using SSN "<SSN>" to get asset details AssetNumber and ServiceAccountId for promotionName "<PromotionCode>"
    And call ModifyAssetToQuote for the RelationshipName "<RelationShipName>" and Package "<CurrentPackage>" to "<TobeModifiedPackage>"
    And call BeginConfigurationService using product item name "<ServiceBundle>"
    And call ModifyProduct to set RelationShipName "<RelationShipName>" and Package "<CurrentPackage>" to "<TobeModifiedPackage>"
    And call EndConfigurationService and get Quote
    And call SynchronizeQuote for a Modify order
    And call QuoteCheckOut to convert the quote into a order
    And call TSChannelSubmitOrder to submit the order

Examples:
     | CustomerType  | SSN          |  PromotionCode             | ServiceBundle            | RelationShipName       | CurrentPackage                  | TobeModifiedPackage       | 
     | B2B-BB        | 199810095985 |  P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 100/100-1703        | IA-Bredband 250-1703      | 
     | B2C-BB        | 5566711213   |  P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 100/100-1703        | IA-Bredband 250-1703      |
     | B2B-IPTV      | 5566711213   |  P-IA-Fiber-Broadband-1743 | Service Bundle-IA-1703-1 | OptGrp-IA-speed-1703-1 | IA-Bredband 100/100-1703        | IA-Bredband 250-1703      |
     
      
      
      