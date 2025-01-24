package dam.pmdm.practicanavigationyoutube;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private Fragment navHostFragment;
    private BottomNavigationView bottomNavigationView;
    private int offset =0;
    private int limit =151;
    PokedexFragment pokedexFragment;
    private RecyclerView recyclerView;
    private PokedexAdapter adapter;
    private List<Pokemon> pokedexList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Obtén el idioma guardado
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "en"); // "en" por defecto

        // Aplica el idioma
        applyLanguage(language);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "onCreate started", Toast.LENGTH_SHORT).show();

        navHostFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        bottomNavigationView = findViewById(R.id.BottomNavigationView);

        if (navHostFragment == null) {
            Toast.makeText(this, "navHostFragment is null", Toast.LENGTH_SHORT).show();
        } else {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            Toast.makeText(this, "Navigation setup complete", Toast.LENGTH_SHORT).show();
        }

        if (bottomNavigationView == null) {
            Toast.makeText(this, "BottomNavigationView is null", Toast.LENGTH_SHORT).show();
        } else {
            bottomNavigationView.setOnItemSelectedListener(this::onMenuSelected);

        }
    }


    private boolean onMenuSelected(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.buttonPokedex){
            navController.navigate(R.id.fragmentPokedex);
            Toast.makeText(this, "Item clicked: " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        } else if(menuItem.getItemId() == R.id.buttonTeam){
            navController.navigate(R.id.fragmentTeam);
            Toast.makeText(this, "Item clicked: " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (menuItem.getItemId() == R.id.buttonSettings){
            navController.navigate(R.id.fragmentSettings);
            Toast.makeText(this, "Item clicked: " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return true;
    }

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
    private void applyLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

}