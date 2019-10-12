Feature: Simple Move

  @move
  Scenario: Move single broadband on xDSL
  This scenario is a bit tricky because some steps will not be executed under all circumstances
    Given call QueryAsset using SSN "199911100783" to get asset details AssetNumber and ServiceAccountId for promotionName "P-IA-xDSL-Broadband-1711"
    When call QueryCustomer using SSN "199911100783" to get PrimaryAddressId
    Then call CheckMoveInAddress using MoveInPointId to validate the existence of move-in address and call AssociateMoveInAddress or CreateMoveInAddress if necessary
      | MoveInPointId        | 129997480  |
      | MoveInCity           | KÅLLERED   |
      | MoveInStreetAddress  | BÖLETVÄGEN |
      | MoveInStreetAddress2 | 6          |
      | MoveInEntrance       | A          |
      | MoveInPostalCode     | 72340      |
      | MoveInApartmentNum   |            |
      | MoveOutDate          | 2019-11-30 |
      | MoveInDate           | 2019-12-01 |

    And call MoveModifyAssetToQuote to create move quote using AssetNumber and ServiceAccountId
    And call QueryQuote to fetch the quote from database
    And call SynchronizeQuote to populate the due date on the quote line items for a Move order
    And call QuoteCheckOut to convert the quote into a move order
    And call GetOrder to get the order and assert orderDueDate and moveOutDate
    And call TSChannelSubmitOrder to submit the order
    Then call CheckOrderStatus to verify the order status to be "Open"
    And test data is reset after move order