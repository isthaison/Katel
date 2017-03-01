package nson.katel;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class SignUpBusinessStep1 extends AppCompatActivity {
    private ActionBar actionBar;
    private ImageView imv_avatar_businness;
    private Button btn_cam,btn_lib,btn_next;
    private TextView txt_loai;
    private Switch swc_chon;
    private EditText edt_ten_doanhnghiep;
    private static final int CODE_CAM = 2;
    private static final int CODE_LIBRARY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_business_step1);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


        imv_avatar_businness =(ImageView)findViewById(R.id.imgv_signup_business_step1_avatar);
        btn_cam =(Button)findViewById(R.id.btn_signup_businness_step1_camera);
        btn_lib =(Button)findViewById(R.id.btn_signup_businness_step1_library);
        btn_next =(Button)findViewById(R.id.btn_signup_businness_step1_next);
        txt_loai =(TextView)findViewById(R.id.txt_signup_business_step1_info);
        swc_chon =(Switch)findViewById(R.id.switch_signup_business_step1);
        edt_ten_doanhnghiep =(EditText)findViewById(R.id.edt_signup_businnes_step1_name);


        swc_chon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    txt_loai.setText("Loại hình kinh doanh của bạn: HOTEL");
                }else {
                    txt_loai.setText("Loại hình kinh doanh của bạn: KARAOKE");
                }
            }
        });
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
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpBusinnessStep2.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CAM && resultCode ==RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imv_avatar_businness.setImageBitmap(bitmap);
        }
        if (requestCode == CODE_LIBRARY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }catch (IOException e) {
                e.printStackTrace();
            }
            imv_avatar_businness.setImageBitmap(bitmap);
        }
    }
}