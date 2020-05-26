package com.sarath.m_easybuy;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class postFragment extends Fragment {

    public postFragment() {
        // Required empty public constructor
    }

    private ImageView imageViewUpload;
    private Button buttonPostAd;
    private EditText editTextTitle,editTextDescription,editTextPrice,editTextPhone;
    private String title,description,price,phone,imageurl;
    private ProgressBar progressBar;
    private StorageReference storageReference;
    private DocumentReference documentReference;
    private FirebaseUser user;
    private FirebaseFirestore fstore;


    String adId;
    Uri resultUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        imageViewUpload = view.findViewById(R.id.imageViewUpload);
        imageViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        buttonPostAd = view.findViewById(R.id.buttonPostAd);
        buttonPostAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAd();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        fstore = FirebaseFirestore.getInstance();

        documentReference = fstore.collection("ads").document();
        adId = documentReference.getId();

        storageReference = FirebaseStorage.getInstance().getReference("images");

        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        progressBar = view.findViewById(R.id.progressbar);

        progressBar.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri selectedImageUri = data.getData();
            CropImage.activity(selectedImageUri)
                    .setAspectRatio(1,1)
                    .start(getContext(),this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                resultUri = result.getUri();
                imageViewUpload.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void FileUploader(){
        final StorageReference ref = storageReference.child(adId+"."+"jpeg");
        ref.putFile(resultUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageurl = uri.toString();
                                Log.d("dd",imageurl);
                                documentReference.update("image",imageurl);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getActivity(),"Image not uploaded",Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void postAd(){
        progressBar.setVisibility(View.VISIBLE);

        FileUploader();

        title = editTextTitle.getText().toString().trim();
        description = editTextDescription.getText().toString().trim();
        price = editTextPrice.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();

        if(title.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            editTextTitle.setError("Title Required");
            editTextTitle.requestFocus();
            return;
        }
        if(title.length()>50){
            progressBar.setVisibility(View.INVISIBLE);
            editTextTitle.setError("Title is too long");
            editTextTitle.requestFocus();
            return;
        }

        if(price.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            editTextPrice.setError("Price Required");
            editTextPrice.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            editTextPhone.setError("Mobile number Required");
            editTextPhone.requestFocus();
            return;
        }
        if(phone.length()<10){
            progressBar.setVisibility(View.INVISIBLE);
            editTextPhone.setError("Enter a valid phone number");
            editTextPhone.requestFocus();
            return;
        }

        editTextTitle.setText("");
        editTextDescription.setText("");
        editTextPhone.setText("");
        editTextPrice.setText("");

        String userid,username;

        userid = user.getUid();
        username = user.getDisplayName();

        Map<String,Object> adDetails = new HashMap<>();
        adDetails.put("phone",phone);
        adDetails.put("price",price);
        adDetails.put("description",description);
        adDetails.put("title",title);
        adDetails.put("publisherName",username);
        adDetails.put("publisherID",userid);
        adDetails.put("timestamp", FieldValue.serverTimestamp());

        documentReference.set(adDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Ad posted", Toast.LENGTH_LONG).show();
                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
