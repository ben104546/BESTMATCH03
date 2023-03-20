package com.example.a5dayappchat2.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesUser;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateDoingsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE_PROFILE_IMAGE = 102;

    Button timeButton,btAddPicture,btCreateDoings,timeButton2,timeButton3;
    private DatePickerDialog datePickerDialog;
    int hour, minute;
    String groupToken,userId;
    EditText etDoingsName,etDoingsDescription;
    CircleImageView ciDoingProfile;
    ImageView imageView16;

    ArrayList<Uri> imageList;
    Uri imageUri,profileImageUri;
    private int upload_count = 0;
    PreferencesUser preferencesUser;
    String strDateOfCreateDoing;

    DatabaseReference mGroupRef;
    StorageReference mDoingsRef,mDoingFolder,mProfileDoings;

    FirebaseFirestore db;

    String SelectModeDate = "noting";

    RadioButton radioButtonOneDay,radioButtonManyDay ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_doings);

        preferencesUser = new PreferencesUser(getApplicationContext());

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        userId = preferencesUser.getString(Constant.USER_ID);
        preferencesUser = new PreferencesUser(getApplicationContext());

        db = FirebaseFirestore.getInstance();


        groupToken = getIntent().getStringExtra("groupTokenForCreateDoings");

        mDoingsRef = FirebaseStorage.getInstance().getReference().child("ActivityImage");
        mDoingFolder = FirebaseStorage.getInstance().getReference().child("ActivityImageFolder");
        mProfileDoings = FirebaseStorage.getInstance().getReference().child("ActivityProfile");

        etDoingsName = findViewById(R.id.etDoingsName);
        etDoingsDescription = findViewById(R.id.etDoingsDescription);
        ciDoingProfile = findViewById(R.id.ciDoingProfile);
        timeButton2 = findViewById(R.id.DateButton1);
        timeButton3 = findViewById(R.id.DateButton2);

        imageView16 = findViewById(R.id.imageView16);

        btCreateDoings = findViewById(R.id.btCreateDoings);
        timeButton = findViewById(R.id.timeButton1);
        btAddPicture = findViewById(R.id.btAddPicture);

        radioButtonOneDay = findViewById(R.id.radioButtonOneDay);
        radioButtonManyDay = findViewById(R.id.radioButtonManyDay);

        imageList = new ArrayList<Uri>();




        initDatePicker();

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker();
            }
        });

        ciDoingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_PROFILE_IMAGE);
            }
        });

        btCreateDoings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateDoings();
                UploadImage();



            }
        });

        btAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });





        MaterialDatePicker.Builder<Long> builder2 = MaterialDatePicker.Builder.datePicker();
        builder2.setTitleText("Select a date");

        MaterialDatePicker materialDatePicker2 = builder2.build();

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a date");

        MaterialDatePicker materialDatePicker = builder.build();



        timeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SelectModeDate.equals("noting")){

                    Toast.makeText(CreateDoingsActivity.this, "Please Choose Time", Toast.LENGTH_SHORT).show();

                }else if(SelectModeDate.equals("OneDay")){


                    materialDatePicker2.show(getSupportFragmentManager(),"DATE_PICKER");

                }else {

                    materialDatePicker2.show(getSupportFragmentManager(),"DATE_PICKER");
                }


            }
        });

        timeButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SelectModeDate.equals("noting")){

                    Toast.makeText(CreateDoingsActivity.this, "Please Choose Time", Toast.LENGTH_SHORT).show();

                }else if(SelectModeDate.equals("OneDay")){

                    materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");

                }else {

                    materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
                }


            }
        });



        materialDatePicker2.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((Long) selection);
                SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                String formattedDate  = format.format(calendar.getTime());

                timeButton2.setText(formattedDate);




            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((Long) selection);
                SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                String formattedDate  = format.format(calendar.getTime());

                timeButton3.setText(formattedDate);
            }
        });

        radioButtonOneDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectModeDate = "OneDay";
                imageView16.setVisibility(View.GONE);
                timeButton3.setVisibility(View.GONE);

            }
        });

        radioButtonManyDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectModeDate = "ManyDay";
                imageView16.setVisibility(View.VISIBLE);
                timeButton3.setVisibility(View.VISIBLE);
            }
        });



    }



    private void CreateDoings() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss");
        strDateOfCreateDoing = formatter.format(date);

        String DoingsName = etDoingsName.getText().toString() ;
        String DoingsDescription  = etDoingsDescription.getText().toString();
        String Time = timeButton.getText().toString();
        String Date = timeButton2.getText().toString();
        String Date2 = timeButton3.getText().toString();


        mProfileDoings.child(groupToken).putFile(profileImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mProfileDoings.child(groupToken).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            HashMap hashMap = new HashMap();
                            hashMap.put("DoingsName",DoingsName);
                            hashMap.put("DoingsDescription",DoingsDescription);
                            hashMap.put("DoingsDate",Date);
                            hashMap.put("DoingsTime",Time);
                            hashMap.put("Doing_Key",userId+strDateOfCreateDoing);
                            hashMap.put("DoingsImage",uri.toString());


                            if(SelectModeDate.equals("OneDay")){
                                mGroupRef.child(groupToken).child("Activity").child(userId+strDateOfCreateDoing).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        AddPeopleForActivity();
                                        Toast.makeText(CreateDoingsActivity.this, "Create Activity Success", Toast.LENGTH_SHORT).show();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(CreateDoingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {

                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                                    Date Date01 = sdf.parse(Date);
                                    Date Date02 = sdf.parse(Date2);

                                    if(Date01.getTime() > Date02.getTime()){
                                        Toast.makeText(CreateDoingsActivity.this, "Time Start More Than Time Ends", Toast.LENGTH_SHORT).show();
                                    }else {
                                        hashMap.put("DoingsDateEND",Date2);

                                        mGroupRef.child(groupToken).child("Activity").child(userId+strDateOfCreateDoing).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {

                                                AddPeopleForActivity();
                                                Toast.makeText(CreateDoingsActivity.this, "Create Activity Success", Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CreateDoingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateDoingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void popTimePicker() {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


    private void initDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "January";
        if(month == 2)
            return "February";
        if(month == 3)
            return "March";
        if(month == 4)
            return "April";
        if(month == 5)
            return "May";
        if(month == 6)
            return "June";
        if(month == 7)
            return "July";
        if(month == 8)
            return "August";
        if(month == 9)
            return "September";
        if(month == 10)
            return "October";
        if(month == 11)
            return "November";
        if(month == 12)
            return "December";

        //default should never happen
        return "June";
    }

    private String getMonthFormatNew(String month) {
        if(month == "Jan")
            return "January";
        if(month == "Feb")
            return "February";
        if(month == "Mar")
            return "March";
        if(month == "Apr")
            return "April";
        if(month == "May")
            return "May";
        if(month == "Jun")
            return "June";
        if(month == "Jul")
            return "July";
        if(month == "Aug")
            return "August";
        if(month == "Sep")
            return "September";
        if(month == "Oct")
            return "October";
        if(month == "Nov")
            return "November";
        if(month == "Dec")
            return "December";

        //default should never happen
        return "June";
    }

    private void ActionSelectDate() {

        datePickerDialog.show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data.getClipData() != null){

            Integer countClipData = data.getClipData().getItemCount();

            int currentImageSelect = 0;


            while (currentImageSelect < countClipData){

                imageUri = data.getClipData().getItemAt(currentImageSelect).getUri();

                imageList.add(imageUri);

                currentImageSelect = currentImageSelect + 1;

            }


        }else {
            Uri uri = data.getData();
            if (uri != null) {

                final StorageReference ImageName = mDoingFolder.child("Image"+uri.getLastPathSegment());

                ImageName.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String  url = String.valueOf(uri);

                                Storelink(url);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateDoingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }

        if(requestCode == REQUEST_CODE_PROFILE_IMAGE && resultCode == RESULT_OK && data != null){
            profileImageUri=data.getData();
            ciDoingProfile.setImageURI(profileImageUri);

        }
    }

    private void UploadImage() {


        for (upload_count = 0 ; upload_count<imageList.size() ; upload_count++){

            Uri IndividualImage = imageList.get(upload_count);
            final StorageReference ImageName = mDoingFolder.child("Image"+IndividualImage.getLastPathSegment());

            ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String  url = String.valueOf(uri);

                            Storelink(url);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateDoingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });





        }




    }

    private void Storelink(String url) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss");
        String strDate = formatter.format(date);


        HashMap hashMap = new HashMap();
        hashMap.put("ImageLink",url);
        hashMap.put("User_Id",userId);


        db.collection(userId+strDateOfCreateDoing+Constant.IMAGE_DOINGS).add(hashMap);

        mGroupRef.child(groupToken).child("Activity").child(userId+strDateOfCreateDoing).child("ImageActivity").push().updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(CreateDoingsActivity.this, "Add Image Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateDoingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void AddPeopleForActivity() {
        mGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userid = preferencesUser.getString(Constant.USER_ID);
                    String UserName =snapshot.child(groupToken).child("Member").child(userid).child("username").getValue().toString();
                    String UserProfile = snapshot.child(groupToken).child("Member").child(userid).child("profileImage").getValue().toString();

                    HashMap hashMap = new HashMap();
                    hashMap.put("User_Id",userid);
                    hashMap.put("username",UserName);
                    hashMap.put("profileImage",UserProfile);

                    db.collection(userId+strDateOfCreateDoing+Constant.MEMBER_DOINGS).document(userid).set(hashMap);

                    mGroupRef.child(groupToken).child("Activity").child(userId+strDateOfCreateDoing).child("PeopleOfActivity").child(userid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(CreateDoingsActivity.this, "Update People Success", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateDoingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }else {
                    Toast.makeText(CreateDoingsActivity.this, "Load Data Error", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreateDoingsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}