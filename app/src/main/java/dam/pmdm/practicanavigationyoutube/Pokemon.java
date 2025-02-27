package dam.pmdm.practicanavigationyoutube;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Clase que representa un Pokémon con sus atributos principales.
 * Incluye información como altura, peso, tipos y sprites.
 */
public class Pokemon {
    private boolean caught; // Indica si el Pokémon ha sido capturado.
    private int id; // Identificador único del Pokémon.
    private String name, imageUrl, url; // Nombre, URL de la imagen y URL del Pokémon.
    private List<TypeWrapper> types; // Lista de tipos del Pokémon.
    @SerializedName("weight")
    private int weight; // Peso del Pokémon en hectogramos.
    @SerializedName("height")
    private int height; // Altura del Pokémon en decímetros.
    private Sprites sprites; // Sprites del Pokémon.

    public Pokemon() {
        // Constructor vacío requerido por Firestore
    }

    public Pokemon(int height, String imageUrl, int id, String name, List<TypeWrapper> types, int weight,Sprites sprite) {
        this.height = height;
        this.imageUrl = imageUrl;
        this.id = id;
        this.name = name;
        this.types = types;
        this.weight = weight;
        this.sprites = sprite;
        caught = false;
    }

    public Pokemon(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        caught = false;
    }

    public Pokemon(String name, String imageUrl, int id) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.id = id;
        caught = false;
    }

    // Getters y setters
    public List<TypeWrapper> getTypes() {
        return types;
    }

    public void setTypes(List<TypeWrapper> types) {
        this.types = types;
    }

    public int getHeight() {
        return height; // Verifica que no haya valores por defecto aquí.
    }

    public int getWeight() {
        return weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    public boolean isCaught() {
        return caught;
    }
    public void setCaught(boolean caught) {
        this.caught = caught;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIdFromUrl() {
        // La URL tiene el formato https://pokeapi.co/api/v2/pokemon/{id}/
        String[] parts = url.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
    // Clase interna TypeWrapper.
    public static class TypeWrapper {
        private Type type;

        public static class Type {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

    }
    // Método auxiliar para convertir la lista de tipos a String
    /*public String getTypesAsString() {
        if (types == null || types.isEmpty()) return "Unknown";
        StringBuilder typesString = new StringBuilder();
        for (TypeWrapper typeWrapper : types) {
            typesString.append(typeWrapper.getType().getName()).append(", ");
        }
        return typesString.substring(0, typesString.length() - 2); // Eliminar la última coma
    */
    public String getTypesAsString() {
        if (types == null || types.isEmpty()) return "Unknown";
        StringBuilder typesString = new StringBuilder();
        for (TypeWrapper typeWrapper : types) {
            typesString.append(typeWrapper.getType().getName()).append(", ");
        }
        // Asegurarse de que la cadena no termine con una coma y un espacio.
        if (typesString.length() > 0) {
            typesString.setLength(typesString.length() - 2); // Eliminar la última coma y espacio.
        }
        return typesString.toString();

    }

    // Clase interna para Sprites
    public static class Sprites {
        private String front_default;

        public String getFrontDefault() {
            return front_default;
        }

        public void setFrontDefault(String front_default) {
            this.front_default = front_default;
        }
    }
    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

}
