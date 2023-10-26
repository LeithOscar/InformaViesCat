/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Home extends AppCompatActivity {

    private Button buttonLogout;

    //Variables del usuario
    private int id;
    private String name;
    private String userName;
    private String lastName;
    private String password;
    private boolean logged;
    private int rolId;


    //Se inicializa la clase que realiza conexiones
    ServerController serverController = new ServerController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Se obtiene el Intent que inició esta activity
        Intent intent = getIntent();

        //Se extrae del intent el json con la info del usuario
        String json_datos_usuario = intent.getStringExtra("json_datos_usuario");

        //Se parsean los datos del usuario
        try {
            JSONObject json = new JSONObject(json_datos_usuario);
            id = json.getInt("id");
            name = json.getString("name");
            userName = json.getString("userName");
            lastName = json.getString("lastName");
            password = json.getString("password");
            logged = json.getBoolean("logged");
            rolId = json.getInt("rolId");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Se inicializan los views
        buttonLogout = findViewById(R.id.buttonLogout);

        //Se añade al TextView un "Hola..." con el nombre del usuario
        TextView hola = findViewById(R.id.hola);
        hola.setText("Hola " + name);


        //Click listener para el botón de Logout
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    //Si se hace click en "atrás" volveríamos a la pantalla de bienvenida, es decir, es necesario un logout
    @Override
    public void onBackPressed() {
        logout();
    }

    //Todas las acciones del logout
    private void logout() {

        //Se lanza la función logout del servidor
        Log.d("Debug Vicent","Click en logout");
        serverController.logout(userName, password, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //TODO: Ahora mismo siempre se retorna código 200, si las credenciales no funcionan se retorna un 200 con body vacío. El response siempre va a ser successful.
                if (response.isSuccessful()) {

                    //Se guarda la respuesta del servidor en un string
                    String responseBody = response.body().string();
                    Log.d("Debug Vicent", "JSON recibido: " + responseBody);

                    //Si la respuesta está vacía
                    if (responseBody.length()==0){

                        //Significaría que alguien está dentro sin tener username y password válido, dejamos hacer el logout sin filtro
                        Log.d("Debug Vicent", "Logout con credenciales incorrectas");
                        Intent intent = new Intent(Home.this, MainActivity.class);
                        startActivity(intent);

                        //Si sí hay respuesta se entiende que las credenciales son correctas
                    } else {

                        //Se abre la primera pantalla de login y se pasa el json
                        Log.d("Debug Vicent", "Logout con credenciales correctas");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Muestra el Toast en el hilo principal
                                Toast.makeText(Home.this, "Adiós", Toast.LENGTH_LONG).show();
                            }
                        });

                        Intent intent = new Intent(Home.this, MainActivity.class);
                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Debug Vicent", "Error al hacer logout: " + e.getMessage());
            }
        });


    }

}