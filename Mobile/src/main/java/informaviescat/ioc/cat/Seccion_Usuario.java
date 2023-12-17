/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Seccion_Usuario extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    //Variables del usuario
    private int id;
    private String name;
    private String userName;
    private String lastName;
    private String email;
    private String password;
    private boolean logged;
    private int rolId;
    private String session_id;
    private String json_datos_usuario;
    Activity activity_de_origen = Seccion_Usuario.this;


    //Se inicializa la clase que realiza conexiones
    ServerController serverController = new ServerController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_usuario);

        //Deshabilitar la animacion al abrir la actividad
        overridePendingTransition(0, 0);

        //Menú NavigationDrawer (el lateral) más la cabecera
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Drawable drawable = toolbar.getNavigationIcon();
        toolbar.setNavigationIcon(drawable);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.obre_menu, R.string.tanca_menu);
        int color = ContextCompat.getColor(this, R.color.white);
        toggle.getDrawerArrowDrawable().setColor(color);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Se obtiene el Intent que inició esta activity
        Intent intent = getIntent();

        //Se extrae del intent el json con la info del usuario
        json_datos_usuario = intent.getStringExtra("json_datos_usuario");

        //Se parsean los datos del usuario
        try {
            JSONObject json = new JSONObject(json_datos_usuario);

            //Extrae primero el usuario
            JSONObject userObject = json.getJSONObject("user");

            //Extrae todos los valores dentro de usuario
            id = userObject.getInt("id");
            name = userObject.getString("name");
            userName = userObject.getString("username");
            lastName = userObject.getString("lastname");
            email = userObject.getString("email");
            password = userObject.getString("password");
            rolId = userObject.getInt("rolid");

            //Extrae el userId
            session_id = json.getString("sessionId");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        Menu navMenu = navigationView.getMenu();
        MenuItem perValidarItem = navMenu.findItem(R.id.nav_per_validar);
        MenuItem resoltesItem = navMenu.findItem(R.id.nav_resolta);
        MenuItem tancadesItem = navMenu.findItem(R.id.nav_tancada);

        //Se busca el floating button para añadir una incidencia
        FloatingActionButton afegirIncidenciaButton = findViewById(R.id.reportarIncidenciaButton);

        //Click listener para el floating button,que tendrá que abrir la actividad para reportar incidencias
        afegirIncidenciaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Reportar.class, json_datos_usuario);
            }
        });

        //Se inicializan los TextEdits
        TextInputEditText nomEditText = findViewById(R.id.nomTextView);
        TextInputEditText cognomEditText = findViewById(R.id.cognomTextView);
        TextInputEditText usernameEditText = findViewById(R.id.usernameTextView);
        TextInputEditText emailEditText = findViewById(R.id.emailTextView);
        TextInputEditText passwordEditText = findViewById(R.id.passwordTextView);

        //Se pone en ellos los detalles del usuario directamente
        nomEditText.setText(name);
        cognomEditText.setText(lastName);
        usernameEditText.setText(userName);
        emailEditText.setText(email);
        passwordEditText.setText(password);

        if (rolId == 2) {

            //Si el rol es 2 (técnico) se deshabilitan las secciones que son solo para admins, o ciudadanos.
            Log.d("Debug Vicent","Rol es: " + rolId + " (técnico) .Se esconden las secciones per validar, resolta y tancada.");
            perValidarItem.setVisible(false);
            resoltesItem.setVisible(false);
            tancadesItem.setVisible(false);

            //Se deshabilitan también los campos para editar los detalles de usuario
            nomEditText.setEnabled(false);
            cognomEditText.setEnabled(false);
            usernameEditText.setEnabled(false);
            emailEditText.setEnabled(false);
            passwordEditText.setEnabled(false);

            //Se cambia el texto para informar de que no se puede editar los detalles de usuario
            TextView informativoTextView = findViewById(R.id.texto_informativo_modificar_usuario);
            informativoTextView.setText("No disposes de permissos per editar al teu compte amb rol tècnic de carretera. Contacta amb el teu administrador.");
            TextView warningTextView = findViewById(R.id.warning_modificar_usuario);
            warningTextView.setVisibility(View.INVISIBLE);

            //Se deshabilita el botón de modificar usuario
            MaterialButton modificarButton = findViewById(R.id.button_modificar_usuari);
            modificarButton.setVisibility(View.INVISIBLE);
            modificarButton.setEnabled(false);

            //Se deshabilita el botón de eliminar usuario
            MaterialButton eliminarButton = findViewById(R.id.button_eliminar_usuari);
            eliminarButton.setVisibility(View.INVISIBLE);
            eliminarButton.setEnabled(false);
        }

        //Listener para el botón de modificar
        MaterialButton modificarButton = findViewById(R.id.button_modificar_usuari);
        modificarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Se captura el texto que haya en los TextEdits en ese momento
                String nom_edit = nomEditText.getText().toString();
                String cognom_edit = cognomEditText.getText().toString();
                String username_edit = usernameEditText.getText().toString();
                String email_edit = emailEditText.getText().toString();
                String password_edit = passwordEditText.getText().toString();

                serverController.modificarUsuario(id, rolId, nom_edit, cognom_edit, username_edit, email_edit, password_edit, session_id, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d("Debug Vicent", "Fallo al modificar el usuario");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Muestra el Toast en el hilo principal
                                Toast.makeText(Seccion_Usuario.this, "No ha podido moodificarse el perfil", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    //Si hay respuesta se edita el JSON con los datos del usuario
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            //Si es con código 200
                            Log.d("Debug Vicent", "Perfil modificado");
                            updateJsonDatosUsuario(nom_edit, cognom_edit, username_edit, email_edit, password_edit);

                        //Si es con código 304
                        } else if (response.code() == 304) {

                            Log.d("Debug Vicent", "Sección usuario: Response con error 304 al modificar el perfil");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Muestra el Toast en el hilo principal
                                    Toast.makeText(Seccion_Usuario.this, "Error al modificar el perfil", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            //Otros posibles errores
                            Log.d("Debug Vicent", "Sección usuario: Respuesta inesperada con código: " + response.code());
                        }

                        updateJsonDatosUsuario(nom_edit, cognom_edit, username_edit, email_edit, password_edit);
                    }
                });
            }
        });

        //Listener para el botón de eliminar
        MaterialButton eliminarButton = findViewById(R.id.button_eliminar_usuari);
        eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverController.eliminarUsuario(id, session_id, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d("Debug Vicent", "Fallo al eliminar el usuario");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Muestra el Toast en el hilo principal
                                Toast.makeText(Seccion_Usuario.this, "No ha podido eliminarse el perfil", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.d("Debug Vicent", "Usuario eliminado");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Muestra el Toast en el hilo principal
                                Toast.makeText(Seccion_Usuario.this, "Perfil eliminado", Toast.LENGTH_LONG).show();

                                //Se vacían los datoa de usuario y se pasa a la pantalla de login
                                json_datos_usuario = "";
                                abrirActividadPasandoJSONUsuario(activity_de_origen,  MainActivity.class, json_datos_usuario);
                            }
                        });
                    }
                });
            }
        });
    }


    //Función para actualizar el json con los datos del usuario tras utilizar "Modificar el meu compte"
    private void updateJsonDatosUsuario(String nom, String cognom, String username, String email, String password) {
        try {
            //Se parsea el JSON actual
            JSONObject json = new JSONObject(json_datos_usuario);

            //Se actualiza con los nuevos datos
            JSONObject userObject = json.getJSONObject("user");
            userObject.put("name", nom);
            userObject.put("lastName", cognom);
            userObject.put("userName", username);
            userObject.put("email", email);
            userObject.put("password", password);

            //Se guarda
            json_datos_usuario = json.toString();

            //Toast de feedback para el usuario
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast("Perfil actualitzat correctament");
                }
            });

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Seccion_Usuario.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Home.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_reportar) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Reportar.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_per_validar) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Per_Validar.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_oberta) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Obertes.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_en_progress) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_En_Progress.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_resolta) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Resoltes.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_tancada) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Tancades.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_usuari) {

        } else if (item.getItemId() == R.id.nav_logout) {
            Utility.logout(this, id, session_id);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void abrirActividadPasandoJSONUsuario(Activity activity, Class<?> destinationActivity, String jsonData) {
        //Crea un intent de la actual Activity a la de destino
        Intent intent = new Intent(activity, destinationActivity);

        //Pasa los datos del actual usuario
        intent.putExtra("json_datos_usuario", jsonData);

        //Arranca el intent
        activity.startActivity(intent);
    }

    //Si se hace click en "atrás" volveríamos a la pantalla de bienvenida, es decir, es necesario un logout
    @Override
    public void onBackPressed() {
        Utility.logout(this, id, session_id);
    }

}