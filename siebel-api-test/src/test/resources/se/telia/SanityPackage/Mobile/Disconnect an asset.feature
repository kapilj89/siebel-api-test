@SanityMobileDisconnect

Feature: Mobile Sanity Disconnect

  Scenario Outline: Disconnect an asset MBB and MobileVoice 
  
    Given call QueryMainAsset using SSN "<SSN>" to get asset details AssetNumber and ServiceAccountId for promotionName "<PromotionCode>"
      And call DisconnectAssetToQuote
      And call QueryQuote to fetch the quote from database
      And call SynchronizeQuote for a Disconnect order
      And call QuoteCheckOutService and get ActiveOrderID
  	 #And call TSChannelSubmitOrder to submit the order
  
    Examples: 
      | Stream | SSN          | PromotionCode                                    | 
     #| Mobile | 198109020431 | MT-Telia Mobil 30GB-1924;MT-Telia Mobil 6GB-1924 | 
      | MBB    | 198108296255 | MT-MBB Privat 20 GB no commit-174                | 
  

