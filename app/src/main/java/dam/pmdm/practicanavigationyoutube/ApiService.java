package dam.pmdm.practicanavigationyoutube;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("pokemon/")
    Call<PokemonResponse> getPokemonList(@Query("offset") int offset, @Query("limit") int limit);
    @GET("pokemon/{id}")
    Call<Pokemon> getPokemonDetails(@retrofit2.http.Path("id") int id);

}
