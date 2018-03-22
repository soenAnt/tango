package com.tango;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilePage extends AppCompatActivity {

    ImageView profilePicture;
    Button button;
    TextView username;
    TextView email;
    private static int PICK_IMAGE = 100;
    Uri imageInGallery;           // This is the image inside the gallery

    //Access to Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        username = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.email);

        // This code was used when answers redirected to a profile page which is no longer the case
        //      Intent intent = getIntent();
        //      username.setText(intent.getStringExtra("username"));

        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        button = (Button) findViewById(R.id.changepicture);

        // Get the current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // Setting username and email
        username.setText(usernameFromEmail(user.getEmail()));
        email.setText(user.getEmail());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    public void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);  // Only 100 images will appear when gallery is opened because PICK_IMAGE=100
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageInGallery = data.getData();
            profilePicture.setImageURI(imageInGallery);   // Set the profile picture to rhe image selected in the gallery
        }
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

}
