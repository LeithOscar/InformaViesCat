/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Checks.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class Modificar_Incidencias_Test {

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
    public void modificar_incidencias() {

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

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.btnEdit),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerView),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

        SystemClock.sleep(2000);

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editTextDescription), withText("Retencio de transit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.custom),
                                        0),
                                9),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("Descripció modificada"));

        SystemClock.sleep(2000);

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.buttonSave), withText("Guardar canvis"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.custom),
                                        0),
                                15),
                        isDisplayed()));
        materialButton2.perform(click());

        SystemClock.sleep(2000);

        //Espera a que aparezca el RecyclerView
        onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()));
        SystemClock.sleep(4000);

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editTextDescription), withText("Descripció modificada"),
                        childAtPosition(
                                childAtPosition(
                                        withId(androidx.appcompat.R.id.custom),
                                        0),
                                9),
                        isDisplayed()));
        SystemClock.sleep(2000);





    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }






}
