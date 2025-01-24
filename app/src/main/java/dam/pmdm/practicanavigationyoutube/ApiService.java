package dam.pmdm.practicanavigationyoutube;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interfaz que define los métodos para interactuar con la API de Pokémon.
 */
public interface ApiService {
    /**
     * Obtiene una lista de Pokémon desde la API.
     *
     * @param offset Índice desde donde comenzar la lista de Pokémon.
     * @param limit  Número máximo de Pokémon a devolver.
     * @return Un objeto `Call` que devuelve la respuesta en formato `PokemonResponse`.
     */
    @GET("pokemon/")
    Call<PokemonResponse> getPokemonList(@Query("offset") int offset, @Query("limit") int limit);

    /**
     * Obtiene los detalles de un Pokémon específico a partir de su ID.
     *
     * @param id ID del Pokémon cuyos detalles se desean obtener.
     * @return Un objeto `Call` que devuelve la respuesta en formato `Pokemon`.
     */
    @GET("pokemon/{id}")
    Call<Pokemon> getPokemonDetails(@retrofit2.http.Path("id") int id);
}
