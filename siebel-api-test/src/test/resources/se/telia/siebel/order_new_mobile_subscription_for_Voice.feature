Feature: New Order


  @mobile
  Scenario: Order new mobile subscription without hardware voice
  This Scenario goes through the steps needed to order a mobile subscription stand alone (without hardware)
  as described in USE CASE - BUY ORDER - Promotion Standalone, which is documented here:
  https://diva.teliacompany.net/confluence/pages/viewpage.action?pageId=68651483
    When call SelfServiceUser to get primary organization id which is used in quote creation
    And call QueryCustomer using SSN "197907263219" to get account and billing details
    And call GetProductDetailsService using promotionCode "MT-Mobil Personal 10GB no commit-1819" and get ProductId, PriceList
    And call ApplyProductPromotionService and get quote
    And call ExecuteQuoting to commit the virtual quote in Siebel and get updated quote
    And call BeginConfigurationService using product item name "MT-Mobile Privat Service Bundle-1743"
    And call UpdateConfigurationSetAttribute to set commitment duration to "24" months for product item name "MT-Telia Mobil Personal 10GB-1819"
    And call EndConfigurationService and get Quote
    And call SynchronizeQuote to populate the due date on the quote line items for a Mobile order
    And call QuoteCheckOutService and get ActiveOrderID
    Then call SubmitOrder Service and Get successful OrderID
    Then call CheckOrderStatus to verify the order status to be "Open"


