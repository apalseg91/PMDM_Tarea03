package dam.pmdm.practicanavigationyoutube;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase `Client` utilizada para configurar y proporcionar una instancia de Retrofit para realizar
 * llamadas a la API.
 */
public class Client {
    /**
     * Declaracion ya signacion de variables
     */
    public static final String BASE_URL = "https://pokeapi.co/api/v2/";
    public static Retrofit retrofit = null;

    /**
     * Proporciona una instancia de Retrofit. Si no existe, la crea con la configuración adecuada.
     *
     * @return Una instancia de Retrofit configurada con la URL base y un convertidor Gson.
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
