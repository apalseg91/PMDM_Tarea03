package dam.pmdm.practicanavigationyoutube;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Adaptador para un `RecyclerView` que muestra una lista de Pokémon en la Pokédex.
 */
public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {
    private List<Pokemon> pkmnList;
    private Context context;
    private final FirebaseFirestore firestore;
    private PokemonViewModel viewModel;


    public PokedexAdapter(Context context, List<Pokemon> pkmnList, PokemonViewModel viewModel) {
        this.context = context;
        this.pkmnList = pkmnList;
        this.firestore = FirebaseFirestore.getInstance();
        this.viewModel = viewModel;

    }

    /**
     * método que devuelve la lista de Pokémon gestionada por el adaptador.
     */
    public List<Pokemon> getPkmnList() {
        return pkmnList;
    }

    /**
     * método que infla la vista con el layoout que contiene los elementos cardview
     * @return una instancia del ViewHolder
     */
    @NonNull
    @Override
    public PokedexAdapter.PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_pokedex, parent, false);
        return new PokedexViewHolder(view);
    }

    /**
     *método que enlaza los datos de nuestro modelo con la vista:
     * Establece el nombre, ID y estado de captura del Pokémon.
     * Configura el botón para capturar el Pokémon, deshabilitándolo si ya está capturado.
     * Guarda el estado del Pokémon en Firestore al capturarlo
     */
    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        Pokemon pokemon = pkmnList.get(position);
        // Setea los datos en el cardview
        holder.nameField.setText("Nombre:\n"+ pokemon.getName()+"\t\t\t\t\t\t" );
        holder.idField.setText("ID:\n"+String.valueOf(pokemon.getId()));
        // Actualiza el color y el estado según `isCaught`
        int color = pokemon.isCaught() ? Color.parseColor("#A5D6A7") : Color.WHITE; // Verde si capturado
        holder.cardView.setCardBackgroundColor(color);
        holder.cardView.setClickable(!pokemon.isCaught());
        holder.goItButtom.setEnabled(!pokemon.isCaught());
        // Controlar la habilitación basándonos en el atributo "caught"
        holder.itemView.setAlpha(pokemon.isCaught() ? 0.5f : 1.0f); // Transparencia si está capturado
        holder.itemView.setClickable(!pokemon.isCaught());
        holder.goItButtom.setEnabled(!pokemon.isCaught());
        // Listener para capturar el Pokémon
        holder.goItButtom.setOnClickListener(v -> {
            pokemon.setCaught(true); // Marcar como capturado
            notifyItemChanged(position);
            // Guardar en Firestore
            firestore.collection("team")
                    .document(String.valueOf(pokemon.getId()))
                    .set(pokemon)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, pokemon.getName() + context.getString(R.string.added_to_team), Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, context.getString(R.string.failed_to_add) + pokemon.getName(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    /**
     * método que Devuelve el número de elementos en la lista.
     */
    @Override
    public int getItemCount() {
        return pkmnList.size();
    }

    public static class PokedexViewHolder extends RecyclerView.ViewHolder {
        TextView idField, nameField;
        ImageButton goItButtom;
        CardView cardView;

        public PokedexViewHolder(@NonNull View itemView) {
            super(itemView);
            idField = itemView.findViewById(R.id.numberPokedex);
            nameField = itemView.findViewById(R.id.namePokedex);
            goItButtom = itemView.findViewById(R.id.catchButton);
            cardView = itemView.findViewById(R.id.cardviewPokedex);
        }
    }
}
