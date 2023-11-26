/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
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
import androidx.test.espresso.contrib.DrawerActions;


@RunWith(AndroidJUnit4.class)
public class Logout_Test {

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
     * Prueba el proceso de cierre de sesión (logout) en la aplicación.
     *
     * Esta prueba realiza las siguientes acciones:
     * 1. Espera 2 segundos.
     * 2. Introduce el username correcta en el campo de usuario, cierra el teclado y espera 2 segundos.
     * 3. Introduce la contraseña correcta en el campo de contraseña, cierra el teclado y espera 2 segundos.
     * 4. Hace clic en el botón de login.
     * 5. Espera 5 segundos.
     * 6. Comprueba que se haya abierto la actividad "Home" después de iniciar sesión.
     * 7. Espera 5 segundos.
     * 8. Hace clic en el botón de cierre de sesión (Logout).
     * 9. Espera 5 segundos.
     * 10. Comprueba que se haya abierto la actividad "MainActivity" después de cerrar la sesión.
     * 11. Espera 5 segundos.
     */
    @Test
    public void Test_Logout() {

        SystemClock.sleep(2000);

        //Introduce en "password" la contraseña correcta, cierra el teclado y espera
        Espresso.onView(withId(R.id.edit_username)).perform(ViewActions.typeText("juanperez"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Introduce en "password" la contraseña correcta, cierra el teclado y espera
        Espresso.onView(withId(R.id.edit_password)).perform(ViewActions.typeText("1234"));
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(2000);

        //Hace click en el botón de login
        Espresso.onView(withId(R.id.button_login)).perform(click());
        Espresso.closeSoftKeyboard();
        SystemClock.sleep(5000);

        //Comprueba que se haya abierto el intent que deja pasar a Home
        Intents.intended(IntentMatchers.hasComponent(Seccion_Home.class.getName()));
        SystemClock.sleep(2000);

        //Abre el menú
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
                .perform(DrawerActions.open());

        //Hace click en la opción de Logout
        Espresso.onView(ViewMatchers.withId(R.id.nav_logout))
                .perform(ViewActions.click());

        //Comprueba que se haya abierto el intent que deja pasar de nuevo a la WelcomePage
        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
        SystemClock.sleep(5000);

    }

}
