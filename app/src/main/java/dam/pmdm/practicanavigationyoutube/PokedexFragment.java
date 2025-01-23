package dam.pmdm.practicanavigationyoutube;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PokedexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PokedexFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private PokedexAdapter adapter;
    private List<Pokemon> list;
    private List<Pokemon> pokedexList;
    private int offset= 0;
    private int limit = 151;

    public PokedexFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PokedexFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PokedexFragment newInstance(String param1, String param2) {
        PokedexFragment fragment = new PokedexFragment();
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
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);
        recyclerView = view.findViewById(R.id.recyclerviewPokedex);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = new ArrayList<>();
        adapter = new PokedexAdapter(requireContext(),list);
        recyclerView.setAdapter(adapter);

        // Llama al método para obtener los Pokémon
        getPokemones();

        return view;
    }
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private void getPokemones() {
        ApiService service = Client.getClient().create(ApiService.class);
        Call<PokemonResponse> call = service.getPokemonList(0, 151);
        call.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pokemon> results = response.body().getResults();
                    for (Pokemon pokemon : results) {
                        if (pokemon.getUrl() != null) {
                            pokemon.setId(pokemon.getIdFromUrl()); // Extrae y asigna el ID desde la URL
                        }
                    }
                    list.addAll(results); // Agrega los Pokémon con IDs asignados a la lista
                    adapter.notifyDataSetChanged(); // Notifica al adaptador para actualizar el RecyclerView
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged(); // Refresca el RecyclerView
    }

}