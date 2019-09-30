  Feature: Modify

  @modifyBB
    Scenario Outline: Modify single broadband on xDSL
    This scenario is a bit tricky because some steps will not be executed under all circumstances

    Given call QueryAllAsset using SSN "<SSN>" to get asset details AssetNumber and ServiceAccountId for promotionName "<PromotionCode>"
		And call ModifyAssetToQuote for ServiceBundle "<ServiceBundle>" from Package "<CurrentPackage>" to "<TobeModifiedPackage>"
		And Modify all exsiting Asset using product item name "<ServiceBundle>" RelationShipName "<RelationShipName>" and Package "<CurrentPackage>" to "<TobeModifiedPackage>"   
    And call SynchronizeQuote for a Modify order
    And call QuoteCheckOutService and get ActiveOrderID  
    #And call TSChannelSubmitOrder to submit the order

Examples: 
| CustomerType          | SSN          | PromotionCode                                                              | ServiceBundle                                                                    | RelationShipName                                                                    | CurrentPackage                                                        | TobeModifiedPackage                                                     | 
#| B2C-BB-SDU            | 199810095985 | P-IA-Fiber-Broadband-1743                                                  | Service Bundle-IA-1703-1                                                         | OptGrp-IA-speed-1703-1                                                              | IA-Bredband 10/10-1725                                              |  IA-Bredband 100/100-1703                                                   | 
#| B2B-BB-SDU            | 5566711213   | P-IA-Fiber-Broadband-1743                                                  | Service Bundle-IA-1703-1                                                         | OptGrp-IA-speed-1703-1                                                              | IA-Bredband 100/100-1703                                              | IA-Bredband 250-1703                                                    | 
#| B2C-IPTV-SDU          | 199912280626 | P-TV-IPTV-PlayPlus-1711                                                    | Service Bundle-TV-IPTV-1704                                                      | OptGrp-TV-TeliaPackages-1704                                                        | TV-Lagom-1606                                                         | TV-Stor-1741                                                            | 
#| B2C-BB/IPTV-MDU       | 199810095985 | P-IA-Fiber-Broadband-1743;P-TV-IPTV-PlayPlus-1711;;                        | Service Bundle-IA-1703-1;Service Bundle-TV-IPTV-1704                             | OptGrp-IA-speed-1703-1;OptGrp-TV-TeliaPackages-1704                                 | IA-Bredband 100/100-1703;TV-Lagom-1606                                | IA-Bredband 250-1703;TV-Stor-1741                                       | 
#| B2C-BB/IPTV/VoIP-xDSL | 199912280626 | P-IA-xDSL-Broadband-1711;P-TV-IPTV-PlayPlus-1711;VoIP SE_Promo_Offer_1743  | Service Bundle-IA-1703-1;Service Bundle-TV-IPTV-1704;VoIP SE Service bundle_1606 | OptGrp-IA-speed-1703-1;OptGrp-TV-TeliaPackages-1704;VoIP SE Subscription alternativ | IA-Bredband 30-ADSL-1722;TV-Lagom-1606;VoIP-Price Agreement Mini-1612 | IA-Bredband 30-VDSL-1722;TV-Stor-1741;VoIP-Price Agreement Maximal-1612 | 
| B2C-BB/IPTV/VoIP-SDU  | 199810095985 | P-IA-Fiber-Broadband-1743;P-TV-IPTV-PlayPlus-1711;VoIP SE_Promo_Offer_1743 | Service Bundle-IA-1703-1;Service Bundle-TV-IPTV-1704;VoIP SE Service bundle_1606 | OptGrp-IA-speed-1703-1;OptGrp-TV-TeliaPackages-1704;VoIP SE Subscription alternativ | IA-Bredband 10/10-1725;TV-Lagom-1606;VoIP-Price Agreement Mini-1612 | IA-Bredband 100/100-1703;TV-Stor-1741;VoIP-Price Agreement Maximal-1612   | 


     
      
      