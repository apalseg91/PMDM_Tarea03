package dam.pmdm.practicanavigationyoutube;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Fragment que carga y muestra el detalle de los datos del pokemon que formaparte del equipo.
 * Muestra: Imagen, nombre, id, tipo(s), peso y altura.
 * Emplea un botón para volver al fragment que contiene el resto de pokemon del equipo.
 */
public class DetailsFragment extends Fragment {
    /**
     * Declaración de variables que representan los elementos de la pantalla de detalles.
     */
    private ImageView imageDetails;
    private TextView idDetails, nameDetails, typeDetails, heightDetails, weightDetails;

    /**
     * Método que carga los elementos en la pantalla de detalles
     *
     * @param inflater           El layoutinflater es un objeto que alberga los elementos de la pantalla.
     * @param container          El elemento padre al que debe estar vinculado el layout
     * @param savedInstanceState contiene los datos que albergan los elementos de la pantalla
     * @return la vista con los elementos ya dotados de contenido
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //creamos el elemento;
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        //inicialización de variables
        imageDetails = view.findViewById(R.id.imageDetails);
        idDetails = view.findViewById(R.id.idDetails);
        nameDetails = view.findViewById(R.id.nameDetails);
        typeDetails = view.findViewById(R.id.typeDetails);
        heightDetails = view.findViewById(R.id.heightDetails);
        weightDetails = view.findViewById(R.id.weightDetails);
        Button backButton = view.findViewById(R.id.backToTeamButton);
        //manejo del botón para volver al fragmento de Equipo
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        //llamada al método que carga la info de los detalles del pokemon
        loadDetails();
        //devuelvo la pantalla
        return view;
    }

    /**
     * Método que recibe los datos desde el TeamFragment y los carga en este fragment.
     */
    private void loadDetails() {
// Compruebo que el objeto Bundle con la info no es nulo
        if (getArguments() != null) {
            Bundle args = getArguments();
            // Cojo la info del bundle, fijo valores por defecto por si hay problemas con el Bundle
            int id = args.getInt("pokemon_id", -1);
            String name = args.getString("pokemon_name", "Unknown");
            String imageUrl = args.getString("pokemon_image", "");
            String types = args.getString("pokemon_types", "Unknown"); // Ya tienes los tipos aquí
            int height = args.getInt("pokemon_height", -1);
            int weight = args.getInt("pokemon_weight", -1);

            // Asigno los valores a los elementos que han de mostrarlos
            idDetails.setText(getString(R.string.id) + id);
            nameDetails.setText(name);
            heightDetails.setText(String.valueOf(height)+getString(R.string.kg));
           weightDetails.setText(String.valueOf(weight)+getString(R.string.cm));

            // Traducir los tipos del Pokémon
            StringBuilder translatedTypes = new StringBuilder();
            String[] typesArray = types.split(", ");
            for (String type : typesArray) {
                if (translatedTypes.length() > 0) {
                    translatedTypes.append(", ");
                }
                int typeResId = getResources().getIdentifier(
                        "type_" + type.toLowerCase(),
                        "string",
                        getContext().getPackageName()
                );
                if (typeResId != 0) {
                    translatedTypes.append(getString(typeResId));
                } else {
                    translatedTypes.append(type); // Si no hay traducción, usa el tipo original
                }
            }

            // Establecer los tipos traducidos
            typeDetails.setText(getString(R.string.types) + translatedTypes.toString());

            // Mostrar la imagen del Pokémon
            Glide.with(this)
                    .load(imageUrl) // URL de la imagen del Pokémon
                    .placeholder(R.drawable.ic_launcher_foreground) // Imagen si no se puede mostrar la URL
                    .error(R.drawable.baseline_error_24) // Imagen en caso de que la URL sea null
                    .into(imageDetails);
        }

    }
}


