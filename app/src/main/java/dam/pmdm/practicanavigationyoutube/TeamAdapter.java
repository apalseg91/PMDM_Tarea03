package dam.pmdm.practicanavigationyoutube;

import static java.security.AccessController.getContext;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private  List<Pokemon> pkmnList;
    private OnItemClickListener listener;
    private Context context;
    private boolean isDeletable; // Estado del switch

    public interface OnItemClickListener {
        void onItemClick(Pokemon pokemon);
    }

    public TeamAdapter(List<Pokemon> pkmnList, OnItemClickListener listener,boolean isDeletable,Context context) {
        this.pkmnList = pkmnList;
        this.listener = listener;
        this.isDeletable = isDeletable;
        this.context = context;
    }

    @NonNull
    @Override
    public TeamAdapter.TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_team,
                parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.TeamViewHolder holder, int position) {
        Pokemon pokemon = pkmnList.get(position);
        holder.nameField.setText(pokemon.getName());
        String types = pokemon.getTypesAsString();
        holder.typesField.setText(types);
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

        holder.deleteButton.setOnClickListener(v -> {
            if (isDeletable) {
                // Si está permitido, elimina el Pokémon y muestra un mensaje
                removePokemonFromTeam(position, v);
                Toast.makeText(v.getContext(), pokemon.getName() + " eliminado del equipo",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Si no está permitido, muestra un mensaje informativo
                Toast.makeText(v.getContext(), "No puedes eliminar Pokémon mientras el bloqueo esté activo",
                        Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(v -> listener.onItemClick(pokemon));
    }

    @Override
    public int getItemCount() {
        return pkmnList.size();
    }

    private void removePokemonFromTeam(int position, View view) {
        Pokemon removedPokemon = pkmnList.get(position);

        // Eliminar de Firestore
        FirebaseFirestore.getInstance()
                .collection("team")
                .document(String.valueOf(removedPokemon.getId()))
                .delete()
                .addOnSuccessListener(aVoid -> {
                    /*if ( context instanceof MainActivity) {
                        ((MainActivity) context).updatePokemonState(removedPokemon.getId(), false);
                    }*/
                    // Eliminar de la lista local y notificar al adaptador
                    pkmnList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, pkmnList.size());
                    Toast.makeText(view.getContext(), "Eliminado con éxito", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(view.getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                });
    }


    // Método para recargar los datos
    private void reloadTeamFromFirestore() {
        FirebaseFirestore.getInstance()
                .collection("team")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        pkmnList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Pokemon pokemon = document.toObject(Pokemon.class);
                            pkmnList.add(pokemon);
                        }
                        notifyDataSetChanged(); // Refresca toda la lista
                    } else {
                        // Maneja error en la recarga
                    }
                });
    }

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
