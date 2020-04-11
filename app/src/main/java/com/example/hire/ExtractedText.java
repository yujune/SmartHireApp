package com.example.hire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hire.databinding.ActivityFabForExtactedBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtractedText extends AppCompatActivity {

    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;
    Intent intent;
    private String extractedPhoneNumber,extractedEmail,extractedName,extractedAddress,extractedSkills,extractedEducation,extractedOther,extractedFace,resume;
    private int extractedAge;
    private static final int EDIT_EXTRACTED_TEXT_CODE = 6;

    //google firebase database
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    private Uri photoUri,resumeUri,photoDownloadUri,resumeDownloadUri;

    private ActivityFabForExtactedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFabForExtactedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //firebase
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        intent = getIntent();

        extractedPhoneNumber = intent.getStringExtra("EXTRACTED_PHONE");
        extractedEmail = intent.getStringExtra("EXTRACTED_EMAIL");
        extractedName = intent.getStringExtra("EXTRACTED_NAME");
        extractedAddress = intent.getStringExtra("EXTRACTED_ADDRESS");
        extractedAge = intent.getIntExtra("EXTRACTED_AGE",0);
        extractedSkills = intent.getStringExtra("EXTRACTED_SKILLS");
        extractedEducation = intent.getStringExtra("EXTRACTED_EDUCATION");
        //extractedOther = intent.getStringExtra("EXTRACTED_OTHER");

        extractedFace = intent.getStringExtra("EXTRACTED_FACE");
        resume = intent.getStringExtra("RESUME");
        resumeUri = Uri.parse(resume);
        photoUri = Uri.parse(extractedFace);

        binding.include.textViewExtractedPhoneNum.setText(extractedPhoneNumber);
        binding.include.textViewExtractedEmail.setText(extractedEmail);
        binding.include.textViewExtractedName.setText(extractedName);
        binding.include.textViewExtractedAddress.setText(extractedAddress);
        binding.include.textViewExtractedAge.setText(Integer.toString(extractedAge));
        binding.include.textViewExtractedSkills.setText(extractedSkills);
        binding.include.textViewExtractedEducation.setText(extractedEducation);
        //textViewExtractedOther.setText(extractedOther);
        binding.include.imageViewExtractedImage.setImageURI(photoUri);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        binding.fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                Intent intent2 = new Intent(ExtractedText.this,ExtractedTextEdit.class);
                intent2.putExtra("EXTRACTED_NAME",extractedName);
                intent2.putExtra("EXTRACTED_PHONE",extractedPhoneNumber);
                intent2.putExtra("EXTRACTED_EMAIL",extractedEmail);
                intent2.putExtra("EXTRACTED_ADDRESS",extractedAddress);
                intent2.putExtra("EXTRACTED_AGE",extractedAge);
                intent2.putExtra("EXTRACTED_SKILLS",extractedSkills);
                intent2.putExtra("EXTRACTED_EDUCATION",extractedEducation);
                intent2.putExtra("EXTRACTED_FACE",photoUri.toString());

                startActivityForResult(intent2,EDIT_EXTRACTED_TEXT_CODE);
            }
        });

        binding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    //mProgressBar.setVisibility(ProgressBar.VISIBLE);
                    Toast.makeText(ExtractedText.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadToDatabase();

                }
            }
        });

    }

    private void animateFab(){
        if (isOpen){
            binding.fab.startAnimation(rotateBackward);
            binding.fabEdit.startAnimation(fabClose);
            binding.fabSave.startAnimation(fabClose);
            //fabExtract.startAnimation(fabClose);
            binding.fabEdit.setClickable(false);
            binding.fabSave.setClickable(false);
            //fabExtract.setClickable(false);
            isOpen=false;
        }else{
            binding.fab.startAnimation(rotateForward);
            binding.fabEdit.startAnimation(fabOpen);
            binding.fabSave.startAnimation(fabOpen);
            //fabExtract.startAnimation(fabOpen);
            binding.fabEdit.setClickable(true);
            binding.fabSave.setClickable(true);
            //fabExtract.setClickable(true);
            isOpen=true;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==EDIT_EXTRACTED_TEXT_CODE)
        {
            extractedName =data.getStringExtra("NEW_NAME");
            extractedPhoneNumber =data.getStringExtra("NEW_PHONE_NUM");
            extractedEmail =data.getStringExtra("NEW_EMAIL");
            extractedAddress =data.getStringExtra("NEW_ADDRESS");
            //final Bitmap bitmap = data.getParcelableExtra("NEW_PHOTO");
            //Bundle extras = data.getExtras();
            //byte[] b = extras.getByteArray("NEW_PHOTO");
            extractedFace = data.getStringExtra("NEW_PHOTO");
            photoUri = Uri.parse(extractedFace);
            //Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);


            binding.include.textViewExtractedPhoneNum.setText(extractedPhoneNumber);
            binding.include.textViewExtractedEmail.setText(extractedEmail);
            binding.include.textViewExtractedName.setText(extractedName);
            binding.include.textViewExtractedAddress.setText(extractedAddress);
            binding.include.imageViewExtractedImage.setImageURI(photoUri);
        }
    }

    //Firebase
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadToDatabase() {
        binding.progressBarExtracted.setVisibility(ProgressBar.VISIBLE);
        if (photoUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(photoUri));

            fileReference.putFile(photoUri).continueWithTask(
                    new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException(); }
                            return fileReference.getDownloadUrl();
                        } })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                photoDownloadUri = task.getResult();

                            }
                            else { Toast.makeText(ExtractedText.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ExtractedText.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

        if (resumeUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(resumeUri));

            fileReference.putFile(resumeUri).continueWithTask(
                    new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException(); }
                            return fileReference.getDownloadUrl();
                        } })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.progressBarExtracted.setVisibility(ProgressBar.INVISIBLE);
                                    }
                                }, 200);

                                resumeDownloadUri = task.getResult();
                                String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
                                Employee upload = new Employee(extractedName.trim(), photoDownloadUri.toString(),resumeDownloadUri.toString(),extractedAddress.trim(),extractedPhoneNumber.trim(),extractedEmail.trim(),timeStamp,extractedSkills.trim(),extractedEducation.trim(),extractedAge);
                                String uploadId = mDatabaseRef.push().getKey();
                                mDatabaseRef.child(uploadId).setValue(upload);
                                //mDatabaseRef.push().setValue(upload);
                                Toast.makeText(ExtractedText.this, "Upload successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ExtractedText.this,RecylerViewActivity.class);
                                startActivity(intent);
                            }
                            else { Toast.makeText(ExtractedText.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ExtractedText.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


}
