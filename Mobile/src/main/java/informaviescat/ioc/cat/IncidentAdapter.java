/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder> {
    private List<Incidencia> incidentList;
    private int rolId;
    private String session_id;
    ServerController serverController = new ServerController();

    public IncidentAdapter(List<Incidencia> incidentList, int rolId, String session_id) {
        this.incidentList = incidentList;
        this.rolId = rolId;
        this.session_id = session_id;
    }

    private String currentSortingOption = "Carretera";

    @NonNull
    @Override
    public IncidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incident, parent, false);
        return new IncidentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentViewHolder holder, int position) {
        Incidencia incident = incidentList.get(position);
        holder.bind(incident);
    }

    @Override
    public int getItemCount() {
        return incidentList.size();
    }

    public class IncidentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewId;
        private TextView textViewCarretera;
        private TextView textViewKm;
        private TextView textViewDescription;
        private TextView textViewStartDate;
        private ImageView btnEdit;
        List<String> yourIncidentTypeList = Arrays.asList("Per validar", "Onerta", "En progrés", "Resolta", "Tancada");

        public IncidentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.textView_Id);
            textViewCarretera = itemView.findViewById(R.id.textView_Carretera);
            textViewKm = itemView.findViewById(R.id.textView_Km);
            textViewDescription = itemView.findViewById(R.id.textView_Description);
            textViewStartDate = itemView.findViewById(R.id.textView_StartDate);

            // Listener del botón de editar
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnEdit.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    showEditPopup(incidentList.get(position));
                }
            });


        }

        public void bind(Incidencia incident) {
            textViewId.setText("ID de la incidència: " + incident.getId());
            textViewCarretera.setText("Carretera: " + incident.getRoadName());
            textViewKm.setText("Km: " + incident.getKm());
            textViewDescription.setText("Descripció: " + incident.getDescription());
            textViewStartDate.setText("Data d'inici: " + incident.getStartDate());

        }

        private void showEditPopup(Incidencia incident) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            View popupView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.popup_layout, null);
            builder.setView(popupView);

            //Se inicializan los campos
            EditText editTextRoadName = popupView.findViewById(R.id.editTextRoadName);
            EditText editTextKm = popupView.findViewById(R.id.editTextKm);
            EditText editTextDescription = popupView.findViewById(R.id.editTextDescription);
            Button buttonSave = popupView.findViewById(R.id.buttonSave);
            RadioGroup radioGroupUrgent = popupView.findViewById(R.id.radioGroupUrgent);
            RadioButton radioButtonTrue = popupView.findViewById(R.id.radioButtonTrue);
            RadioButton radioButtonFalse = popupView.findViewById(R.id.radioButtonFalse);
            EditText editTextStartDate = popupView.findViewById(R.id.editTextStartDate);
            EditText editTextEndDate = popupView.findViewById(R.id.editTextEndDate);
            EditText editTextTecnic = popupView.findViewById(R.id.editTextTecnic);
            Spinner spinnerIncidentType = popupView.findViewById(R.id.spinnerIncidentType);
            ArrayAdapter<String> incidentTypeAdapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_dropdown_item, yourIncidentTypeList);
            spinnerIncidentType.setAdapter(incidentTypeAdapter);

            //Se ponen los valores actuales
            editTextRoadName.setText(incident.getRoadName());
            editTextKm.setText(incident.getKm());
            editTextDescription.setText(incident.getDescription());
            boolean isUrgent = incident.isUrgent();
            radioButtonTrue.setChecked(isUrgent);
            radioButtonFalse.setChecked(!isUrgent);
            editTextStartDate.setText(incident.getStartDate());
            editTextEndDate.setText(incident.getEndDate());
            editTextTecnic.setText(String.valueOf(incident.getTecnicId()));
            spinnerIncidentType.setSelection(getIndex(yourIncidentTypeList, String.valueOf(incident.getIncidentTypeId())));


            //Estos son los valores que deshabilitamos por que el ciudadano no puede cambiarlos
            if (rolId == 3) {
                editTextStartDate.setEnabled(false);
                editTextEndDate.setEnabled(false);
                editTextTecnic.setEnabled(false);
                spinnerIncidentType.setEnabled(false);
            }

            //Popup
            AlertDialog dialog = builder.create();

            //Listener del botón de guardar cambios
            buttonSave.setOnClickListener(view -> {

                //Se capturan los nuevos valores y se colocan en el array de incidentes
                String newRoadName = editTextRoadName.getText().toString();
                String newKm = editTextKm.getText().toString();
                String newDescription = editTextDescription.getText().toString();
                String newStartDate = editTextStartDate.getText().toString();
                String newEndDate = editTextEndDate.getText().toString();
                int newTecnic = Integer.parseInt(editTextTecnic.getText().toString());
                incident.setRoadName(newRoadName);
                incident.setKm(newKm);
                incident.setDescription(newDescription);
                incident.setStartDate(newStartDate);
                incident.setEndDate(newEndDate);
                incident.setTecnicId(newTecnic);
                String selectedIncidentTypeId = spinnerIncidentType.getSelectedItem().toString();
                int selectedIndex = yourIncidentTypeList.indexOf(selectedIncidentTypeId);


                //Se modifica la incidencia que hubiera abierta con los nuevos campos (el id, el userId, el type no pueden cambiarse
                serverController.modificarIncidencia(incident.getId(), incident.getUserId(), newTecnic, selectedIndex+1 ,newRoadName, newKm, incident.getGeo(), newDescription, newStartDate, newEndDate, isUrgent, session_id, new Callback() {
                        @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.d("Debug Vicent","Edición correcta de la incidencia");
                        }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d("Debug Vicent","Error al modificar la incidencia");
                    }
                });
                notifyDataSetChanged();
                dialog.dismiss();

            });

            //Muestra el popup
            dialog.show();
        }
    }

    private int getIndex(List<String> incidentTypeList, String incidentTypeId) {
        for (int i = 0; i < incidentTypeList.size(); i++) {
            if (incidentTypeList.get(i).equals(incidentTypeId)) {
                return i;
            }
        }
        return 0;
    }

    public void sortBy(String sortingOption) {
        //Se captura la posición actual
        currentSortingOption = sortingOption;

        //Logica para ordenar con Collections dependiendo de la opción escogida en el spinner
        switch (sortingOption) {
            case "Carretera":
                //
                Collections.sort(incidentList, new Comparator<Incidencia>() {
                    @Override
                    public int compare(Incidencia o1, Incidencia o2) {
                        return o1.getRoadName().compareToIgnoreCase(o2.getRoadName());
                    }
                });
                break;

            case "Descripció":
                Collections.sort(incidentList, new Comparator<Incidencia>() {
                    @Override
                    public int compare(Incidencia o1, Incidencia o2) {
                        return o1.getDescription().compareTo(o2.getDescription());
                    }
                });
                break;

            case "Id":
                Collections.sort(incidentList, new Comparator<Incidencia>() {
                    @Override
                    public int compare(Incidencia o1, Incidencia o2) {
                        return o1.getId() - o2.getId();
                    }
                });
                break;

            case "Km":
                Collections.sort(incidentList, new Comparator<Incidencia>() {
                    @Override
                    public int compare(Incidencia o1, Incidencia o2) {
                        return o1.getKm().compareTo(o2.getKm());
                    }
                });
                break;

            case "Data d'inici":
                Collections.sort(incidentList, new Comparator<Incidencia>() {
                    @Override
                    public int compare(Incidencia o1, Incidencia o2) {
                        return o1.getStartDate().compareTo(o2.getStartDate());
                    }
                });
                break;

            default:
                Collections.sort(incidentList, new Comparator<Incidencia>() {
                    @Override
                    public int compare(Incidencia o1, Incidencia o2) {
                        return o1.getStartDate().compareTo(o2.getStartDate());
                    }
                });
                break;
        }
        notifyDataSetChanged();
    }
}
