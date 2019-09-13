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
     | CustomerType            | SSN          |  PromotionCode                                                              | ServiceBundle                                                                                | RelationShipName                                                                 | CurrentPackage                          | TobeModifiedPackage                                             | 
     | B2C-BB-SDU             | 199810234212 |  P-IA-Fiber-Broadband-1743                                                  | Service Bundle-IA-1703-1                                                                   | OptGrp-IA-speed-1703-1                                                           | IA-Bredband 100/100-1703                | IA-Bredband 250-1703                                            | 
     | B2B-BB-SDU             | 5566711213   |  P-IA-Fiber-Broadband-1743                                                  | Service Bundle-IA-1703-1                                                                   | OptGrp-IA-speed-1703-1                                                           | IA-Bredband 100/100-1703                | IA-Bredband 250-1703                                            |
     | B2C-IPTV-SDU           | 199810095985 |  P-TV-IPTV-PlayPlus-1711                                                    | Service Bundle-TV-IPTV-1704                                                                | OptGrp-TV-TeliaPackages-1704                                                     | TV-Lagom-1606                           | TV-Stor-1741                                                    |
     | B2C-BB/IPTV-MDU        | 199810095985 |  P-IA-Fiber-Broadband-1743;P-TV-IPTV-PlayPlus-1711;;                          | Service Bundle-IA-1703-1;Service Bundle-TV-IPTV-1704    														       | OptGrp-IA-speed-1703-1;OptGrp-TV-TeliaPackages-1704                              | IA-Bredband 100/100-1703;TV-Lagom-1606  | IA-Bredband 250-1703;TV-Stor-1741                               |
     | B2C-BB/IPTV/VoIP-xDSL   | 199810303009 |  P-IA-xDSL-Broadband-1711;P-TV-IPTV-PlayPlus-1711;VoIP SE_Promo_Offer_1743  | Service Bundle-IA-1703-1;Service Bundle-TV-IPTV-1704;VoIP SE_B2B_0Months_Promo_Offer_1649    | OptGrp-IA-speed-1703-1;OptGrp-TV-TeliaPackages-1704;VoIP-Change of number-1649   | IA-Bredband 100/100-1703;TV-Lagom-1606  | IA-Bredband 250-1703;TV-Stor-1741;VoIP-Change of number-1649    |
     
      
      
      