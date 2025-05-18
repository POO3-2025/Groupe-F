package be.helha.labos.collection.Item;

import be.helha.labos.collection.Character.CharacterType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;

/**
 * Classe représentant un objet dans le jeu.
 * La classe est utilisée pour stocker les informations de l'objet dans la base de données.
 */
public class Item {
    /**
     * Attributs de la classe Item
     */
    @JsonProperty("_id")
        protected ObjectId id;
        protected String name;
        protected String type;
        protected String allowedCharacterType;

        /**
         * Constructeur par défaut
         */
        public Item() {
            this.name = getName();
            this.id = new ObjectId();
        }

        /**
         * ObjectIdWrapper est une classe interne utilisée pour encapsuler l'ObjectId
         */
        public static class ObjectIdWrapper {
            @JsonProperty("$oid")
            private String oid;

            public String getOid() { return oid; }
            public void setOid(String oid) { this.oid = oid; }

            @Override
            /**
             * Méthode toString pour afficher l'ObjectId
             * @return une chaîne de caractères contenant l'ObjectId
             */
            public String toString() {
                return oid;
            }
        }

        /**
         * Méthode pour obtenir l'ObjectId
         * @return l'ObjectId de l'objet
         */
        public ObjectId getId() {
            return id;
        }
        /**
         * Méthode pour définir l'ObjectId
         * @param id l'ObjectId de l'objet
         */
        public void setId(ObjectId id) {
            this.id = id;
        }
        /**
         * Méthode pour obtenir le nom de l'objet
         * @return le nom de l'objet
         */
        public String getName() {
            return name;
        }
        /**
         * Méthode pour obtenir la classe qui sera autorisée à utiliser l'objet
         */
        public String getAllowedcharacterType() {
            return allowedCharacterType;
        }
        /**
         * Méthode pour définir la classe qui sera autorisée à utiliser l'objet
         * @param name la classe qui sera autorisée à utiliser l'objet
         */
        public void setName(String name) {
            this.name = name;
        }
        /**
         * Méthode pour vérifier si l'objet peut être équipé par un personnage
         * @return le type de l'objet
         */
    public boolean canEquip(CharacterType personnage) {
        System.out.println("Allowed type for item: " + this.allowedCharacterType);
        System.out.println("Character title: " + personnage.getTitle());
        return this.allowedCharacterType != null && this.allowedCharacterType.equalsIgnoreCase(personnage.getTitle());
    }

        /**
         * Méthode pour obtenir le toString de l'objet
         */
        @Override
        public String toString() {
            return "Item2{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
}


