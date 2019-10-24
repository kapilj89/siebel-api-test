@SanityMobileUpgradeDowngradeSidegrade

Feature: Mobile-Upgrade/downgrade/sidegrade for MobileVoice and MBB

  Scenario Outline: This includes upgrade downgrade and sidegrade scenarios for mobileVoice and MBB 
  
    Given call QueryCustomer using SSN "<SSN>" to get account and billing details
      And call QueryAsset using SSN "<SSN>" to get asset details AssetNumber and ServiceAccountId for promotionName "<PromotionCode>"
      And call GetProductDetailsService using promotionCode "<UpgradePromotion>" and get ProductId, PriceList
      And call QueryUpgradePromotionToQuote for "<ReasonCode>" Order and get Quote
      And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      And call BeginConfigurationService using product item name "<ServiceBundle>"
      And call UpdateConfigurationSetAttribute to set commitment duration to "<Duration>" months for product item name "<NewPlan>"
      And call EndConfigurationService and get Quote
      And call SynchronizeQuote to change dueDate of "<ReasonCode>" for serivce "<OldPlan>" and "<NewPlan>" in a Mobile order
      And call QuoteCheckOutService and get ActiveOrderID
  #   And call TSChannelSubmitOrder to submit the order
  
    Examples: 
      | Stream/Scenario  | SSN          | PromotionCode                      | UpgradePromotion              | ServiceBundle                        | ReasonCode | Duration | NewPlan                            | OldPlan                       | 
      | Mobile/Upgrade   | 199806166774 | MT-Telia Mobil 30GB-1924           | MT-Telia Mobil Unlimited-1924 | MT-Mobile Privat Service Bundle-1743 | Upgrade    | 12       | MT-Telia Mobil Unlimited Plan-1924 | MT-Telia Mobil 30GB Plan-1924 | 
      | MBB/Upgrade      | 199806166774 | MT-MBB Privat 20 GB no commit-1741 | HW-Standalone Hardware-1725   | MT-MBB Privat Service Bundle-1741    | Upgrade    | 12       | MT-Telia Mobil Unlimited Plan-1924 | MT-Telia Mobil 30GB Plan-1924 | 
      | Mobile/Downgrade | 199806166774 | MT-Telia Mobil 30GB-1924           | MT-Telia Mobil 6GB-1924       | MT-Mobile Privat Service Bundle-1743 | Downgrade  | 0        | MT-Telia Mobil 6GB Plan-1924       | MT-Telia Mobil 30GB Plan-1924 | 
      | MBB/Downgrade    | 199806166774 | MT-MBB Privat 20 GB no commit-1741 | HW-Standalone Hardware-1725   | MT-MBB Privat Service Bundle-1741    | Downgrade  | 0        | MT-Telia Mobil 6GB Plan-1924       | MT-Telia Mobil 30GB Plan-1924 | 
      | Mobile/Sidegrade | 199806166774 | MT-Telia Mobil 30GB-1924           | MT-Telia Mobil 30GB-1924      | MT-Mobile Privat Service Bundle-1743 | Sidegrade  | 0        | MT-Telia Mobil 30GB Plan-1924      | MT-Telia Mobil 30GB Plan-1924 | 
  
  
