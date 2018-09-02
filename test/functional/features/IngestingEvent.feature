Feature: Process Ingestion Event
  In order to have the devices logs saved and information extracted
  As a Vision Customer
  I want logs from Ingestion Queue been processed and saved in database

  Scenario: A Wise log ingestion event is received in queue
    Given the service is up and running
    When I receive an event from Wise device
    Then I should save Wise IO Log in database
    And get ingestion event info of Wise device
    And detect as Wise device
    And extract Wise info

  Scenario: A unknown log ingestion event is received in queue
    Given the service is up and running
    When I receive an event from unknown device type
    Then I should save unknown IO Log in database
    And get ingestion event info of unknown device
    And detect as unknown device
    And does not extract info