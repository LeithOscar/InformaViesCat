/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.SystemClock;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
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
public class Listas_Incidencias_Test {

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
    public void listar_incidencias() {

        //Introduce en "username" a "vicent1990", cierra el teclado y espera
        onView(ViewMatchers.withId(R.id.edit_username)).perform(ViewActions.typeText("vicent1990"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce en "password" la contraseña correcta, cierra el teclado y espera
        onView(ViewMatchers.withId(R.id.edit_password)).perform(ViewActions.typeText("1234"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Hace click en el botón de login
        onView(ViewMatchers.withId(R.id.button_login)).perform(click());
        SystemClock.sleep(2000);
        SystemClock.sleep(10000);

        //Comprueba que se haya abierto el intent que deja pasar a Home
        Intents.intended(IntentMatchers.hasComponent(Seccion_Home.class.getName()));
        SystemClock.sleep(2000);

        //Abre el menú
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        SystemClock.sleep(2000);

        //Hace click en la sección per validar
        onView(ViewMatchers.withId(R.id.nav_per_validar)).perform(click());
        SystemClock.sleep(2000);

        //Comprueba que se haya abierto el intent que deja pasar a la pantalla de reportar incidencia
        Intents.intended(IntentMatchers.hasComponent(Seccion_Per_Validar.class.getName()));
        SystemClock.sleep(2000);

        //Espera a que aparezca el RecyclerView
        onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()));
        SystemClock.sleep(2000);

        //Comprueba que tenga al menos un item visible
        onView(withId(R.id.recyclerView))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        SystemClock.sleep(2000);

        //Abre el menú
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        SystemClock.sleep(2000);

        //Hace click en la sección obertes
        onView(ViewMatchers.withId(R.id.nav_oberta)).perform(click());
        SystemClock.sleep(2000);

        //Comprueba que se haya abierto el intent que deja pasar a la pantalla de reportar incidencia
        Intents.intended(IntentMatchers.hasComponent(Seccion_Per_Validar.class.getName()));
        SystemClock.sleep(2000);

        //Espera a que aparezca el RecyclerView
        onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()));
        SystemClock.sleep(2000);

        //Comprueba que tenga al menos un item visible
        onView(withId(R.id.recyclerView))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        SystemClock.sleep(2000);

        //Abre el menú
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        SystemClock.sleep(2000);

        //Hace click en la sección en progres
        onView(ViewMatchers.withId(R.id.nav_en_progress)).perform(click());
        SystemClock.sleep(2000);

        //Comprueba que se haya abierto el intent que deja pasar a la pantalla de reportar incidencia
        Intents.intended(IntentMatchers.hasComponent(Seccion_Per_Validar.class.getName()));
        SystemClock.sleep(2000);

        //Espera a que aparezca el RecyclerView
        onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()));
        SystemClock.sleep(2000);

        //Comprueba que tenga al menos un item visible
        onView(withId(R.id.recyclerView))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        SystemClock.sleep(2000);

        //Abre el menú
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        SystemClock.sleep(2000);

        //Hace click en la sección resoltes
        onView(ViewMatchers.withId(R.id.nav_resolta)).perform(click());
        SystemClock.sleep(2000);

        //Comprueba que se haya abierto el intent que deja pasar a la pantalla de reportar incidencia
        Intents.intended(IntentMatchers.hasComponent(Seccion_Per_Validar.class.getName()));
        SystemClock.sleep(2000);

        //Espera a que aparezca el RecyclerView
        onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()));
        SystemClock.sleep(2000);

        //Comprueba que tenga al menos un item visible
        onView(withId(R.id.recyclerView))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        SystemClock.sleep(2000);

        //Abre el menú
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        SystemClock.sleep(2000);

        //Hace click en la sección tancades
        onView(ViewMatchers.withId(R.id.nav_tancada)).perform(click());
        SystemClock.sleep(2000);

        //Comprueba que se haya abierto el intent que deja pasar a la pantalla de reportar incidencia
        Intents.intended(IntentMatchers.hasComponent(Seccion_Per_Validar.class.getName()));
        SystemClock.sleep(2000);

        //Espera a que aparezca el RecyclerView
        onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()));
        SystemClock.sleep(2000);

        //Comprueba que tenga al menos un item visible
        onView(withId(R.id.recyclerView))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        SystemClock.sleep(2000);
    }
}
