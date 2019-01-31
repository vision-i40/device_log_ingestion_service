Feature: Identify and Extract Device Log
  In order to have the devices information extracted and saved
  I as a Vision Customer
  I want device logs to be processed and stored

  Scenario: A Wise log is received
    Given the service is up and running
    When I receive a request from Wise device
    Then I should save Wise Device Log in database
    And detect as Wise device
    And extract Wise information