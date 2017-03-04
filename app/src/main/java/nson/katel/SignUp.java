package nson.katel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class SignUp extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private static final int CODE_CAM = 2;
    private static final int CODE_LIBRARY = 3;

    private ImageView imgv_avatar;
    private ActionBar actionBar;
    private EditText edt_email,edt_ten,edt_ho,edt_matkhau,edt_xacnhapmatkhau;
    private Button btn_signup,btn_cam,btn_lib;
    private TextView txt_notifice;
    ProgressDialog myProgress;
    AlertDialog mDialog;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageRef;
    private DatabaseReference mData;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Đăng kí");

        mAnhXa();
        mClick();
        //  Show_Select_Category_User();

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mData = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                }
            }
        };

        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Đang Xử lý ...");
        myProgress.setMessage("Vui lòng chờ...");
        myProgress.setCancelable(false);

        SignInWithGoogle();





    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void mAnhXa(){
        edt_email=(EditText)findViewById(R.id.edt_signup_email);
        edt_ten =(EditText)findViewById(R.id.edt_signup_ten);
        edt_ho=(EditText)findViewById(R.id.edt_signup_ho);
        edt_matkhau = (EditText) findViewById(R.id.edt_signup_matkhau);
        edt_xacnhapmatkhau=(EditText)findViewById(R.id.edt_signup_xacnhanmatkhau);
        imgv_avatar =(ImageView)findViewById(R.id.imgv_signup_avatar);
        btn_signup =(Button) findViewById(R.id.btn_signup_dangki);
        btn_cam =(Button) findViewById(R.id.btn_signup_camera);
        btn_lib =(Button) findViewById(R.id.btn_signup_library);
        txt_notifice =(TextView)findViewById(R.id.txt_notifice);
    }
    public void  mClick(){
        btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra Camera trong thiết bị
                if (getApplicationContext().getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {
                    // Mở camera mặc định
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Tiến hành gọi Capture Image intent
                    startActivityForResult(intent, CODE_CAM);
                } else {
                    Toast.makeText(getApplication(), "Camera không được hỗ trợ", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_lib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CODE_LIBRARY);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myProgress.show();
                if (KiemTraDuLieu()==true) {
                    SignUpNewUser(edt_email.getText().toString(), edt_matkhau.getText().toString());

                }
                else {
                    Toast.makeText(getApplicationContext(),"Kiểm tra dữ liệu",Toast.LENGTH_SHORT).show();
                }
            }
        });
        txt_notifice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Tài khoản Katel buộc phải kế thừa từ Google",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void SignInWithGoogle(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient  = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        Intent signInIntent = new Intent( Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient));
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void  SignUpNewUser ( String e, String p ){
        final String a = e,b = p;
        mAuth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            SigninUserExist(a,b );
                        }else {
                            Toast.makeText(getApplicationContext(),"Tài khoản này đã tồn tại",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_CAM && resultCode ==RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgv_avatar.setImageBitmap(bitmap);
        }


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct= result.getSignInAccount();
                edt_email.setText(acct.getEmail().toString());
                edt_ten.setText(acct.getGivenName().toString());
                edt_ho.setText(acct.getFamilyName().toString());
                Picasso.with(getApplicationContext()).load(acct.getPhotoUrl()).into(imgv_avatar);
                Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient);

            } else {
            }
        }


        if (requestCode == CODE_LIBRARY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }catch (IOException e) {
                e.printStackTrace();
            }
            imgv_avatar.setImageBitmap(bitmap);
        }
    }

    public  void SigninUserExist(String e,String p ){
        mAuth.signInWithEmailAndPassword(e, p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            AddInfomationforUser();
                        }

                    }
                });
    }


    public  void AddInfomationforUser(){
        final Calendar[] calendar = {Calendar.getInstance()};
        StorageReference avatarUser = storageRef.child("AvatarUser");
        StorageReference item = avatarUser.child("image"+ calendar[0].getTimeInMillis()+".png");

        imgv_avatar.setDrawingCacheEnabled(true);
        imgv_avatar.buildDrawingCache();
        Bitmap bitmap = imgv_avatar.getDrawingCache();
        ByteArrayOutputStream m = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, m);
        byte[] data = m.toByteArray();

        final UploadTask uploadTask = item.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(),"Lỗi",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                user = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(edt_ten.getText().toString()+" "+edt_ho.getText().toString())
                        .setPhotoUri(downloadUrl)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                }
                            }
                        });


                myProgress.dismiss();
                Show_Select_Category_User();

            }
        });
    }


    public boolean  KiemTraDuLieu(){
        if (edt_email.getText().toString().equals("")==true) return false;
        else if (edt_ho.getText().toString().equals("")==true) return false;
        else if (edt_ten.getText().toString().equals("")==true) return false;
        else if (edt_matkhau.getText().toString().equals("")==true) return false;
        else if (edt_matkhau.getText().toString().equals(edt_xacnhapmatkhau.getText().toString())==false) {
            return false;
        }
        else return true;
    }

public  void  Show_Select_Category_User() {
    mDialog = new AlertDialog.Builder(this,R.style.MyDialogTheme)
            .setMessage("Bạn muốn đăng kí kinh doanh với Katel")
            .setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(),SignUpBusinessStep1.class));
                        }
                    })

            .setNegativeButton("NO",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                    }).create();
    mDialog.setInverseBackgroundForced(true);
    mDialog.setCancelable(false);
    mDialog.show();
}
}
