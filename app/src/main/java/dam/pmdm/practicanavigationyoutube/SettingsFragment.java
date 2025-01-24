package dam.pmdm.practicanavigationyoutube;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private Switch deleteSwitch;
    private Button languageButton, aboutButton, logoutButton;

    // SharedPreferences para almacenar las configuraciones
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializa SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Obtiene el idioma guardado y lo aplica
        String language = sharedPreferences.getString("language", "en"); // "en" es el idioma predeterminado
        applyLanguage(language);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Referencias a los elementos del layout
        deleteSwitch = view.findViewById(R.id.deleteSwitch);
        languageButton = view.findViewById(R.id.languageButton);
        aboutButton = view.findViewById(R.id.aboutButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Configurar el estado inicial del switch desde SharedPreferences
        boolean isDeletable = sharedPreferences.getBoolean("isDeletable", false);
        deleteSwitch.setChecked(isDeletable);

        // Listener para el Switch
        deleteSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("isDeletable", isChecked);
            editor.apply();
            Toast.makeText(getContext(), "Delete Pokémon: " + (isChecked ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
        });

        // Listener para el cambio de idioma
        languageButton.setOnClickListener(v -> changeLanguage());

        // Listener para "Acerca de"
        aboutButton.setOnClickListener(v -> showAboutDialog());

        // Listener para cerrar sesión
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void changeLanguage() {
        // Obtiene el idioma actual desde las preferencias
        String currentLanguage = sharedPreferences.getString("language", "en"); // "en" es el predeterminado si no hay valor

        // Alterna entre inglés y español
        String newLanguage = currentLanguage.equals("en") ? "es" : "en";

        // Cambia el idioma
        applyLanguage(newLanguage);

        // Guarda el idioma seleccionado en SharedPreferences
        editor.putString("language", newLanguage);
        editor.apply();

        // Mensaje al usuario
        Toast.makeText(getContext(), "Language changed to " + (newLanguage.equals("en") ? "English" : "Español"), Toast.LENGTH_SHORT).show();

        // Reinicia la actividad para aplicar los cambios
        requireActivity().recreate();
    }
    public void applyLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("About...");
        builder.setMessage("Developed by: Alejandro Palomeque Segura.\nIES Aguadulce-Distancia\nTarea 3.-PMDM 2024-2025\nVersión: 1.0.0");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    public void logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut(); // Cierra la sesión de Firebase

        // Asegúrate de redirigir a LoginActivity y limpiar la pila de actividades
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpia la pila de actividades
        startActivity(intent);

        // Finaliza la actividad actual
        requireActivity().finish();
    }
}
