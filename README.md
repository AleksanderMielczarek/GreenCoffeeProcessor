[![](https://jitpack.io/v/AleksanderMielczarek/GreenCoffeeProcessor.svg)](https://jitpack.io/#AleksanderMielczarek/GreenCoffeeProcessor)

# GreenCoffeeProcessor

Allows to use [green-coffee](https://github.com/mauriciotogneri/green-coffee) together with [Android Test Orchestrator](https://developer.android.com/training/testing/junit-runner.html#using-android-test-orchestrator).

## Usage

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
	repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Add the dependency

```groovy
apply plugin: "kotlin-kapt"

dependencies {
    ...
    androidTestImplementation "com.mauriciotogneri:greencoffee:$latestVersion"
    androidTestImplementation "com.github.AleksanderMielczarek.GreenCoffeeProcessor:greencoffee-annotations:$latestVersion"
    kaptAndroidTest "com.github.AleksanderMielczarek.GreenCoffeeProcessor:greencoffee-processor:$latestVersion"
}
```

## Example

Instated of using parametrized test, just make your test abstract and annotate with `@GreenCoffee`. GreenCoffeeProcessor will generate rest for you.
   
```kotlin
@GreenCoffee(
        screenshotPath = "testapp",
        featureFromAssets = "assets/login.feature",
        locales = [ScenarioLocale("en", "GB"), ScenarioLocale("es", "ES")],
        includeScenarios = ["Invalid username and password", "Set of wrong credentials"],
        excludeScenarios = ["Invalid username"]
)
abstract class LoginTest(scenario: ScenarioConfig) : GreenCoffeeTest(scenario) {

    @Rule
    @JvmField
    val activity = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun test() {
        start(LoginSteps())
    }
}
```

If app folder is different than default app than specify it:

```groovy
kapt {
    arguments {
        arg("appFolder", "myApp")
    }
}
```

## TODO

- fix dummy file warning

## Changelog

### 0.2.0 (2018-02-17)

- add `Locale` support

### 0.1.0 (2018-02-16)

- initial release

## License

    Copyright 2018 Aleksander Mielczarek

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
