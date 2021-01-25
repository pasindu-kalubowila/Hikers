package com.example.hikers.activities;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hikers.dialogs.SampleDialog;
import com.example.hikers.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import java.util.HashMap;
import java.util.Objects;


public class PostEventActivity extends AppCompatActivity {

    Uri imageUri;
    String myUrl;
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView close;
    ImageView event_add;
    TextView event_post;
    EditText event_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        event_add = findViewById(R.id.event_add);
        close = findViewById(R.id.close);
        event_post = findViewById(R.id.event_post);
        event_description = findViewById(R.id.event_description);

        storageReference = FirebaseStorage.getInstance().getReference("Event");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostEventActivity.this, MainActivity.class));
            }
        });

        event_post.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
            }
        });

        CropImage.activity()
                .setAspectRatio(4, 2)
                .start(PostEventActivity.this);

    }

    private String getFileExtenstion(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void uploadImage() {
        final SampleDialog sampleDialog = new SampleDialog();
        sampleDialog.show(getSupportFragmentManager(), "DONE");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);



        if (imageUri != null) {

            final StorageReference filerefrence = storageReference.child(System.currentTimeMillis() + "." + getFileExtenstion(imageUri));
            uploadTask = filerefrence.putFile(imageUri);


            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isComplete()) {
                        throw task.getException();
                    }
                    return filerefrence.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        assert downloadUri != null;
                        myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Event");

                        String eventid = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("eventid", eventid);
                        hashMap.put("eventimage", myUrl);
                        hashMap.put("eventdescription", event_description.getText().toString());
                        hashMap.put("publisher", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

                        assert eventid != null;
                        reference.child(eventid).setValue(hashMap);
                        sampleDialog.dismiss();
                        progressDialog.dismiss();
                        finish();

                    } else {
                        Toast.makeText(PostEventActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostEventActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Image Selected!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result != null) {
                imageUri = result.getUri();
            }


            Picasso.get().load(imageUri).placeholder(R.drawable.placeholder).into(event_add);


        } else {
            Toast.makeText(this, "Something gone wrong", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}
