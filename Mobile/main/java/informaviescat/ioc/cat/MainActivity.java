package informaviescat.ioc.cat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //Se crean los views
    private EditText editUsername;
    private EditText editPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Debug Vicent", "Pantalla de bienvenida cargada");

        //Se inicializan
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        buttonLogin = findViewById(R.id.button_login);

        //Click listener para el botón de Login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Se inicializa la clase que realiza conexiones
                ServerController serverController = new ServerController();

                //Se cogen los strings que haya en los textedits
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                Log.d("Debug Vicent", "Username capturado: " + username);
                Log.d("Debug Vicent", "Password capturado: " + password);

                //Se lanza la función "login" del serverController
                serverController.login(username, password, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        //TODO: Ahora mismo siempre se retorna código 200, si las credenciales no funcionan se retorna un 200 con body vacío. El response siempre va a ser successful.
                        if (response.isSuccessful()) {

                            //Se guarda la respuesta del servidor en un string
                            String responseBody = response.body().string();
                            Log.d("Debug Vicent", "JSON recibido: " + responseBody);

                            //Si la respuesta está vacía
                            if (responseBody.length()==0){
                                Log.d("Debug Vicent", "Credenciales incorrectas");

                            //Si sí hay respuesta se entiende que las credenciales son correctas
                            } else {

                                //Se abre la actividad principal "Incidencies_Home" y se pasa el json
                                Log.d("Debug Vicent", "Login con credenciales correctas");
                                Intent intent = new Intent(MainActivity.this, Incidencies_Home.class);
                                intent.putExtra("json_datos_usuario",responseBody);
                                startActivity(intent);
                            }
                        } else {
                            Log.d("Debug Vicent", "Login ha fallado");
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("Debug Vicent", "Error en la conexión: " + e.getMessage());
                    }
                });
            };
        });
    }

}