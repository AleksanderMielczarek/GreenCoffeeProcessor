package com.github.aleksandermielczarek.greencoffeeprocessor

import android.support.test.rule.ActivityTestRule
import com.github.aleksandermielczarek.greencoffee.GreenCoffee
import com.mauriciotogneri.greencoffee.GreenCoffeeTest
import com.mauriciotogneri.greencoffee.ScenarioConfig
import org.junit.Rule
import org.junit.Test

/**
 * Created by Aleksander Mielczarek on 16.02.2018.
 */
@GreenCoffee(featureFromAssets = "assets/login.feature")
abstract class LoginTest(scenario: ScenarioConfig) : GreenCoffeeTest(scenario) {

    @Rule
    @JvmField
    val activity = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun test() {
        start(LoginSteps())
    }
}