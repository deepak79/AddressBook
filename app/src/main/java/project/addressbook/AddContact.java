package project.addressbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by deepakgavkar on 18/12/16.
 */
public class AddContact extends AppCompatActivity {

    CircleImageView imgProfile;
    EditText etName, etMobileNo, etAddress, etEmail;
    Button btnAdd, btnClear, btnBack;
    Bundle extras;
    String id = "", flag = "";
    private DBHelper mydb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);

        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        etName = (EditText) findViewById(R.id.etName);
        etMobileNo = (EditText) findViewById(R.id.etMobileNo);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etEmail = (EditText) findViewById(R.id.etEmail);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnBack = (Button) findViewById(R.id.btnBack);

        mydb = new DBHelper(this);
        extras = getIntent().getExtras();

        if (extras != null) {
            //flag 0 for edit
            //flag 1 for view only
            id = extras.getString("id");
            flag = extras.getString("flag");

            if (id != null && !id.equals("")) {
                if (flag != null && flag.equals("0")) {
                    setTitle("Edit Contact");
                    btnAdd.setText("Update");

                    AddressStruct address;

                    address = mydb.getData(Integer.parseInt(id));

                    byte[] decodedString = Base64.decode(address.getProfile(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imgProfile.setImageBitmap(decodedByte);

                    etName.setText(address.getName());
                    etMobileNo.setText(address.getMobileno());
                    etAddress.setText(address.getAddress());
                    etEmail.setText(address.getEmail());

                } else if (flag != null && flag.equals("1")) {
                    setTitle("Contact");

                    btnBack.setVisibility(View.VISIBLE);
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent i = new Intent(AddContact.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    });
                    imgProfile.setFocusable(false);
                    etName.setFocusable(false);
                    etMobileNo.setFocusable(false);
                    etAddress.setFocusable(false);
                    etEmail.setFocusable(false);


                    imgProfile.setFocusableInTouchMode(false);
                    etName.setFocusableInTouchMode(false);
                    etMobileNo.setFocusableInTouchMode(false);
                    etAddress.setFocusableInTouchMode(false);
                    etEmail.setFocusableInTouchMode(false);

                    imgProfile.setClickable(false);
                    etName.setClickable(false);
                    etMobileNo.setClickable(false);
                    etAddress.setClickable(false);
                    etEmail.setClickable(false);

                    btnAdd.setVisibility(View.GONE);
                    btnClear.setVisibility(View.GONE);

                    AddressStruct address;

                    address = mydb.getData(Integer.parseInt(id));

                    byte[] decodedString = Base64.decode(address.getProfile(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imgProfile.setImageBitmap(decodedByte);

                    etName.setText(address.getName());
                    etMobileNo.setText(address.getMobileno());
                    etAddress.setText(address.getAddress());
                    etEmail.setText(address.getEmail());
                }
            }
        }

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate() == true) {
                    if (id != null && !id.equals("") && flag != null && !flag.equals("")) {
                        if (flag.equals("0")) {
                            imgProfile.buildDrawingCache();
                            Bitmap bmap = imgProfile.getDrawingCache();
                            String image = getEncoded64ImageStringFromBitmap(bmap);

                            mydb.updateContact(Integer.parseInt(id), etName.getText().toString(), etMobileNo.getText().toString(), etAddress.getText().toString(), etEmail.getText().toString(), image);
                            clearFields();
                            Toast.makeText(AddContact.this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else if (id != null && id.equals("")) {
                        imgProfile.buildDrawingCache();
                        Bitmap bmap = imgProfile.getDrawingCache();
                        String image = getEncoded64ImageStringFromBitmap(bmap);

                        mydb.insertContact(etName.getText().toString(), etMobileNo.getText().toString(), etAddress.getText().toString(), etEmail.getText().toString(), image);
                        clearFields();
                        Toast.makeText(AddContact.this, "Contact added successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFields();
            }
        });

    }

    public boolean validate() {

        if (etName.getText().toString().length() < 1) {
            Toast.makeText(AddContact.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etMobileNo.getText().toString().length() < 1) {
            Toast.makeText(AddContact.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etMobileNo.getText().toString().length() < 9) {
            Toast.makeText(AddContact.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etAddress.getText().toString().length() < 1) {
            Toast.makeText(AddContact.this, "Please enter your address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etEmail.getText().toString().length() < 1) {
            Toast.makeText(AddContact.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Utils.isValidEmail(etEmail.getText().toString())) {
            Toast.makeText(AddContact.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void clearFields() {
        imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.profile));
        etName.setText("");
        etMobileNo.setText("");
        etAddress.setText("");
        etEmail.setText("");
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        String imgString = "";
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            byte[] byteFormat = stream.toByteArray();
            // get the base 64 string
            imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        }

        return imgString;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AddContact.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
