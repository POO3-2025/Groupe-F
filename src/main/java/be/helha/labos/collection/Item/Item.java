package be.helha.labos.collection.Item;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
//import be.helha.labos.collection.User;

public class Item {
        @JsonProperty("_id")
        protected ObjectId id;
        protected int idUser;
        protected String name;
        protected String type;

        public Item() {
            this.name = getName();
            this.id = new ObjectId();
            //this.idUser = getIdUser();
        }

        public static class ObjectIdWrapper {
            @JsonProperty("$oid")
            private String oid;

            public String getOid() { return oid; }
            public void setOid(String oid) { this.oid = oid; }

            @Override
            public String toString() {
                return oid;
            }
        }

        public ObjectId getId() {
            return id;
        }

        public void setId(ObjectId id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        @Override
        public String toString() {
            return "Item2{" +
                    "id=" + id +
                    "idUser=" + idUser +
                    ", name='" + name +
                    ", type='" + type +
                    '}';
        }
}


