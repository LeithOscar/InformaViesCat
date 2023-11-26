/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.SystemClock;

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
public class Alta_Test {

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
     * Prueba el proceso de alta de usuario.
     *
     * Esta prueba realiza las siguientes acciones:
     * 1. Espera durante 2000 milisegundos (2 segundos).
     * 2. Hace clic en el botón "Crear un compte".
     * 3. Espera durante 5000 milisegundos (5 segundos).
     * 4. Verifica si se ha abierto la actividad de alta de usuario.
     * 5. Introduce un nombre en el campo correspondiente.
     * 6. Introduce un apellido en el campo correspondiente.
     * 7. Introduce un nombre de usuario en el campo correspondiente.
     * 8. Introduce un correo electrónico en el campo correspondiente.
     * 9. Introduce una contraseña en el campo correspondiente.
     * 10. Hace clic en aceptar el acuerdo de usuario.
     * 11. Espera durante 2000 milisegundos (2 segundos).
     * 12. Hace clic en el botón de registro.
     * 13. Espera durante 2000 milisegundos (2 segundos).
     * 14. Hace clic nuevamente en el botón de registro.
     *
     */
    @Test
    public void Test_Alta() {

        SystemClock.sleep(2000);

        //Click on "Crear un compte"
        Espresso.onView(ViewMatchers.withText("Crear un compte"))
                .perform(ViewActions.click());
        SystemClock.sleep(5000);

        //Comprueba si se ha abierto la actividad de alta de usuario
        Intents.intended(IntentMatchers.hasComponent(AltaUsuario.class.getName()));

        //Introduce un nombre
        Espresso.onView(ViewMatchers.withId(R.id.edit_nom)).perform(ViewActions.typeText("Vicent"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce un apellido
        Espresso.onView(ViewMatchers.withId(R.id.edit_cognom)).perform(ViewActions.typeText("Gil Esteve Testing"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce un username
        Espresso.onView(ViewMatchers.withId(R.id.edit_username)).perform(ViewActions.typeText("vicentgilesteve"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce un email
        Espresso.onView(ViewMatchers.withId(R.id.edit_email)).perform(ViewActions.typeText("vicentgilesteve@gmail.com"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce un password
        Espresso.onView(ViewMatchers.withId(R.id.edit_password)).perform(ViewActions.typeText("IOCBarcelona345?"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Hace click en aceptar el aula
        Espresso.onView(ViewMatchers.withId(R.id.eula)).perform(ViewActions.click());

        SystemClock.sleep(2000);

        //Hace click en el botón de registre
        Espresso.onView(ViewMatchers.withId(R.id.button_registre)).perform(ViewActions.click());

        SystemClock.sleep(2000);

        //Hace click en el botón de registre
        Espresso.onView(ViewMatchers.withId(R.id.button_registre)).perform(ViewActions.click());

    }
}
