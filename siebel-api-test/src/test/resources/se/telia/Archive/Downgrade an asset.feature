Feature: Mobile- Downgrade MobileVoice/MBB

@SanitymodifyBB
  Scenario Outline: 
  
      And call QueryCustomer using SSN "<SSN>" to get account and billing details
    Given call QueryAsset using SSN "<SSN>" to get asset details AssetNumber and ServiceAccountId for promotionName "<PromotionCode>"
      And call GetProductDetailsService using promotionCode "<UpgradePromotion>" and get ProductId, PriceList
      And call QueryUpgradePromotionToQuote for "<ReasonCode>" Order and get Quote
      And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
      And call BeginConfigurationService using product item name "<ServiceBundle>"
      And call UpdateConfigurationSetAttribute to set commitment duration to "<Duration>" months for product item name "<NewPlan>"
      And call EndConfigurationService and get Quote
      And call downgrade/sidegrade SynchronizeQuote to change dueDate for serivce "<OldPlan>" and "<NewPlan>" in a Mobile order
      And call QuoteCheckOutService and get ActiveOrderID
  #		And call TSChannelSubmitOrder to submit the order
  
    Examples: 
      | CustomerType | SSN          | PromotionCode                      | UpgradePromotion              | ServiceBundle                        | ReasonCode | Duration | NewPlan                            | OldPlan                       | 
     #| B2C-Mobile   | 198109020431 | MT-Telia Mobil 6GB-1924            | MT-Telia Mobil Unlimited-1924 | MT-Mobile Privat Service Bundle-1743 | Downgrade  | 0        | MT-Telia Mobil 6GB Plan-1924 | MT-Telia Mobil 30GB Plan-1924 | 
      | B2C-Mobile   | 199806166774 | MT-Telia Mobil 30GB-1924           | MT-Telia Mobil 6GB-1924       | MT-Mobile Privat Service Bundle-1743 | Downgrade  | 0        | MT-Telia Mobil 6GB Plan-1924 | MT-Telia Mobil 30GB Plan-1924 | 
     #| B2C-MBB      | 199806166774 | MT-MBB Privat 20 GB no commit-1741 | HW-Standalone Hardware-1725   | MT-MBB Privat Service Bundle-1741    | Downgrade  | 0        | MT-Telia Mobil 6GB Plan-1924 | MT-Telia Mobil 30GB Plan-1924 | 
     #| B2C-Mobile   | 198109020431 | MT-Telia Mobil 6GB-1924            | MT-Telia Mobil Unlimited-1924 | MT-Mobile Privat Service Bundle-1743 | Downgrade  | 0        | MT-Telia Mobil 6GB Plan-1924 | MT-Telia Mobil 30GB Plan-1924 | 
  
  # ORDER-ID 5-5124207171
  
  
