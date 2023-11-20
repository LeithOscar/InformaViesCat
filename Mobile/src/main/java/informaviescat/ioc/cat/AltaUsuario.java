package informaviescat.ioc.cat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AltaUsuario extends AppCompatActivity {


    //Se crean los TextInputs
    private EditText editNom;
    private EditText editCognom;
    private EditText editUsername;
    private EditText editEmail;
    private EditText editPassword;
    private CheckBox eulaCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_usuario);

        //Deshabilitar la animacion al abrir la actividad
        overridePendingTransition(0, 0);

        Log.d("Debug Vicent", "Pantalla de alta d'usuari cargada");

        //Bloque para controlar el hipervinculo de los terms and conditions
            CheckBox checkBox = findViewById(R.id.eula);

            // Create a SpannableString for the checkbox text
            SpannableString spannableString = new SpannableString("Estic d'acord amb els " + "termes i condicions");

            //Crea un ClickableSpan para la linea
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    // Define the URL to open in the browser
                    String url = "https://transit.gencat.cat/ca/el_servei/condicions_d_us/app_transit/catala/";

                    // Create an intent to open the URL in a browser
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            };

            //Hace clicacable solo la parte "termes i condicions"
            int startIndex = spannableString.toString().indexOf("termes i condicions");
            int endIndex = startIndex + "termes i condicions".length();
            spannableString.setSpan(clickableSpan, startIndex, endIndex, 0);

            //El texto aparece cómo link
            checkBox.setText(spannableString);
            checkBox.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

        //Se inicializan los TexInputs i el Checkbox
        editNom = findViewById(R.id.edit_nom);
        editCognom = findViewById(R.id.edit_cognom);
        editUsername = findViewById(R.id.edit_username);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        eulaCheckBox = findViewById(R.id.eula);

        //Se inicializa el botón Registre
        Button registreButton = findViewById(R.id.button_registre);

        //El botón se inicializa deshabilitado
        registreButton.setEnabled(false);

        //Listener para el checkbox
        eulaCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //Si "isChecked" el eulaCheckBox, entonces el botón de registro se habilita
                registreButton.setEnabled(isChecked);
            }
        });

        //Se inicializa la clase que realiza conexiones
        ServerController serverController = new ServerController();

        //Click listener para el botón
        registreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Se capturan los valores
                String nom = editNom.getText().toString();
                String cognom = editCognom.getText().toString();
                String username = editUsername.getText().toString();
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                boolean eulaAccepted = eulaCheckBox.isChecked();

                //Comprueba que el nombre no tenga más de 30 caracteres
                if (nom.length() < 2 || nom.length() > 30) {
                    Toast.makeText(AltaUsuario.this, "El nom ha de tenir entre 2 i 30 caràcters", Toast.LENGTH_SHORT).show();
                    editNom.setError("El nom ha de tenir entre 2 i 30 caràcters");
                    editNom.requestFocus();
                    return;
                }

                //Comprueba que el apellido tenga entre 2 i 30 carácteres
                if (cognom.length() < 2 || cognom.length() > 30) {
                    // Check if the length of the last name is less than 2 or greater than 30
                    Toast.makeText(AltaUsuario.this, "El cognom ha de tenir entre 2 i 30 caràcters", Toast.LENGTH_SHORT).show();
                    editCognom.setError("El cognom ha de tenir entre 2 i 30 caràcters");
                    editCognom.requestFocus();
                    return;
                }

                //Comprueba que el username no tenga más de 15 caracteres
                if (username.length() > 15) {
                    Toast.makeText(AltaUsuario.this, "L'usuari ha de tenir menys de 15 caràcters", Toast.LENGTH_SHORT).show();
                    editUsername.setError("L'usuari ha de tenir menys de 15 caràcters");
                    editUsername.requestFocus();
                    return;
                }

                //Comprueba el formato de la contrasenya
                if (!isValidPassword(password)) {
                    Toast.makeText(AltaUsuario.this, "La contrasenya ha de tenir almenys una lletra majúscula, un número i un símbol", Toast.LENGTH_SHORT).show();
                    editPassword.setError("La contrasenya ha de tenir almenys una lletra majúscula, un número i un símbol");
                    editPassword.requestFocus();
                    return;
                }

                if (isValidEmail(email)) {

                    //El correo es válido
                    String result = "Nom: " + nom + "\n" +
                            "Cognoms: " + cognom + "\n" +
                            "Username: " + username + "\n" +
                            "Email: " + email + "\n" +
                            "Password: " + password + "\n" +
                            "EULA aceptado: " + eulaAccepted;
                    Log.d("Debug Vicent", "Detalles del usuario capturados: + \n" + result);



                } else {
                    // Email is invalid, display a Toast and change EditText color
                    Toast.makeText(AltaUsuario.this, "Format de email incorrecte", Toast.LENGTH_SHORT).show();
                    editEmail.setError("Format de email incorrecte");
                    editEmail.requestFocus();
                    return;
                }

                //Proceder con el registro
                serverController.register(nom, cognom, username, email, password, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        //Toast con error de conexión
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Muestra el Toast en el hilo principal
                                Toast.makeText(AltaUsuario.this, "Error de conexión", Toast.LENGTH_LONG).show();
                            }
                        });
                        Log.d("Debug Vicent", "Error en la conexión: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Muestra el Toast en el hilo principal
                                Toast.makeText(AltaUsuario.this, "Compte creat correctament. Fes login amb les noves credencials.", Toast.LENGTH_LONG).show();
                            }
                        });
                        Log.d("Debug Vicent", "Compte creat correctament. Fes login amb les noves credencials");

                        Intent intent = new Intent(AltaUsuario.this,MainActivity.class);
                        startActivity(intent);
                    }
                });


            }
        });

    }

    //Función para comprobar con regex si el email tiene el formato correcto
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }

    //Función para comprobar que la contraseña tiene al menos una mayúscula, un número y un símbolo
    private boolean isValidPassword(String password) {
        // The password must contain at least one uppercase letter, one number, and one symbol
        String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$?¿%^&+=!])(?!.*\\s).{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }
}