package nson.katel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private TextView txt_login_signup, txt_login_forgetpass;
    private ActionBar actionBar;
    private EditText edt_login_id, edt_login_pass;
    private Button btn_login;
    private  ProgressDialog myProgress;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Đăng nhập");
        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Đang Xử lý ...");
        myProgress.setMessage("Vui lòng chờ...");
        myProgress.setCancelable(false);



        txt_login_forgetpass = (TextView) findViewById(R.id.txt_login_forgetpass);
        txt_login_signup = (TextView) findViewById(R.id.txt_login_signup);


        txt_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
        txt_login_forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
            }
        });



        edt_login_id = (EditText) findViewById(R.id.edt_login_id);
        edt_login_pass = (EditText) findViewById(R.id.edt_login_pass);



        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                } else {
                }
            }
        };



        btn_login =(Button) findViewById(R.id.btn_login_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProgress.show();
                if (KiemTraDuLieu()==true) {
                    signin();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Vui lòng kiểm tra dữ liệu ",Toast.LENGTH_SHORT).show();
                    myProgress.dismiss();
                }

            }
        });



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

    public void signin(){
        mAuth.signInWithEmailAndPassword(edt_login_id.getText().toString(),edt_login_pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Đăng nhập thành công ",Toast.LENGTH_SHORT).show();
                            myProgress.dismiss();
                            startActivity(new Intent(getApplicationContext(),Main.class));
                        }else {
                            Toast.makeText(getApplicationContext(),"Password sai!",Toast.LENGTH_SHORT).show();
                            myProgress.dismiss();
                        }

                    }
                });
    }

    public  Boolean  KiemTraDuLieu(){
        if (edt_login_id.getText().toString().equals("")==true) return false ;
        else if (edt_login_id.getText().equals("")==true) return  false;
        else  return   true;
    }
}

