package dam.pmdm.practicanavigationyoutube;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * CLase para sincronizar el estado entre PokedexFragment y TeamFragment.
 */
public class PokemonViewModel extends ViewModel {
    private final MutableLiveData<List<Pokemon>> team = new MutableLiveData<>(new ArrayList<>());

    // Método para exponer un LiveData de solo lectura
    public LiveData<List<Pokemon>> getTeam() {
        return team;
    }

    // Método para modificar la lista del equipo
    public void addPokemonToTeam(Pokemon pokemon) {
        List<Pokemon> currentTeam = team.getValue();
        if (currentTeam == null) {
            currentTeam = new ArrayList<>();
        }

        if (!currentTeam.contains(pokemon)) {
            currentTeam.add(pokemon);
            team.setValue(currentTeam); // Aquí sí puedes usar setValue
        }
    }
}
