package informaviescat.ioc.cat;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Utility {

    private static final String TAG = "Utility";

    /**
     * Realiza el proceso de logout para un usuario.
     *
     * @param activity La actividad actual desde la cual se realiza el logout.
     * @param userId   El ID del usuario que está realizando el logout.
     * @param sessionId El ID de la sesión del usuario que se utilizará para el logout.
     */
    public static void logout(Activity activity, int userId, String sessionId) {

        //Se inicializa conexión con el servidor
        ServerController serverController = new ServerController();

        //Se llama a logout en el servidor
        serverController.logout(String.valueOf(userId), sessionId, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //Si hay respuesta
                if (response.isSuccessful()) {

                    //Se guarda respuesta del servidor para logs
                    String responseBody = response.body().string();
                    Log.d(TAG, "Logout response: " + responseBody);

                    //Tanto si la respuesta esta vacía cómo si es "true" se permitirá el logout
                    if (responseBody.length() == 0 || responseBody.equals("true")) {

                        //Se muestra un Toast de despedida y se arrance la main_activity donde está el login
                        Utility.mostrarToast(activity, "Gràcies per utilitzar InformaVies CAT. Adèu!");

                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error during logout: " + e.getMessage());
            }
        });
    }


    public static void mostrarToast(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}