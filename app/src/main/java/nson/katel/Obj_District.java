package nson.katel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Nson on 2/24/2017.
 */

public class Obj_District {
    public   Integer id_City;
    public  Integer id_District;
    public  String name_District;

    static public String name_class="District";
    static public String KEY_ID_DISTRICT ="id_District";
    static public String KEY_NAME_DISTRICT="name_District";

    public Obj_District() {
    }

    public Obj_District(Integer id_City, Integer id_District, String name_District) {
        this.id_City = id_City;
        this.id_District = id_District;
        this.name_District = name_District;
    }

}
