/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.Espresso;
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


@RunWith(AndroidJUnit4.class)
public class Modificar_Usuario_Test {

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
    public void modificar_usuario() {

        //Introduce en "username" a "mariagonzalez", cierra el teclado y espera
        onView(ViewMatchers.withId(R.id.edit_username)).perform(ViewActions.typeText("vicent1990"));
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

        //Hace click en la opción del area de usuario
        onView(ViewMatchers.withId(R.id.nav_usuari)).perform(click());
        SystemClock.sleep(2000);

        onView(ViewMatchers.withId(R.id.nomTextView))
                .perform(click());
        SystemClock.sleep(2000);


        onView(ViewMatchers.withId(R.id.nomTextView))
                .perform(ViewActions.clearText());
        SystemClock.sleep(2000);

        onView(ViewMatchers.withId(R.id.nomTextView))
                .perform(ViewActions.typeText("nombre_modificado"));
        SystemClock.sleep(4000);

        onView(ViewMatchers.withId(R.id.nomTextView))
                .perform(ViewActions.closeSoftKeyboard());
        SystemClock.sleep(2000);

        onView(ViewMatchers.withId(R.id.button_modificar_usuari))
                .perform(click());
        SystemClock.sleep(2000);

        //Abre el menú
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        SystemClock.sleep(2000);

        //Hace click en la opción del area de usuario
        onView(ViewMatchers.withId(R.id.nav_home)).perform(click());
        SystemClock.sleep(4000);

        onView(ViewMatchers.withId(R.id.saludoTextView))
                .check(ViewAssertions.matches(ViewMatchers.withText(containsString("nombre_modificado"))));
        SystemClock.sleep(2000);

    }

}
