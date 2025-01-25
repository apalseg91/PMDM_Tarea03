package dam.pmdm.practicanavigationyoutube;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Adaptador para mostrar un equipo de Pokémon en un RecyclerView.
 * Proporciona opciones para visualizar detalles y eliminar elementos si está permitido.
 */
public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private List<Pokemon> pkmnList;
    private OnItemClickListener listener;
    private Context context;
    private boolean isDeletable; // Estado del switch
    private PokemonViewModel viewModel;

    /**
     * Interface para manejar eventos de clic en elementos.
     */
    public interface OnItemClickListener {
        /**
         * Evento disparado al hacer clic en un Pokémon.
         *
         * @param pokemon Objeto Pokémon clicado.
         */
        void onItemClick(Pokemon pokemon);
    }

    public TeamAdapter(List<Pokemon> pkmnList, OnItemClickListener listener, boolean isDeletable, Context context, PokemonViewModel viewModel) {
        this.pkmnList = pkmnList;
        this.listener = listener;
        this.isDeletable = isDeletable;
        this.context = context;
        this.viewModel = viewModel;
    }

    /**
     * Crea un nuevo ViewHolder inflando el layout correspondiente.
     *
     * @param parent   Contenedor padre donde se mostrará el ViewHolder.
     * @param viewType Tipo de vista (no utilizado en este caso).
     * @return Una nueva instancia de TeamViewHolder.
     */
    @NonNull
    @Override
    public TeamAdapter.TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_team,
                        parent, false);
        return new TeamViewHolder(view);
    }

    /**
     * Vincula un Pokémon a un ViewHolder y configura los listeners de eventos.
     *
     * @param holder   El ViewHolder que contiene las vistas.
     * @param position Posición del elemento en la lista.
     */
    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.TeamViewHolder holder, int position) {
        //establecemos los datos
        Pokemon pokemon = pkmnList.get(position);
        holder.nameField.setText(pokemon.getName());
        String types = pokemon.getTypesAsString();
        holder.typesField.setText(types);
        //establecemos imagen desde la url
        String imageUrl = pokemon.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.img.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.baseline_error_24)
                    .into(holder.img);
        } else {
            holder.img.setImageResource(R.drawable.baseline_catching_pokemon_24);
        }
        // Obtener el estado del switch desde SharedPreferences
        SharedPreferences sharedPreferences = holder.itemView.getContext()
                .getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        boolean isDeletable = sharedPreferences.getBoolean("isDeletable", false);
        //traducir los tipos del pokemon
        // Verificar que getTypes() no sea nulo
        if (pokemon.getTypes() != null && !pokemon.getTypes().isEmpty()) {
            StringBuilder translatedTypes = new StringBuilder();
            for (Pokemon.TypeWrapper typeWrapper : pokemon.getTypes()) {
                if (translatedTypes.length() > 0) {
                    translatedTypes.append(", ");
                }
                // Obtener nombre del tipo
                String typeName = typeWrapper.getType().getName();
                // Traducir el tipo
                int typeResId = context.getResources().getIdentifier(
                        "type_" + typeName.toLowerCase(),
                        "string",
                        context.getPackageName()
                );
                // Verificar si se encontró la traducción
                if (typeResId != 0) {
                    translatedTypes.append(context.getString(typeResId));
                } else {
                    translatedTypes.append(typeName); // Si no hay traducción, usa el tipo original
                }
            }
            holder.typesField.setText(translatedTypes.toString());
        } else {
            holder.typesField.setText(R.string.unknown); // Si no hay tipos, muestra "Unknown"
        }
        //manejo del botond de borrado
        holder.deleteButton.setOnClickListener(v -> {
            if (isDeletable) {//comprobamos si está activa la opcion de borrado
                // Eliminar de Firestore
                FirebaseFirestore.getInstance()
                        .collection("team")
                        .document(String.valueOf(pokemon.getId()))
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            // Eliminar de la lista local y notificar cambios
                            pkmnList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, pkmnList.size());
                            //informo del pokemon eliminado
                            Toast.makeText(v.getContext(), pokemon.getName() + context.getString(R.string.eliminado_del_equipo), Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {//si ha habido algun error
                            Toast.makeText(v.getContext(), context.getString(R.string.error_al_eliminar_pok_mon), Toast.LENGTH_SHORT).show();
                        });
            } else {//si no está activa la opción de borrado informo
                Toast.makeText(v.getContext(), R.string.el_bloqueo_est_activo, Toast.LENGTH_SHORT).show();
            }
        });
        // Configurar el clic en el elemento
        holder.itemView.setOnClickListener(v -> listener.onItemClick(pokemon));
    }

    /**
     * Devuelve el número total de elementos en la lista.
     *
     * @return Cantidad de elementos en la lista.
     */
    @Override
    public int getItemCount() {
        return pkmnList.size();
    }

    public void updateTeam(List<Pokemon> newTeam) {
        pkmnList.clear();
        pkmnList.addAll(newTeam);
        notifyDataSetChanged();
    }

    /**
     * Clase interna que representa un ViewHolder para un Pokémon.
     */
    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView nameField, typesField;
        ImageView img;
        ImageButton deleteButton;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            nameField = itemView.findViewById(R.id.nameTeam);
            typesField = itemView.findViewById(R.id.typeTeam);
            img = itemView.findViewById(R.id.imageTeam);
            deleteButton = itemView.findViewById(R.id.deleteButtonTeam);
        }
    }
}
