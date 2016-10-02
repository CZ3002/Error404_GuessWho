//package com.example.shrey_000.guesswho.PersonalCollection;
//
//import android.content.Intent;
//
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//
//import android.support.v7.app.AppCompatActivity;
//import android.util.Base64;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//
//import com.example.shrey_000.guesswho.HomeActivity;
//import com.example.shrey_000.guesswho.R;
//
//import java.io.ByteArrayOutputStream;
//
//import Entities.Acquaintance;
//import Utilities.MySQLImpl;
//
//public class CameraActivity extends AppCompatActivity {
//    Button cameraButton,b2;
//    ImageView imageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//
//        cameraButton=(Button)findViewById(R.id.cameraButton);
//        imageView=(ImageView)findViewById(R.id.imageView);
//
//        cameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 0);
//            }
//        });
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode != 0) {
//            Bitmap bp = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(bp);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void addToPC(View view){
//        Bitmap bm = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//        String name = ((EditText)findViewById(R.id.name)).getText().toString();
//        String contact = ((EditText)findViewById(R.id.contact)).getText().toString();
//        String relationship = ((EditText)findViewById(R.id.relationship)).getText().toString();
//        String note = ((EditText)findViewById(R.id.note)).getText().toString();
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//        String base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
//
//        Acquaintance acq = new Acquaintance("gupta", name, relationship, contact, note, base64, "","","","","","");
//
//        MySQLImpl dbm = new MySQLImpl("jdbc:mysql://10.27.143.227:3306/guesswho","pma","");
//        dbm.insertPC(acq);
//
//        goToView();
//    }
//
//    public void goToView(){
//        Intent intentMain = new Intent(this,HomeActivity.class);
//        startActivity(intentMain);
//    }
//}