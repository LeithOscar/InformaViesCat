/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.os.SystemClock;
@RunWith(AndroidJUnit4.class)
public class Login_Test {

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
     * Prueba la carga de la actividad de inicio de sesión.
     *
     * Esta prueba realiza las siguientes acciones:
     * 1. Espera 4 segundos.
     * 2. Verifica que la actividad de inicio de sesión se haya cargado correctamente.
     *
     */
    @Test
    public void Test_Login_Carga() {
        SystemClock.sleep(4000);
        //Verifica que la actividad se haya iniciado
        Espresso.onView(ViewMatchers.withText("Benvingut/uda"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }

    /**
     * Prueba el inicio de sesión con credenciales correctas.
     *
     * Esta prueba realiza las siguientes acciones:
     * 1. Introduce el nombre de usuario "juanperez" en el campo de usuario, cierra el teclado y espera 2 segundos.
     * 2. Introduce la contraseña "1234" en el campo de contraseña, cierra el teclado y espera 2 segundos.
     * 3. Hace clic en el botón de inicio de sesión.
     * 4. Espera 10 segundos.
     * 5. Comprueba que se haya abierto la actividad "Home" después de un inicio de sesión exitoso.
     *
     */
    @Test
    public void Test_Crenciales_Correctas() {
        //Introduce en "username" a "juanperez", cierra el teclado y espera
        Espresso.onView(ViewMatchers.withId(R.id.edit_username)).perform(ViewActions.typeText("juanperez"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce en "password" la contraseña correcta, cierra el teclado y espera
        Espresso.onView(ViewMatchers.withId(R.id.edit_password)).perform(ViewActions.typeText("1234"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Hace click en el botón de login
        Espresso.onView(ViewMatchers.withId(R.id.button_login)).perform(ViewActions.click());
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(10000);

        //Comprueba que se haya abierto el intent que deja pasar a Incidencies Home
        Intents.intended(IntentMatchers.hasComponent(Home.class.getName()));
    }

    /**
     * Prueba el inicio de sesión con credenciales incorrectas.
     *
     * Esta prueba realiza las siguientes acciones:
     * 1. Introduce un nombre de usuario inexistente en el campo de usuario, cierra el teclado y espera 2 segundos.
     * 2. Introduce una contraseña incorrecta en el campo de contraseña, cierra el teclado y espera 2 segundos.
     * 3. Hace clic en el botón de inicio de sesión.
     * 4. Espera 5 segundos.
     * 5. Comprueba que el botón de inicio de sesión sigue visible después de 5 segundos, es decir, que el login no ha funcionado.
     *
     */
    @Test
    public void Test_Crenciales_Incorrectas() {
        //Introduce en "username" un login que no existe
        Espresso.onView(ViewMatchers.withId(R.id.edit_username)).perform(ViewActions.typeText("loginincorrecto"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce en "password" una contraseña incorrecta
        Espresso.onView(ViewMatchers.withId(R.id.edit_password)).perform(ViewActions.typeText("passwordincorrecto"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Hace click en el botón de login
        Espresso.onView(ViewMatchers.withId(R.id.button_login)).perform(ViewActions.click());
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(5000);

        //5 segundos despues intenta buscar el botón de login, para inidcar que no se ha entrado en la acitividad
        Espresso.onView(ViewMatchers.withId(R.id.button_login))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}