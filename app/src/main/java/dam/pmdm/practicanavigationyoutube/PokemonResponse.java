package dam.pmdm.practicanavigationyoutube;

import java.util.List;

/**
 * Clase que representa la respuesta de la API de Pokémon.
 * Contiene información de paginación y una lista de resultados de Pokémon.
 */
public class PokemonResponse {
    private int count; // Número total de Pokémon disponibles.
    private String next; // URL para la siguiente página de resultados.
    private String previous; // URL para la página anterior de resultados.
    private List<Pokemon> results; // Lista de Pokémon obtenidos.

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Pokemon> getResults() {
        return results;
    }

    public void setResults(List<Pokemon> results) {
        this.results = results;
    }
}
