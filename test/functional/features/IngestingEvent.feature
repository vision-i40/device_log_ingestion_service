Feature: Identify and Extract Device Log
  In order to have the devices information extracted and saved
  I as a Vision Customer
  I want device logs to be processed and stored

  Background: Service is up and database is clear
    Given the service is up and running
    And database is clear

  Scenario: A Wise log is received
    When A request is received from Wise device
    Then Wise Device Log should be saved in database
    And detect as Wise device
    And extract Wise information

  Scenario: Unknown log type is received
    When a request with unrecognized payload
    Then the log should not be saved in the database
    And should answer with a bad request response
