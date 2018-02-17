package com.github.aleksandermielczarek.greencoffee

import com.nhaarman.mockito_kotlin.given
import com.squareup.kotlinpoet.ClassName
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import javax.lang.model.element.TypeElement

/**
 * Created by Aleksander Mielczarek on 17.02.2018.
 */
@RunWith(MockitoJUnitRunner.StrictStubs::class)
class ProgrammerTest {

    @Mock
    lateinit var typeHelper: TypeHelper

    @Mock
    lateinit var test: TypeElement

    @InjectMocks
    lateinit var programmer: Programmer

    @Before
    fun setUp() {
        given(typeHelper.getPackage(test)).willReturn("com.test")
        given(typeHelper.getName(test)).willReturn("Test")
        given(typeHelper.getTypeName(test)).willReturn(ClassName("com.test", "Test"))
    }

    @Test
    internal fun `should write all`() {
        val expected = """
            package com.test

            import android.support.test.filters.LargeTest
            import android.support.test.runner.AndroidJUnit4
            import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
            import java.util.Locale
            import org.junit.runner.RunWith

            @LargeTest
            @RunWith(AndroidJUnit4::class)
            class GreenCoffeeTest1 : Test(GreenCoffeeConfig("screenshotPath")
              .withFeatureFromAssets("featurePath")
              .scenarios(Locale("en", "GB", ""))
              .filter { listOf("include").contains(it.scenario().name()) }
              .filter { !listOf("exclude").contains(it.scenario().name()) }
              [0]
            )
        """

        val code = programmer.writeCode(
                test,
                GreenCoffeeData(
                        "screenshotPath",
                        "featurePath",
                        listOf(Locale("en", "GB", "")),
                        listOf("include"),
                        listOf("exclude")
                ),
                1
        )

        assertEquals(expected.trimIndent(), code.toString().trimIndent())
    }

    @Test
    internal fun `should write screenshot`() {
        val expected = """
            package com.test

            import android.support.test.filters.LargeTest
            import android.support.test.runner.AndroidJUnit4
            import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
            import org.junit.runner.RunWith

            @LargeTest
            @RunWith(AndroidJUnit4::class)
            class GreenCoffeeTest1 : Test(GreenCoffeeConfig("screenshotPath")
              .withFeatureFromAssets("")
              .scenarios()
              [0]
            )
        """

        val code = programmer.writeCode(
                test,
                GreenCoffeeData(
                        "screenshotPath",
                        "",
                        emptyList(),
                        emptyList(),
                        emptyList()
                ),
                1
        )

        assertEquals(expected.trimIndent(), code.toString().trimIndent())
    }

    @Test
    internal fun `should write feature`() {
        val expected = """
            package com.test

            import android.support.test.filters.LargeTest
            import android.support.test.runner.AndroidJUnit4
            import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
            import org.junit.runner.RunWith

            @LargeTest
            @RunWith(AndroidJUnit4::class)
            class GreenCoffeeTest1 : Test(GreenCoffeeConfig("")
              .withFeatureFromAssets("featurePath")
              .scenarios()
              [0]
            )
        """

        val code = programmer.writeCode(
                test,
                GreenCoffeeData(
                        "",
                        "featurePath",
                        emptyList(),
                        emptyList(),
                        emptyList()
                ),
                1
        )

        assertEquals(expected.trimIndent(), code.toString().trimIndent())
    }

    @Test
    internal fun `should write locale`() {
        val expected = """
            package com.test

            import android.support.test.filters.LargeTest
            import android.support.test.runner.AndroidJUnit4
            import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
            import java.util.Locale
            import org.junit.runner.RunWith

            @LargeTest
            @RunWith(AndroidJUnit4::class)
            class GreenCoffeeTest1 : Test(GreenCoffeeConfig("")
              .withFeatureFromAssets("")
              .scenarios(Locale("en", "GB", ""), Locale("es", "ES", "2"))
              [0]
            )
        """

        val code = programmer.writeCode(
                test,
                GreenCoffeeData(
                        "",
                        "",
                        listOf(Locale("en", "GB", ""), Locale("es", "ES", "2")),
                        emptyList(),
                        emptyList()
                ),
                1
        )

        assertEquals(expected.trimIndent(), code.toString().trimIndent())
    }

    @Test
    internal fun `should write include`() {
        val expected = """
            package com.test

            import android.support.test.filters.LargeTest
            import android.support.test.runner.AndroidJUnit4
            import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
            import org.junit.runner.RunWith

            @LargeTest
            @RunWith(AndroidJUnit4::class)
            class GreenCoffeeTest1 : Test(GreenCoffeeConfig("")
              .withFeatureFromAssets("")
              .scenarios()
              .filter { listOf("include1", "include2").contains(it.scenario().name()) }
              [0]
            )
        """

        val code = programmer.writeCode(
                test,
                GreenCoffeeData(
                        "",
                        "",
                        emptyList(),
                        listOf("include1", "include2"),
                        emptyList()
                ),
                1
        )

        assertEquals(expected.trimIndent(), code.toString().trimIndent())
    }

    @Test
    internal fun `should write exclude`() {
        val expected = """
            package com.test

            import android.support.test.filters.LargeTest
            import android.support.test.runner.AndroidJUnit4
            import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
            import org.junit.runner.RunWith

            @LargeTest
            @RunWith(AndroidJUnit4::class)
            class GreenCoffeeTest1 : Test(GreenCoffeeConfig("")
              .withFeatureFromAssets("")
              .scenarios()
              .filter { !listOf("exclude1", "exclude2").contains(it.scenario().name()) }
              [0]
            )
        """

        val code = programmer.writeCode(
                test,
                GreenCoffeeData(
                        "",
                        "",
                        emptyList(),
                        emptyList(),
                        listOf("exclude1", "exclude2")
                ),
                1
        )

        assertEquals(expected.trimIndent(), code.toString().trimIndent())
    }

    @Test
    internal fun `should write count`() {
        val expected = """
            package com.test

            import android.support.test.filters.LargeTest
            import android.support.test.runner.AndroidJUnit4
            import com.mauriciotogneri.greencoffee.GreenCoffeeConfig
            import org.junit.runner.RunWith

            @LargeTest
            @RunWith(AndroidJUnit4::class)
            class GreenCoffeeTest1 : Test(GreenCoffeeConfig("")
              .withFeatureFromAssets("")
              .scenarios()
              [0]
            )

            @LargeTest
            @RunWith(AndroidJUnit4::class)
            class GreenCoffeeTest2 : Test(GreenCoffeeConfig("")
              .withFeatureFromAssets("")
              .scenarios()
              [1]
            )
        """

        val code = programmer.writeCode(
                test,
                GreenCoffeeData(
                        "",
                        "",
                        emptyList(),
                        emptyList(),
                        emptyList()
                ),
                2
        )

        assertEquals(expected.trimIndent(), code.toString().trimIndent())
    }
}