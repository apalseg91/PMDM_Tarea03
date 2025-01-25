package dam.pmdm.practicanavigationyoutube;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

/**
 * Clase `MainActivity` que actúa como punto de entrada de la aplicación y maneja la navegación
 * entre fragmentos utilizando un `BottomNavigationView`.
 */
public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private Fragment navHostFragment;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtén el idioma guardado
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "en"); // "en" por defecto
        // Aplica el idioma
        applyLanguage(language);
        //establecemos la pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //establecemos la navegavión e inicializamos la barra de navegacion por botones
        navHostFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        bottomNavigationView = findViewById(R.id.BottomNavigationView);
        navController = NavHostFragment.findNavController(navHostFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        //manejo de los botones de la barra de navegación
        bottomNavigationView.setOnItemSelectedListener(this::onMenuSelected);
    }

    /**
     * método que establece la navegación entre fragmentos según el botón elegido
     *
     * @param menuItem item que hace referencia a uno de los apartados del menu
     * @return `true` si la navegación se realizó correctamente.
     */
    private boolean onMenuSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.buttonPokedex) {
            navController.navigate(R.id.fragmentPokedex);
            Toast.makeText(this, getString(R.string.item_clicked) + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (menuItem.getItemId() == R.id.buttonTeam) {
            navController.navigate(R.id.fragmentTeam);
            Toast.makeText(this, getString(R.string.item_clicked)+ menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (menuItem.getItemId() == R.id.buttonSettings) {
            navController.navigate(R.id.fragmentSettings);
            Toast.makeText(this, getString(R.string.item_clicked) + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }

    /**
     * Actualiza el estado de captura de un Pokémon.
     *
     * @param pokemonId El ID del Pokémon.
     * @param isCaught  Indica si el Pokémon ha sido capturado.
     */
    public void updatePokemonState(int pokemonId, boolean isCaught) {
        PokedexFragment pokedexFragment = (PokedexFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);

        if (pokedexFragment != null) {
            RecyclerView recyclerView = pokedexFragment.getRecyclerView();
            PokedexAdapter adapter = (PokedexAdapter) recyclerView.getAdapter();

            if (adapter != null) {
                for (Pokemon pokemon : adapter.getPkmnList()) {
                    if (pokemon.getId() == pokemonId) {
                        pokemon.setCaught(isCaught); // Actualizar estado
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

        }
    }

    /**
     * Cambia el idioma de la aplicación.
     *
     * @param languageCode Código del idioma deseado (ejemplo: "en", "es").
     */
    private void applyLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}