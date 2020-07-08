package com.example.blitz_github.Account;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blitz_github.About;
import com.example.blitz_github.Address;
import com.example.blitz_github.Contact;
import com.example.blitz_github.Home_activity;
import com.example.blitz_github.R;
import com.example.blitz_github.Utils.CustomLogin;
import com.example.blitz_github.Utils.Project.ProjectAdapter;
import com.example.blitz_github.Utils.Project.projectModel;
import com.example.blitz_github.Utils.registerHelper;
import com.example.blitz_github.dialoguebox.Addressdialogboxclass;
import com.example.blitz_github.dialoguebox.KeySkillActivity;
import com.example.blitz_github.dialoguebox.Languagedialogboxclass;
import com.example.blitz_github.dialoguebox.companyname_dialogboxclass;
import com.example.blitz_github.dialoguebox.keyskill_dialogueboxclass;
import com.example.blitz_github.dialoguebox.phonenumber_dialogboxclass;
import com.example.blitz_github.dialoguebox.username_dialogboxclass;

import com.example.blitz_github.Utils.Project.projects;
import com.example.blitz_github.dialoguebox.resumeHeadline;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

public class Complete_Registration extends AppCompatActivity {

    private TextView skilldisplay,projectlist, addresslist, languagelist, pdftext;
    TextView name,email,mobile,address,company,resumeheadline;


    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference notebookRef;//1
    private ProjectAdapter adapter;



    private ImageView profilePic;
    Map<String, Object> extradata=new HashMap<>();
    Uri resultUri;
    int resumeUploadChecker=0;
    int imageUploadChecker=0;

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "testing";
    registerHelper Helper;
    FirebaseUser firebaseUser;
    String Uid;
    Bundle extra;
    Context context;
    String addressText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete__registration);


        Helper = new registerHelper(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, login_register.class));
            finish();
        } else {
            notebookRef = db.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("Projects");
            Uid = firebaseUser.getUid();

        context = this;
        skilldisplay = findViewById(R.id.skilldisplay_id);
        // projectlist= findViewById(R.id.textView666);//new
        addresslist = findViewById(R.id.addresslist);
        languagelist = findViewById(R.id.languagelist);
        pdftext = findViewById(R.id.pdftext);
        profilePic = findViewById(R.id.profilePic);
        resumeheadline = findViewById(R.id.textView5);

        name = findViewById(R.id.textView);
        email = findViewById(R.id.textView53);
        mobile = findViewById(R.id.textView30);
        address = findViewById(R.id.textView3);
        company = findViewById(R.id.textView32);
        setUpRecyclerView();
        Helper.setListener(new registerHelper.MyListener() {
            @Override
            public void onDataLoaded() {
                Helper.markUpload(Uid);
                startActivity(new Intent(context, Home_activity.class));
                finish();
            }

            @Override
            public void isUserRegistered(boolean isPresent) {
                if (isPresent && extra != null) {
                    startActivity(new Intent(context, Home_activity.class));

                    finish();
                } else {
                    //Load data and render it but here is a catch we will be receiving only half data of upper part
                    //so render only that data and make rest at empty
                    Helper.getDataToRender(Uid);
                    Toast.makeText(context, "Please Enter the Details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void renderData(DocumentSnapshot snapshot) {
                //Implement this and render data
                if (snapshot != null) {
                    name.setText(snapshot.getString("name"));
                    email.setText(snapshot.getString("email"));
                    mobile.setText(snapshot.getString("mobile"));

                    if (snapshot.getString("Address") != null) {
                        String val = snapshot.getString("Address");
                        String[] arr = val.split(", ");
                        int n = arr.length;
                        address.setText(arr[n - 1]);
                    }
                    if (snapshot.getString("Company") != null) {
                        company.setText(snapshot.getString("Company"));
                    }
                    if (snapshot.getString("Image_url") != null) {
                        resultUri = Uri.parse(snapshot.getString("Image_url"));
                        Picasso.get().load(snapshot.getString("Image_url")).into(profilePic);
                        imageUploadChecker = 1;
                    }
                    if (snapshot.getString("ResumeHeadline") != null) {
                        resumeheadline.setText(snapshot.getString("ResumeHeadline"));
                    }
                    if (snapshot.getString("Skill") != null) {
                        skilldisplay.setText(snapshot.getString("Skill"));
                    }
                    if (snapshot.getString("Address") != null) {
                        addressText = snapshot.getString("Address");
                        addresslist.setText("House No.-"+addressText);
                    }
                    if (snapshot.getString("Language") != null) {
                        languagelist.setText(snapshot.getString("Language"));
                    }
                    if (snapshot.getString("Resume_url") != null) {
                        pdftext.setText(snapshot.getString("Resume_url"));
                        resumeUploadChecker = 1;
                    } else {
                        resumeUploadChecker = 0;
                    }
                } else {
                    Toast.makeText(Complete_Registration.this, "Unable to render data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        extra = getIntent().getExtras();
        if (extra == null) {
            //find data and render it
            Helper.getDataToRender(Uid);
        } else {
            Helper.checkUserData(Uid);
        }



    }
    }

    private void setUpRecyclerView() {
        Query query=notebookRef;//

        FirestoreRecyclerOptions<projectModel> options=new FirestoreRecyclerOptions.Builder<projectModel>()
                .setQuery(query,projectModel.class)
                .build();

        adapter=new ProjectAdapter(options);

        //  BucketRecyclerView recyclerView=findViewById(R.id.recycler_view_notification);//1
        RecyclerView recyclerView=findViewById(R.id.project_recycler_view);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        Log.v("methods","setup function for recycler view");//

        // mEmptyView=findViewById(R.id.empty_drops);//1
        //   recyclerView.showIfEmpty(mEmptyView);

        //swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                adapter.deleteItem(viewHolder.getAdapterPosition());

                Toast.makeText(getApplicationContext(),"project deleted",Toast.LENGTH_SHORT).show();//
            }
        }).attachToRecyclerView(recyclerView);// attach the recycler view to touch helper

    }



    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if( resumeUploadChecker==0 || imageUploadChecker==0)
        {
            finish();
        }
        else
        {
            startActivity(new Intent(this,Home_activity.class));
            finish();
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==FILE_SELECT_CODE){
            if(resultCode==RESULT_OK)
            {
                Uri uri = data.getData();
                ContentResolver cR = this.getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String type = mime.getExtensionFromMimeType(cR.getType(uri));
                if(type.equals("pdf")){
                    Helper.Upload_Resume(uri,Uid);
                    pdftext.setText(uri.toString());
                    resumeUploadChecker=1;
                }
                else{
                    Toast.makeText(this, "Please Select the PDF File", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                profilePic.setImageURI(resultUri);
                imageUploadChecker=1;

                //do something
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if(resultCode==2000)
        {
            String val = data.getStringExtra("showThis");
            String[] arr =val.split(", ");
            int n = arr.length;
            address.setText(arr[n-1]);
            addresslist.setText("House No.-"+val);
        }
        if(resultCode==2323)
        {
            String val = data.getStringExtra("keyskills");
            skilldisplay.setText(val);
        }
    }

    //onclick listeners for displaying the dialogboxex
    public void companyname_Dialogbox(View view)
    {
        String companynamestring=company.getText().toString();
        companyname_dialogboxclass companynameclass=new companyname_dialogboxclass(companynamestring,company);
        companynameclass.show(getSupportFragmentManager(), "company name");

    }

    public void activityresumeHeadline(View view) {


        resumeHeadline resumeheadlineobj = new resumeHeadline(resumeheadline.getText().toString(), resumeheadline);
        resumeheadlineobj.show(getSupportFragmentManager(), "resume headline");

    }

    public void phonenumber_Dialogbox(View view)
    {
        String phonenumberstring=mobile.getText().toString();
        phonenumber_dialogboxclass phonenumberclass=new phonenumber_dialogboxclass(phonenumberstring,mobile);
        phonenumberclass.show(getSupportFragmentManager(), "phone number");

    }
    public void username_Dialogbox(View view)
    {
        String usernamestring=name.getText().toString();
        username_dialogboxclass usernameclass=new username_dialogboxclass(usernamestring,name);
        usernameclass.show(getSupportFragmentManager(), "user name");

    }

    public void key_skillDialoguebox(View view)
        {
            String skillstring=skilldisplay.getText().toString();
            Intent intent=new Intent(Complete_Registration.this, KeySkillActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("skillstring", skillstring);
            intent.putExtras(bundle);
            startActivityForResult(intent,234);

    }
    public void addproject(View view)
    {
        startActivity(new Intent(this, projects.class));
    }
    public void addressDialoguebox(View view)
    {
        Intent intent = new Intent(this, Address.class);
        intent.putExtra("Address",addressText);
        startActivityForResult(intent,6715);
    }
    public void languageDialoguebox(View view)
    {
        String languagestring=languagelist.getText().toString();
        Languagedialogboxclass languagedialogboxclass=new Languagedialogboxclass(languagestring,languagelist);
        languagedialogboxclass.show(getSupportFragmentManager(), "project");
    }

    public void resumeDialoguebox(View view)
    {
        //popups the file selector to input resume file
        showFileChooser();

    }

    //onclick submit button
    public void submit(View view){
        if(resumeUploadChecker==1&&imageUploadChecker==1) {
            String namestring=name.getText().toString();
            extradata.put("name", namestring);
            String mobilestring = mobile.getText().toString();
            extradata.put("mobile", mobilestring);
            String companystring=company.getText().toString();
            extradata.put("Company", companystring);

            String skillstring = skilldisplay.getText().toString();
            extradata.put("Skill", skillstring);
            String languagestring = languagelist.getText().toString();
            extradata.put("Language", languagestring);

            String headline=resumeheadline.getText().toString();
            extradata.put("ResumeHeadline", headline);

            Log.v("skill: ", skillstring);
            Log.v("Photo Uri:",resultUri.toString());
            //resultUri taken from onActivityResult function above
            Helper.Upload_Data(extradata, Uid, resultUri);
        }
        else if(resumeUploadChecker==1&&imageUploadChecker==0){
            Toast.makeText(this,"Upload Image",Toast.LENGTH_SHORT).show();
        }
        else if(resumeUploadChecker==0&&imageUploadChecker==1){
            Toast.makeText(this,"Upload Resume",Toast.LENGTH_SHORT).show();
        }

        else {
            Toast.makeText(this,"Upload Resume and image",Toast.LENGTH_SHORT).show();
        }
    }

    //onclick listener for the image in uppersection
    public void picture(View view){
        takePicture();
    }

    private void takePicture(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Please Try Again!", Toast.LENGTH_LONG).show();
                //the toast message will appear for the deny for the first time but in below line the connection is established
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                //when there is no problem for accessing the storage. the Intent is been created here to choose the file from the storage
                //here Crop dependence is been used from git hub for cropping
                CropImage.activity()
                        .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.registeration_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.about_us_r:
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            case R.id.contact_r:
                Intent intent1 = new Intent(this, Contact.class);
                startActivity(intent1);
                return true;
            case R.id.log_out_r:
                Logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void Logout() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth!=null)
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,login_register.class));
        finish();
    }







}