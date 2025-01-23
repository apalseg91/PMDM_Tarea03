package dam.pmdm.practicanavigationyoutube;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private TeamAdapter adapter;
    private List<Pokemon> teamList;
    private FirebaseFirestore firestore;

    public TeamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamFragment newInstance(String param1, String param2) {
        TeamFragment fragment = new TeamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        // Configuración del RecyclerView
        recyclerView = view.findViewById(R.id.recyclerviewTeam);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar Firestore y la lista
        firestore = FirebaseFirestore.getInstance();
        teamList = new ArrayList<>();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        boolean isDeletable = sharedPreferences.getBoolean("isDeletable", false);

        adapter = new TeamAdapter(teamList,pokemon -> openDetailsFragment(pokemon),isDeletable,requireContext());
        recyclerView.setAdapter(adapter);

        // Cargar los Pokémon desde Firestore
        loadTeamFromFirestore();
        return view;
    }

    private void loadTeamFromFirestore() {
        firestore.collection("team")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        teamList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Pokemon pokemon = document.toObject(Pokemon.class);

                            // Completar datos desde la API
                            if (pokemon != null) {
                                Client.getClient().create(ApiService.class)
                                        .getPokemonDetails(pokemon.getId())
                                        .enqueue(new Callback<Pokemon>() {
                                            @Override
                                            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    Pokemon details = response.body();
                                                    pokemon.setTypes(details.getTypes());
                                                    pokemon.setWeight(details.getWeight());
                                                    pokemon.setHeight(details.getHeight());
                                                    if (details.getSprites() != null && details.getSprites().getFrontDefault() != null) {
                                                        pokemon.setImageUrl(details.getSprites().getFrontDefault());
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                    //TODO: BORRAR DEPURACION
                                                    Log.d("API Raw Response", new Gson().toJson(response.body()));
                                                } else{
                                                    Log.e("API Error", "Response not successful");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Pokemon> call, Throwable t) {
                                                Toast.makeText(getContext(), "Error cargando detalles", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            teamList.add(pokemon);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Failure loading team", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openDetailsFragment(Pokemon pokemon) {
        Bundle bundle = new Bundle();
        int height = pokemon.getHeight();
        int weight = pokemon.getWeight();
//TODO:- borrar trazas
        Log.d("TeamFragment", "Height (to pass): " + height);
        Log.d("TeamFragment", "Weight (to pass): " + weight);

        bundle.putInt("pokemon_id", pokemon.getId());
        bundle.putString("pokemon_name", pokemon.getName());
        bundle.putString("pokemon_image", pokemon.getImageUrl());
        bundle.putString("pokemon_types", pokemon.getTypesAsString());
        bundle.putInt("pokemon_height", height);
        bundle.putInt("pokemon_weight", weight);


        NavHostFragment.findNavController(this)
                .navigate(R.id.action_fragmentTeam_to_detailsFragment, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTeamFromFirestore(); // Recargar al volver al fragmento
    }
}