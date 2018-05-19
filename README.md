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

## Test

Instated of using parametrized test, just make your test abstract and annotate with `@GreenCoffee`. GreenCoffeeProcessor will generate rest for you.
   
```kotlin
@GreenCoffee(
        screenshotOnFail = true,
        featureFromAssets = "assets/login.feature",
        tags = ["tag1", "tag2"],
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

## Custom app folder name

If app folder is different than default app than specify it:

```groovy
kapt {
    arguments {
        arg("appFolder", "myApp")
    }
}
```

## Annotations

If tag is present in scenario than processor will annotate generated method with appropriate annotation. 
Processor supports few annotations out of the box (name is enough to generate annotation):
- org.junit.Ignore
- android.support.test.filters.FlakyTest
- android.support.test.filters.SmallTest
- android.support.test.filters.MediumTest
- android.support.test.filters.LargeTest

For custom annotations fully qualified name is required for processor to generate or it can be registered in arguments:

```groovy
kapt {
    arguments {
        arg("supportedAnnotations", "com.custom.Annotation1, com.custom.Annotation2")
    }
}
```

## Changelog

### 0.5.0 (2018-05-19)

- generated method are using names from scenarios
- support annotations

### 0.4.0 (2018-04-29)

- support green-coffee 3.2.1

### 0.3.1 (2018-03-11)

- fix creating of dummy file

### 0.3.0 (2018-02-17)

- fix dummy file warning

### 0.2.1 (2018-02-17)

- fix `FileHelper`

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
