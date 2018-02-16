Feature: Login screen to authenticate users

	Scenario: Invalid username and password
        Given I see an empty login form
         When I introduce an invalid username
          And I introduce an invalid password
          And I press the login button
         Then I see an error message saying 'Invalid credentials'