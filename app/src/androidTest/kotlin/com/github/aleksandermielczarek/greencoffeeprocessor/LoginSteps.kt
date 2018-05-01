package com.github.aleksandermielczarek.greencoffeeprocessor

import com.mauriciotogneri.greencoffee.GreenCoffeeSteps
import com.mauriciotogneri.greencoffee.annotations.Given
import com.mauriciotogneri.greencoffee.annotations.Then
import com.mauriciotogneri.greencoffee.annotations.When

class LoginSteps : GreenCoffeeSteps() {

    @Given("^I see an empty login form$")
    fun iSeeAnEmptyLoginForm() {
    }

    @When("^I introduce an invalid username$")
    fun iIntroduceAnInvalidUsername() {
    }

    @When("^I introduce an invalid password$")
    fun iIntroduceAnInvalidPassword() {
    }

    @When("^I press the login button$")
    fun iPressTheLoginButton() {
    }

    @Then("^I see an error message saying 'Invalid credentials'$")
    fun iSeeAnErrorMessageSayingInvalidCredentials() {
    }
}