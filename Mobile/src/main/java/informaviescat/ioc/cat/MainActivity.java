/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //Se crean los views
    private EditText editUsername;
    private EditText editPassword;
    private Button buttonLogin;
    private CheckBox recordam;

    //Se crean los SharedPreferences para guardar username y password con la opción "Recorda'm"
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Deshabilitar la animacion al abrir la actividad
        overridePendingTransition(0, 0);

        Log.d("Debug Vicent", "Pantalla de bienvenida cargada");

        //Se inicializan los views
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.button_login);
        recordam = findViewById(R.id.recordam);

        //Se inicializa la clase que realiza conexiones
        ServerController serverController = new ServerController();

        //Se inicializan los SharedPreferences
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Listener para el checkbox "Recorda'm"
        recordam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Se guarda o elimina el nombre de usuario y la contraseña según el estado del checkbox
                if (isChecked) {
                    //Guarda nombre de usuario y contraseña
                    editor.putString("username", editUsername.getText().toString());
                    editor.putString("password", editPassword.getText().toString());
                } else {
                    //Elimina nombre de usuario y contraseña
                    editor.remove("username");
                    editor.remove("password");
                }
                editor.apply();
            }
        });

        //Recupera desde SharedPreferencies los valores de login guardados si existen
        if (sharedPreferences.contains("username")) {
            editUsername.setText(sharedPreferences.getString("username", ""));
        }
        if (sharedPreferences.contains("password")) {
            editPassword.setText(sharedPreferences.getString("password", ""));
        }

        //Click listener para el botón de Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Se cogen los strings que haya en los textedits
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                Log.d("Debug Vicent", "Username capturado: " + username);
                Log.d("Debug Vicent", "Password capturado: " + password);

                //Se lanza la función "login" del serverController
                serverController.login(username, password, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        Log.d("Debug Vicent", "Login hecho con status code: " + response.code());

                        //Si se devuelve error 400 se retorna Toast para credenciales incorrectas
                        if (response.code() == 400) {

                            Log.d("Debug Vicent", "Login con credenciales incorrectas");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Muestra el Toast en el hilo principal
                                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        //Si se retorna 200 se guarda el json recibido por el servidor, que se pasa al Home intent
                        if (response.isSuccessful() && response.code()== 200) {

                            //Se guarda la respuesta del servidor en un string
                            String responseBody = response.body().string();
                            Log.d("Debug Vicent", "Login correcto");
                            Log.d("Debug Vicent", "JSON recibido: " + responseBody);
                            Intent intent = new Intent(MainActivity.this, Seccion_Home.class);
                            intent.putExtra("json_datos_usuario",responseBody);
                            startActivity(intent);
                        }
                    }

                    //Si la respuesta falla, enseñar un Toast informando al usuario
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("Debug Vicent", "Ha fallado error en la conexión: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Muestra el Toast en el hilo principal
                                Toast.makeText(MainActivity.this, "Error en la conexión, comprobar servidor", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            };
        });

        //Al hacer click en "done" al teclear el password se esconde el teclado
        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    //Cierra el teclado
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    //Función para abrir la actividad de Alta de un usuario, asociada al textView con texto "crear un compte"
    public void abrirAltaUsuario(View view) {
        Intent intent = new Intent(this, AltaUsuario.class);
        startActivity(intent);
    }
}