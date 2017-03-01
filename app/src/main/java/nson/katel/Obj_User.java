package nson.katel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Obj_User {
    public String familyName;
    public String displayName;
    public String giveName;
    public String email;
    public String id;
    public String photoUrl;
    public String password;



    public Obj_User() {
    }

    public Obj_User(String familyName, String displayName, String giveName, String email, String id, String photoUrl, String password) {
        this.familyName = familyName;
        this.displayName = displayName;
        this.giveName = giveName;
        this.email = email;
        this.id = id;
        this.photoUrl = photoUrl;
        this.password = password;
    }



}
