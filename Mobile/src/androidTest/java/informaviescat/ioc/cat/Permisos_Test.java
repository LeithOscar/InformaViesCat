/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.not;

import android.os.SystemClock;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import kotlin.internal.UProgressionUtilKt;


@RunWith(AndroidJUnit4.class)
public class Permisos_Test {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }


    @Test
    public void secciones_Tecnico() {

        //Introduce en "username" a "mariagonzalez", cierra el teclado y espera
        onView(ViewMatchers.withId(R.id.edit_username)).perform(ViewActions.typeText("mariagonzalez"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce en "password" la contraseña correcta, cierra el teclado y espera
        onView(ViewMatchers.withId(R.id.edit_password)).perform(ViewActions.typeText("1234"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Hace click en el botón de login
        onView(ViewMatchers.withId(R.id.button_login)).perform(click());
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(10000);

        //Comprueba que se haya abierto el intent que deja pasar a Home
        Intents.intended(IntentMatchers.hasComponent(Seccion_Home.class.getName()));
        SystemClock.sleep(2000);

        //Abre el menú
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        SystemClock.sleep(2000);

        //Pasa el test si las siguientes vistas no están disponibles
        Espresso.onView(ViewMatchers.withText("Incidències per validar")).check(ViewAssertions.doesNotExist());
        Espresso.onView(ViewMatchers.withText("Incidències tancades")).check(ViewAssertions.doesNotExist());
        Espresso.onView(ViewMatchers.withText("Incidències resoltes")).check(ViewAssertions.doesNotExist());
    }

    @Test
    public void secciones_Ciudadano() {

        //Introduce en "username" a "vicent1990", cierra el teclado y espera
        onView(ViewMatchers.withId(R.id.edit_username)).perform(ViewActions.typeText("vicent1990"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce en "password" la contraseña correcta, cierra el teclado y espera
        onView(ViewMatchers.withId(R.id.edit_password)).perform(ViewActions.typeText("Patata45@"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Hace click en el botón de login
        onView(ViewMatchers.withId(R.id.button_login)).perform(click());
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(10000);

        //Comprueba que se haya abierto el intent que deja pasar a Home
        Intents.intended(IntentMatchers.hasComponent(Seccion_Home.class.getName()));
        SystemClock.sleep(2000);

        //Abre el menú
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        SystemClock.sleep(2000);

        //Pasa el test si las siguientes vistas no están disponibles
        Espresso.onView(ViewMatchers.withId(R.id.nav_per_validar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.nav_oberta)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.nav_en_progress)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.nav_resolta)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.nav_tancada)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
