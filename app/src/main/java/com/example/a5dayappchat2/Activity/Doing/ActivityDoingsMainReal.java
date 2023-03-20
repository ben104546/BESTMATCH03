package com.example.a5dayappchat2.Activity.Doing;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.a5dayappchat2.Activity.ActivityDetailAndSetting.NotificationSpDate;
import com.example.a5dayappchat2.Activity.SetupActivity;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.ActivityDoingsMainRealBinding;
import com.example.a5dayappchat2.databinding.FragmentAllDoingsBinding;
import com.example.a5dayappchat2.databinding.FragmentDoingsJoinedBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import io.github.muddz.styleabletoast.StyleableToast;

public class ActivityDoingsMainReal extends AppCompatActivity {

    private ActivityDoingsMainRealBinding binding;

    BottomNavigationView bottomNavigationView;
    String groupToken;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FloatingActionButton fbtCreateDoings;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    CircleImageView ciDoingProfile;
    RadioButton radioButtonOneDay,radioButtonManyDay;
    EditText etDoingsName,etDoingsDescription;
    Button DateButton1,DateButton2,timeButton1,btAddPicture,btCreateDoings,btCancelActivity,timeButton2;
    ImageView imageView16,imageView17,imageView9,imageView14;

    private static final int REQUEST_CODE = 101;
    private static final int REQUEST_CODE_PROFILE_IMAGE = 102;

    private DatePickerDialog datePickerDialog;
    int hour, minute;

    ArrayList<Uri> imageList;
    Uri imageUri,profileImageUri;
    private int upload_count = 0;

    String strDateOfCreateDoing;

    DatabaseReference mGroupRef,mUserRef;
    StorageReference mDoingsRef,mDoingFolder,mProfileDoings;

    // FirebaseFirestore db;



    TextView tvEndDate,tvStartDate;

    String DoingToken;
    String DelActivity;

    boolean StageMethold1 ;
    boolean StageMethold2 ;
    boolean StageMethold3 ;





   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = ActivityDoingsMainRealBinding.inflate(getLayoutInflater());
       View view = binding.getRoot();

       FragmentAllDoingsBinding fragmentAllDoingsBinding = FragmentAllDoingsBinding.inflate(getLayoutInflater());
       View view2 = fragmentAllDoingsBinding.getRoot();
       setContentView(view2);

       FragmentDoingsJoinedBinding fragmentDoingsJoinedBinding = FragmentDoingsJoinedBinding.inflate(getLayoutInflater());
       View view3 = fragmentDoingsJoinedBinding.getRoot();
       setContentView(view3);


       setContentView(view);

       mAuth = FirebaseAuth.getInstance();
       mUser = mAuth.getCurrentUser();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
       fbtCreateDoings = findViewById(R.id.fbtCreateDoings);

       mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

       mDoingsRef = FirebaseStorage.getInstance().getReference().child("ActivityImage");
       mDoingFolder = FirebaseStorage.getInstance().getReference().child("ActivityImageFolder");
       mProfileDoings = FirebaseStorage.getInstance().getReference().child("ActivityProfile");
       mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");


       imageList = new ArrayList<Uri>();

        groupToken = getIntent().getStringExtra("GroupKey");
       DoingToken = getIntent().getStringExtra("DoingToken");
       DelActivity = getIntent().getStringExtra("DelActivity");
       
       
       DeleteActiy();


       Intent intent = new Intent(ActivityDoingsMainReal.this,AllDoings.class);
       Intent intent2 = new Intent(ActivityDoingsMainReal.this,DoingsJoined.class);

       Bundle bundle = new Bundle();
       bundle.putString("GroupKey", groupToken);
       intent.putExtras(bundle);
       intent2.putExtras(bundle);

       initDatePicker();

        replaceFragment(new AllDoings());


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.AllActivity:
                        replaceFragment(new AllDoings());
                        break;
                    case R.id.JoinActivity:
                        replaceFragment(new DoingsJoined());
                        break;
                }
                return true;
            }
        });


       fbtCreateDoings.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               
               OpenPopUpCreateActivity();


           }
       });



        StageMethold1 = false;
        StageMethold2 = false ;
        StageMethold3 = false;



    }

    private void DeleteActiy() {

       if(DoingToken != null){
           if(DelActivity != null){
               mGroupRef.child(groupToken).child("Activity").child(DoingToken).removeValue();
           }
       }


/*

        mGroupRef.child(groupToken).child("Activity").child(DoingToken).removeValue();



 */

    }


    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
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

        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
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

    TextView tvPageTitle;
   ImageView btBackPage;



    private void OpenPopUpCreateActivity() {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_create_activity,null);

        ciDoingProfile = contarctPopView.findViewById(R.id.ciDoingProfile);
        radioButtonOneDay = contarctPopView.findViewById(R.id.radioButtonOneDay);
        radioButtonManyDay = contarctPopView.findViewById(R.id.radioButtonManyDay);
        etDoingsName = contarctPopView.findViewById(R.id.etDoingsName);
        etDoingsDescription = contarctPopView.findViewById(R.id.etDoingsDescription);
        DateButton1 = contarctPopView.findViewById(R.id.DateButton1);
        DateButton2 = contarctPopView.findViewById(R.id.DateButton2);
        timeButton1 = contarctPopView.findViewById(R.id.timeButton1);
        btAddPicture = contarctPopView.findViewById(R.id.btAddPicture);
        btCreateDoings = contarctPopView.findViewById(R.id.btCreateDoings);
        imageView16 = contarctPopView.findViewById(R.id.imageView16);
        timeButton2 = contarctPopView.findViewById(R.id.timeButton2);
        imageView17 = contarctPopView.findViewById(R.id.imageView17);
        tvEndDate = contarctPopView.findViewById(R.id.tvEndDate);
        tvStartDate = contarctPopView.findViewById(R.id.tvStartDate);
        imageView9 = contarctPopView.findViewById(R.id.imageView9);
        imageView14 = contarctPopView.findViewById(R.id.imageView14);

        tvPageTitle = contarctPopView.findViewById(R.id.tvPageTitle);
        btBackPage = contarctPopView.findViewById(R.id.btBackPage);


        btBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        tvPageTitle.setText("Create Activity");




        ciDoingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_PROFILE_IMAGE);
            }
        });

        timeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker();
            }
        });




        btCreateDoings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss");
                strDateOfCreateDoing = formatter.format(date);


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
        builder2.setTitleText(R.string.Select_a_date);

        MaterialDatePicker materialDatePicker2 = builder2.build();

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(R.string.Select_a_date);

        MaterialDatePicker materialDatePicker = builder.build();

        DateButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                    materialDatePicker2.show(getSupportFragmentManager(),"DATE_PICKER");





            }
        });

        DateButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");


            }
        });



        materialDatePicker2.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((Long) selection);
                SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                String formattedDate  = format.format(calendar.getTime());



                Calendar currentDate = Calendar.getInstance();
                currentDate.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat format2 = new SimpleDateFormat("dd MMMM yyyy");
                String formattedDate2  = format2.format(currentDate.getTime());

                if(calendar.before(currentDate)){

                    StyleableToast.makeText(ActivityDoingsMainReal.this, getString(R.string.DateBefore), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }else {
                    DateButton1.setText(formattedDate);
                }




            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis((Long) selection);
                SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                String formattedDate  = format.format(calendar.getTime());

                DateButton2.setText(formattedDate);
            }
        });

        radioButtonOneDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvStartDate.setText("Choose an activity day");
                /*
                SelectModeDate = "OneDay";

                tvStartDate.setVisibility(View.VISIBLE);
                imageView9.setVisibility(View.VISIBLE);
                imageView14.setVisibility(View.VISIBLE);
                timeButton1.setVisibility(View.VISIBLE);
                DateButton1.setVisibility(View.VISIBLE);

                 */



            }
        });

        radioButtonManyDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                SelectModeDate = "ManyDay";
                tvStartDate.setVisibility(View.VISIBLE);
                imageView9.setVisibility(View.VISIBLE);
                imageView14.setVisibility(View.VISIBLE);
                timeButton1.setVisibility(View.VISIBLE);
                tvEndDate.setVisibility(View.VISIBLE);
                imageView16.setVisibility(View.VISIBLE);
                DateButton2.setVisibility(View.VISIBLE);
                imageView17.setVisibility(View.VISIBLE);
                timeButton2.setVisibility(View.VISIBLE);
                DateButton1.setVisibility(View.VISIBLE);

                 */

            }
        });




        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }

    private void popTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton1.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }



    private void CreateDoings() {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        String DoingsName = etDoingsName.getText().toString() ;
        String DoingsDescription  = etDoingsDescription.getText().toString();
        String Time = timeButton1.getText().toString();
        String Date = DateButton1.getText().toString();
        String Date2 = DateButton2.getText().toString();

        if(DoingsName.isEmpty()){
            etDoingsName.setError(getText(R.string.Please_enter_Name));
        }else if(DoingsDescription.isEmpty()){
            etDoingsDescription.setError(getText(R.string.Please_enter_Description));
        }else if(profileImageUri == null){
            StyleableToast.makeText(ActivityDoingsMainReal.this, (String) getText(R.string.Please_enter_Icon), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
        }else if(Time == getText(R.string.select_time)){
            StyleableToast.makeText(ActivityDoingsMainReal.this, (String) getText(R.string.Please_enter_Time), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
        }else if(Date == getText(R.string.select_date)){
            StyleableToast.makeText(ActivityDoingsMainReal.this, (String) getText(R.string.Please_enter_Date), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
        }

        else {

/*
        if(SelectModeDate.isEmpty()){
            Toast.makeText(this, "Chose Time And Date Activity", Toast.LENGTH_SHORT).show();
        }

 */


            mProfileDoings.child(groupToken + strDate).putFile(profileImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        mProfileDoings.child(groupToken + strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                HashMap hashMap = new HashMap();
                                hashMap.put("DoingsName", DoingsName);
                                hashMap.put("DoingsDescription", DoingsDescription);
                                hashMap.put("DoingsDate", Date);
                                hashMap.put("DoingsTime", Time);
                                hashMap.put("Doing_Key", mUser.getUid() + strDateOfCreateDoing);
                                hashMap.put("DoingsImage", uri.toString());



/*
                            if(SelectModeDate.equals("OneDay")){

 */
                                hashMap.put("ActivityStatus", "OneDay");
                                mGroupRef.child(groupToken).child("Activity").child(mUser.getUid() + strDateOfCreateDoing).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {


                                        AddPeopleForActivity();
                                        Toast.makeText(ActivityDoingsMainReal.this, "Create Activity Success", Toast.LENGTH_SHORT).show();

                                        StageMethold1 = true;

                                        CheckClosDialog();

                                        String timeString = Date + " " + Time;

                                        try {
                                            setNotificationAlarm(timeString, mUser.getUid() + strDateOfCreateDoing); // this is the context object
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ActivityDoingsMainReal.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                                /*
                            }else {

                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                                    Date Date01 = sdf.parse(Date);
                                    Date Date02 = sdf.parse(Date2);

                                    if(Date01.getTime() > Date02.getTime()){
                                        Toast.makeText(ActivityDoingsMainReal.this, "Time Start More Than Time Ends", Toast.LENGTH_SHORT).show();
                                    }else {
                                        hashMap.put("DoingsDateEND",Date2);

                                        mGroupRef.child(groupToken).child("Activity").child(mUser.getUid()+strDateOfCreateDoing).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {

                                                AddPeopleForActivity();

                                                Toast.makeText(ActivityDoingsMainReal.this, "Create Activity Success", Toast.LENGTH_SHORT).show();



                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ActivityDoingsMainReal.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            */


                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivityDoingsMainReal.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }



    }

    private void setNotificationAlarm(String timeString, String s) throws ParseException {


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date = dateFormat.parse(timeString);

        long timeInMillis = date.getTime();


        Intent intent = new Intent(getApplicationContext(), NotificationSpDate.class);
        intent.putExtra("GroupToken",groupToken);
        intent.putExtra("DoingToken",s);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

    }

    private void CheckClosDialog() {
       if( StageMethold1 == true &&  StageMethold2 == true ){
           dialog.dismiss();
       }
    }


    private void UploadImage() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy H:mm:ss:SSS");
        String strDate = formatter.format(date);

        for (upload_count = 0 ; upload_count<imageList.size() ; upload_count++){

            Uri IndividualImage = imageList.get(upload_count);
            final StorageReference ImageName = mDoingFolder.child("Image"+IndividualImage.getLastPathSegment());

            ImageName.child(groupToken+strDate).putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ImageName.child(groupToken+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                    Toast.makeText(ActivityDoingsMainReal.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }

    }

    private void Storelink(String url) {

        HashMap hashMap = new HashMap();
        hashMap.put("ImageLink",url);
        hashMap.put("User_Id",mUser.getUid());

        if(strDateOfCreateDoing == (null)){
            Toast.makeText(this, "strDateOfCreateDoing IS NULL", Toast.LENGTH_SHORT).show();

        }else {
            mGroupRef.child(groupToken).child("Activity").child(mUser.getUid()+strDateOfCreateDoing).child("ImageActivity").push().updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(ActivityDoingsMainReal.this, "Add Image Success", Toast.LENGTH_SHORT).show();

                    StageMethold3 = true;
                    CheckClosDialog();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ActivityDoingsMainReal.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

    private void AddPeopleForActivity() {
                    HashMap hashMap = new HashMap();
                    hashMap.put("Doing_position","Doing_Header");
                    hashMap.put("Notification",true);


                    mGroupRef.child(groupToken).child("Activity").child(mUser.getUid()+strDateOfCreateDoing).child("PeopleOfActivity").child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            StageMethold2 = true;
                            CheckClosDialog();
                            Toast.makeText(ActivityDoingsMainReal.this, "Update People Success", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ActivityDoingsMainReal.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


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
                        Toast.makeText(ActivityDoingsMainReal.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }

        if(requestCode == REQUEST_CODE_PROFILE_IMAGE && resultCode == RESULT_OK && data != null){
            profileImageUri=data.getData();
            ciDoingProfile.setImageURI(profileImageUri);

        }
    }


    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}