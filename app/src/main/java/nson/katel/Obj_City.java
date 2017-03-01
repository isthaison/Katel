package nson.katel;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nson on 2/24/2017.
 */

public class Obj_City {
    public Integer id_City;
    public String name_City;
    static public String name_class = "City";
    static public String KEY_ID_CITY="id_city";
    static public String KEY_NAME_CITY="name_City";



    public Obj_City() {
    }

    public Obj_City(Integer id_City, String name_City) {
        this.id_City = id_City;
        this.name_City = name_City;
    }

}




