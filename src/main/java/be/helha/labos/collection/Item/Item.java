package be.helha.labos.collection.Item;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import be.helha.labos.collection.User;

/**
 * Classe mère représentant un objet dans le jeu.
 * Elle contient des informations sur l'objet, telles que son nom, son type, etc.
 */
public class Item
{
        /**
         * Attributs de la classe Item
         * @JsonProperty est utilisé pour indiquer que cet attribut doit être sérialisé/désérialisé avec ce nom
         * @ObjectId est un identifiant unique généré par MongoDB
         * @param name le nom de l'objet
         * @param type le type de l'objet
         * @param user l'utilisateur auquel appartient l'objet
         */
        @JsonProperty("_id")
        protected ObjectId id;
        protected int idUser;
        protected String name;
        protected String type;
        protected int level_Required;

        /**
         * Constructeur vide
         */
        public Item() {
            this.name = getName();
            this.id = new ObjectId();
            //this.idUser = getIdUser();
        }

        /**
         * ObjectIdWrapper est une classe interne utilisée pour encapsuler l'identifiant d'objet
         */
        public static class ObjectIdWrapper {
            @JsonProperty("$oid")
            private String oid;

            /**
             * getOid et setOid sont des méthodes d'accès pour l'identifiant d'objet
             */
            public String getOid() { return oid; }
            public void setOid(String oid) { this.oid = oid; }

            @Override
            public String toString() {
                return oid;
            }
        }

        /**
        * getter pour l'identifiant d'objet
        * @return
         */
        public ObjectId getId() {
            return id;
        }

        /**
         * setter pour l'identifiant d'objet
         * @param id
         */
        public void setId(ObjectId id) {
            this.id = id;
        }

        /**
         * getter pour l'identifiant d'utilisateur
         * @return
         */
        public String getName() {
            return name;
        }

    public int getLevel_Buy() {
        return level_Required;
    }


    /**
         * setter pour l'identifiant d'utilisateur
         * @param idUser
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * methode toString pour afficher les informations de l'objet
         * @return une chaîne de caractères contenant les informations de l'objet (id,idUser, nom, type, niveau requis)
         */
        @Override
        public String toString() {
            return "Item{" +
                    "id=" + id +
                    "idUser=" + idUser +
                    ", name='" + name +
                    ", type='" + type +
                    ", level_Required=" + level_Required +
                    '}';
        }
}


