/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;

import static androidx.test.espresso.Espresso.onView;

import android.os.SystemClock;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class Crear_Incidencia_Test {

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


    /**
     * Escenario de prueba para la creación de un informe de incidencia.
     *
     * Pasos:
     * 1. Ingresar un nombre de usuario y contraseña válidos.
     * 2. Hacer clic en el botón de inicio de sesión.
     * 3. Abrir el cajón de navegación y seleccionar la opción de informe de incidencias.
     * 4. Completar los detalles de la incidencia (carretera, kilómetro, descripción).
     * 5. Seleccionar el nivel de urgencia.
     * 6. Confirmar el informe marcando la casilla de confirmación.
     * 7. Hacer clic en el botón de informar incidencia.
     * 8. Verificar la transición a la sección principal.
     */
    @Test
    public void Test_Crear_Incidencia() {

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

        //Hace click en la opción de Logout
        onView(ViewMatchers.withId(R.id.nav_reportar)).perform(click());
        SystemClock.sleep(2000);

        //Comprueba que se haya abierto el intent que deja pasar a la pantalla de reportar incidencia
        Intents.intended(IntentMatchers.hasComponent(Seccion_Reportar.class.getName()));
        SystemClock.sleep(2000);

        onView(ViewMatchers.withId(R.id.carreteraTextView)).perform(ViewActions.typeText("Ronda del Litoral"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce un kilometro
        onView(ViewMatchers.withId(R.id.kmTextView)).perform(ViewActions.typeText("12"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce una descripcion
        onView(ViewMatchers.withId(R.id.descriptionTextView)).perform(ViewActions.typeText("S'ha caigut un arbre"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Marca que el "no urgentge"
        onView(withId(R.id.noUrgentRadioButton)).perform(click());
        onView(withId(R.id.noUrgentRadioButton)).check(matches(isChecked()));
        SystemClock.sleep(2000);

        //Marca el checkbox de confirmacion
        Espresso.onView(ViewMatchers.withId(R.id.confirmacio)).perform(ViewActions.click());
        SystemClock.sleep(2000);

        //Click en el botón de reportar incidencia
        Espresso.onView(ViewMatchers.withId(R.id.reportar_incidencia_button)).perform(ViewActions.click());
        SystemClock.sleep(5000);

        //Comprueba que se haya abierto el intent que deja pasar a la pantalla home (que se abre tras reportar una incidencia)
        Intents.intended(IntentMatchers.hasComponent(Seccion_Home.class.getName()));
        SystemClock.sleep(2000);
    }

}
