package dam.pmdm.practicanavigationyoutube;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pokemon {
    private boolean caught;
    private int id;
    private String name, imageUrl, url;
    private List<TypeWrapper> types;
    @SerializedName("weight")
    private int weight;
    @SerializedName("height")
    private int height;
    private Sprites sprites;


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
    public String getTypesAsString() {
        if (types == null || types.isEmpty()) return "Unknown";
        StringBuilder typesString = new StringBuilder();
        for (TypeWrapper typeWrapper : types) {
            typesString.append(typeWrapper.getType().getName()).append(", ");
        }
        return typesString.substring(0, typesString.length() - 2); // Eliminar la última coma
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
