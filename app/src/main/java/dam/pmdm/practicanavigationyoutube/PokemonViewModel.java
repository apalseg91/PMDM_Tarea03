package dam.pmdm.practicanavigationyoutube;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para sincronizar el estado entre PokedexFragment y TeamFragment.
 * Proporciona un equipo de Pokémon compartido mediante LiveData.
 */
public class PokemonViewModel extends ViewModel {
    private final MutableLiveData<List<Pokemon>> team = new MutableLiveData<>(new ArrayList<>());

    /**
     * Obtiene el LiveData del equipo de Pokémon.
     * Este LiveData es de solo lectura para los observadores.
     *
     * @return LiveData que contiene la lista del equipo de Pokémon.
     */
    public LiveData<List<Pokemon>> getTeam() {
        return team;
    }
}
