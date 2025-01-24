package dam.pmdm.practicanavigationyoutube;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
 * Fragmento que muestra la lista de Pokémon en la Pokédex.
 */
public class PokedexFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private PokedexAdapter adapter;
    private List<Pokemon> list;

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

    /**
     * método que inicializa la vista del fragmento y devuelve la vista raiz del fragmento
     */
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       //Inlfa el layout del fragmento
       View view = inflater.inflate(R.layout.fragment_pokedex, container, false);
       recyclerView = view.findViewById(R.id.recyclerviewPokedex);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       //Inicializa el ViewModel compartido
       PokemonViewModel viewModel = new ViewModelProvider(requireActivity()).get(PokemonViewModel.class);
       //Inicializar la lista y el adaptador
       list = new ArrayList<>();
       adapter = new PokedexAdapter(requireContext(), list,viewModel); // Inicializa el adaptador
       recyclerView.setAdapter(adapter);
       // Llama al método para obtener los Pokémon
       getPokemones();
       return view;
   }

    /**
     * método que obtiene una instancia de un recyclerview y la devuelve
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * método que Realiza una llamada con Retrofit. Actualiza la lista y el adaptador con los resultados.
     */
    private void getPokemones() {
        ApiService service = Client.getClient().create(ApiService.class);
        Call<PokemonResponse> call = service.getPokemonList(0, 151);//establecemos el numero de pokemeons sobre el que iterar en la API
        call.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pokemon> results = response.body().getResults();//cargamos resultados en la lista
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
            public void onFailure(Call<PokemonResponse> call, Throwable t) {//en caso de probelmas
                t.printStackTrace();
            }
        });
    }

    /**
     * Actualiza la vista al reanudar el fragmento. Notifica al adaptador para refrescar el RecyclerView.
     */
    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged(); // Refresca el RecyclerView
    }
}