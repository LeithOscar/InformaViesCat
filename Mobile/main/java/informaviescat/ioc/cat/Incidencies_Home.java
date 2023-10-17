package informaviescat.ioc.cat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import informaviescat.ioc.cat.databinding.ActivityIncidenciesHomeBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public class Incidencies_Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityIncidenciesHomeBinding binding;

    //Variables del usuario
    private int id;
    private String name;
    private String userName;
    private String lastName;
    private String password;
    private boolean logged;
    private int rolId;

    //Instancia de ServerController en la actividad;
    ServerController serverController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIncidenciesHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = findViewById(R.id.toolbar);

        //Ponemos el título en el actionbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("InformaVies CAT");

        //Se inicializa la clase que realiza conexiones
        ServerController serverController = new ServerController();

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

        setSupportActionBar(binding.appBarIncidenciesHome.toolbar);
        binding.appBarIncidenciesHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_incidencies_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.incidencies__home, menu);
        return true;
    }

    //Función que se ejecuta si se utiliza una de las opciones del menú superior-derecho. En nuestra app solo es el logout.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Se detecta en qué botón se ha hecho click (Realmente solo hay uno)
        int id = item.getItemId();

        //Sí se trata del botón "Sortir"...
        if (id == R.id.sortir) {

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
                            Intent intent = new Intent(Incidencies_Home.this, MainActivity.class);
                            startActivity(intent);

                        //Si sí hay respuesta se entiende que las credenciales son correctas
                        } else {

                            //Se abre la primera pantalla de login y se pasa el json
                            Log.d("Debug Vicent", "Logout con credenciales correctas");
                            Intent intent = new Intent(Incidencies_Home.this, MainActivity.class);
                            startActivity(intent);
                        }

                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("Debug Vicent", "Error al hacer logout: " + e.getMessage());
                }
            });



            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_incidencies_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }






}