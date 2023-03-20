package com.example.a5dayappchat2.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesDoingToken;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesGroup;
import com.example.a5dayappchat2.Activity.Utills.Plan;
import com.example.a5dayappchat2.Activity.Utills.Sub_Plan;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.ActivityPlanBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlanActivity extends AppCompatActivity {
    ActivityPlanBinding binding;

    FloatingActionButton btToolActivity;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    EditText etNamePlan,etPlanDescription;
    Button btDate,btTime,btCancel,btAddPlan,btAddSubPlan,btEditPlan,btAddPeople;
    ImageView imageView6;
    TextView textView7;


    private DatePickerDialog datePickerDialog;

    PreferencesDoingToken preferencesDoingToken;
    PreferencesGroup preferencesGroup;

    String DoingToken;
    String GroupToken;

    int hour, minute;



    CircleImageView ciDoings;
    TextView tvNameDoings;
    DatabaseReference mGroupRef;

    FirebaseRecyclerOptions<Plan> options;
    FirebaseRecyclerAdapter<Plan,ViewHolderActivity> adapter;

    FirebaseRecyclerOptions<Sub_Plan> optionssubplan;
    FirebaseRecyclerAdapter<Sub_Plan,ViewHolderActivity> adaptersubplan;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        preferencesDoingToken = new PreferencesDoingToken(getApplicationContext());
        preferencesGroup = new PreferencesGroup(getApplicationContext());

        DoingToken = preferencesDoingToken.getString(Constant.DOINGS_TOKEN);
        GroupToken = preferencesGroup.getString(Constant.GROUP_TOKEN);

        ciDoings = findViewById(R.id.ciDoings);
        tvNameDoings = findViewById(R.id.tvNameDoings);

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");

        binding.recycleViewGroup.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        btToolActivity = findViewById(R.id.btToolActivity);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

        btToolActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopUpPlan();
            }
        });
        LoadActivity();

        LoadPlan();



    }

    private void LoadPlan() {

        Query query = mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").orderByChild("PlanDate");
        options = new FirebaseRecyclerOptions.Builder<Plan>().setQuery(query, Plan.class).build();
        adapter = new FirebaseRecyclerAdapter<Plan, ViewHolderActivity>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderActivity holder, int position, @NonNull Plan model) {

                    getRef(position);

                    holder.tvNameDoingsList.setText(model.getPlanName());
                    holder.tvTimeDoings.setText(model.getPlanDate());
                    holder.tvPlanDescription.setText(model.getPlanDescription());

                    String PlanKey = getRef(position).getKey().toString();

                mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(PlanKey).child("PlanStatus").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            String Status = snapshot.getValue().toString();

                            if(Status.equals("Finish")){
                                holder.rbFNPlan.setChecked(true);
                                holder.ivRight.setVisibility(View.VISIBLE);
                            }else if(Status.equals("UnFinish")){
                                holder.rbFNPlan.setChecked(false);
                                holder.ivRight.setVisibility(View.INVISIBLE);
                            }else {
                                Toast.makeText(PlanActivity.this, "NOT Finish AND UnFinishs"+Status, Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(PlanActivity.this, ""+Constant.ERROR_TEXT_NOT_EXISTS, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PlanActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




                mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(PlanKey).child("PlanStatus").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String Status = snapshot.getValue().toString() ;

                            holder.rbFNPlan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(Status.equals("Finish")){
                                        holder.rbFNPlan.setChecked(false);

                                        HashMap hashMap = new HashMap();
                                        hashMap.put("PlanStatus","UnFinish");

                                        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(PlanKey).updateChildren(hashMap);


                                    }else if(Status.equals("UnFinish")){
                                        holder.rbFNPlan.setChecked(true);
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("PlanStatus","Finish");

                                        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(PlanKey).updateChildren(hashMap);

                                    }else {
                                        Toast.makeText(PlanActivity.this, "NOT A AND B"+Status, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        }else {
                            Toast.makeText(PlanActivity.this, ""+Constant.ERROR_TEXT_NOT_EXISTS, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PlanActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OpenPopUpPlanDetail(PlanKey);
                    }
                });


                holder.ivSettingActivtiy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String PlanToken = getRef(position).getKey().toString();
                        ShowPopUpPlanSetting(PlanToken);
                    }
                });
            }


            @NonNull
            @Override
            public ViewHolderActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_activity, parent, false);
                return new ViewHolderActivity(view);
            }
        };
        adapter.startListening();
        binding.recycleViewGroup.setAdapter(adapter);




    }




    TextView tvNamePlan,tvDescriptionPlan;
    ImageButton btHideSubTask,btHidePeople;
    RecyclerView recyclerViewSubTask,recyclerViewPeople;

    private void OpenPopUpPlanDetail(String PlanKey) {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_plan_detail,null);

        tvNamePlan = contarctPopView.findViewById(R.id.tvNamePlan);
        tvDescriptionPlan = contarctPopView.findViewById(R.id.tvDescriptionPlan);
        btHideSubTask = contarctPopView.findViewById(R.id.btHideSubTask);
        btHidePeople = contarctPopView.findViewById(R.id.btHidePeople);
        recyclerViewSubTask = contarctPopView.findViewById(R.id.recyclerViewSubTask);
        recyclerViewPeople = contarctPopView.findViewById(R.id.recyclerViewPeople);

        recyclerViewSubTask.setLayoutManager(new LinearLayoutManager(PlanActivity.this));
        recyclerViewPeople.setLayoutManager(new LinearLayoutManager(PlanActivity.this));

        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(PlanKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String GroupName = snapshot.child("PlanName").getValue().toString();
                    String GroupDescription = snapshot.child("PlanDescription").getValue().toString();

                    tvDescriptionPlan.setText(GroupDescription);
                    tvNamePlan.setText(GroupName);


                }else {
                    Toast.makeText(PlanActivity.this, ""+Constant.ERROR_TEXT_NOT_EXISTS, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PlanActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        btHideSubTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewSubTask.isShown()){
                    recyclerViewSubTask.setVisibility(View.GONE);
                }else {
                    recyclerViewSubTask.setVisibility(View.VISIBLE);
                }
            }
        });

        btHidePeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewPeople.isShown()){
                    recyclerViewPeople.setVisibility(View.GONE);
                }else {
                    recyclerViewPeople.setVisibility(View.VISIBLE);
                }
            }
        });

        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();

        LoadSubPan(PlanKey,recyclerViewSubTask);

    }

    private void LoadSubPan(String planKey, RecyclerView recyclerViewSubTask) {

        Query query = mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(planKey).child("Sub_Plan").orderByChild("Sub_PlanDate");
        optionssubplan = new FirebaseRecyclerOptions.Builder<Sub_Plan>().setQuery(query, Sub_Plan.class).build();
        adaptersubplan = new FirebaseRecyclerAdapter<Sub_Plan, ViewHolderActivity>(optionssubplan) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderActivity holder, int position, @NonNull Sub_Plan model) {
                String Sub_plan_Token = getRef(position).getKey().toString();

                mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(planKey).child("Sub_Plan").child(Sub_plan_Token).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            getRef(position);
                            holder.tvNameDoingsList.setText(model.getSub_PlanName());
                            holder.tvTimeDoings.setText(model.getSub_PlanDate());
                            holder.tvPlanDescription.setText(model.getSub_PlanDescription());
                            holder.ivSettingActivtiy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String PlanSubToken = getRef(position).getKey().toString();
                                    ShowPopUpSubPlan(PlanSubToken,planKey);
                                }
                            });

                            String Status = snapshot.child("SubPlanStatus").getValue().toString();

                            if(Status.equals("Finish")){
                                holder.rbFNPlan.setChecked(true);
                                holder.ivRight.setVisibility(View.VISIBLE);
                            }else if(Status.equals("UnFinish")){
                                holder.rbFNPlan.setChecked(false);
                                holder.ivRight.setVisibility(View.INVISIBLE);
                            }else {
                                Toast.makeText(PlanActivity.this, "NOT Finish AND UnFinishs", Toast.LENGTH_SHORT).show();
                            }

                            holder.rbFNPlan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if(Status.equals("Finish")){
                                        holder.rbFNPlan.setChecked(false);

                                        HashMap hashMap = new HashMap();
                                        hashMap.put("SubPlanStatus","UnFinish");

                                        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(planKey).child("Sub_Plan").child(Sub_plan_Token).updateChildren(hashMap);


                                    }else if(Status.equals("UnFinish")){
                                        holder.rbFNPlan.setChecked(true);
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("SubPlanStatus","Finish");

                                        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(planKey).child("Sub_Plan").child(Sub_plan_Token).updateChildren(hashMap);

                                    }else {
                                        Toast.makeText(PlanActivity.this, "NOT A AND B", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            

                        } else{
                            getRef(position);
                            holder.itemView.setVisibility(View.GONE);
                            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PlanActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                getRef(position);
                holder.tvNameDoingsList.setText(model.getSub_PlanName());
                holder.tvTimeDoings.setText(model.getSub_PlanDate());
                holder.tvPlanDescription.setText(model.getSub_PlanDescription());
            }

            @NonNull
            @Override
            public ViewHolderActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_activity, parent, false);
                return new ViewHolderActivity(view);
            }

        };
        adaptersubplan.startListening();
        recyclerViewSubTask.setAdapter(adaptersubplan);

    }


    CardView cardView7;

    private void OpenSubPlanDetail(String planKey, String sub_plan_token) {

        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_plan_detail,null);

        tvNamePlan = contarctPopView.findViewById(R.id.tvNamePlan);
        tvDescriptionPlan = contarctPopView.findViewById(R.id.tvDescriptionPlan);
        btHideSubTask = contarctPopView.findViewById(R.id.btHideSubTask);
        btHidePeople = contarctPopView.findViewById(R.id.btHidePeople);
        recyclerViewSubTask = contarctPopView.findViewById(R.id.recyclerViewSubTask);
        recyclerViewPeople = contarctPopView.findViewById(R.id.recyclerViewPeople);

        cardView7 = contarctPopView.findViewById(R.id.cardView7);
        textView7 = contarctPopView.findViewById(R.id.textView7);

        textView7.setVisibility(View.GONE);
        cardView7.setVisibility(View.GONE);
        btHideSubTask.setVisibility(View.GONE);

        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(planKey).child("Sub_Plan").child(sub_plan_token).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String SubPlanName = snapshot.child("Sub_PlanName").getValue().toString();
                String SubPlanDescription = snapshot.child("Sub_PlanDescription").getValue().toString();

                tvNamePlan.setText(SubPlanName);
                tvDescriptionPlan.setText(SubPlanDescription);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PlanActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();


    }

    private void ShowPopUpSubPlan( String planSubToken,String planKey) {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_seeting_plan,null);

        btAddSubPlan = contarctPopView.findViewById(R.id.btAddSubPlan);
        btAddSubPlan.setVisibility(View.GONE);
        imageView6 =contarctPopView.findViewById(R.id.imageView6);
        imageView6.setVisibility(View.GONE);
        btEditPlan = contarctPopView.findViewById(R.id.btEditPlan);
        btAddPeople = contarctPopView.findViewById(R.id.btAddPeople);

        btEditPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopUpEditSubPlan(planSubToken,planKey);

            }
        });

        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    private void ShowPopUpEditSubPlan(String planSubToken, String planKey) {
        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(planKey).child("Sub_Plan").child(planSubToken).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String SubPlanName = snapshot.child("Sub_PlanName").getValue().toString();
                String SubPlanDate = snapshot.child("Sub_PlanDate").getValue().toString();
                String SubPlanDescription = snapshot.child("Sub_PlanDescription").getValue().toString();
                String SubPlanTime = snapshot.child("Sub_PlanTime").getValue().toString();

                LoadPopUpEditSupPlan(SubPlanName,SubPlanDate,SubPlanDescription,SubPlanTime,planSubToken,planKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PlanActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void LoadPopUpEditSupPlan(String subPlanName, String subPlanDate, String subPlanDescription, String subPlanTime, String planSubToken, String planKey) {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_create_doings,null);



        etNamePlan = contarctPopView.findViewById(R.id.etNamePlan);
        etPlanDescription = contarctPopView.findViewById(R.id.etPlanDescription);
        btDate = contarctPopView.findViewById(R.id.btDate);
        btTime = contarctPopView.findViewById(R.id.btTime);
        btCancel = contarctPopView.findViewById(R.id.btCancel);
        btAddPlan = contarctPopView.findViewById(R.id.btAddPlan);

        etNamePlan.setText(subPlanName);
        etPlanDescription.setText(subPlanDescription);
        btDate.setText(subPlanDate);
        btTime.setText(subPlanTime);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSelectDate();
            }
        });

        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker();
            }
        });

        btAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String SubPlanName = etNamePlan.getText().toString();
                String SubPlanDescription = etPlanDescription.getText().toString();
                String SubPlanDate = btDate.getText().toString();
                String SubPlanTime = btTime.getText().toString();

                HashMap hashMap = new HashMap();
                hashMap.put("Sub_PlanName",SubPlanName);
                hashMap.put("Sub_PlanDescription",SubPlanDescription);
                hashMap.put("Sub_PlanDate",SubPlanDate);
                hashMap.put("Sub_PlanTime",SubPlanTime);

                mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(planKey).child("Sub_Plan").child(planSubToken).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(PlanActivity.this, "Edit Sub Plan Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PlanActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();


    }

    private void ShowPopUpPlanSetting(String planToken) {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_seeting_plan,null);

        btAddSubPlan = contarctPopView.findViewById(R.id.btAddSubPlan);
        btEditPlan = contarctPopView.findViewById(R.id.btEditPlan);
        btAddPeople = contarctPopView.findViewById(R.id.btAddPeople);

        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();

        btAddSubPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSubPlan(planToken);
            }
        });

        btEditPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ShowPopUp

                ShowPopUpEditPlan(planToken);
            }
        });

        btAddPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopUpAddPeople_Plan();

            }
        });


    }

    private void ShowPopUpAddPeople_Plan() {
        //

    }

    private void ShowPopUpEditPlan(String planToken) {
        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(planToken).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String PlanName = snapshot.child("PlanName").getValue().toString();
                String PlanDate = snapshot.child("PlanDate").getValue().toString();
                String PlanDescription = snapshot.child("PlanDescription").getValue().toString();
                String PlanTime = snapshot.child("PlanTime").getValue().toString();


                LoadPopUpEditPlan(PlanName,PlanDate,PlanDescription,PlanTime,planToken);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PlanActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }

    private void LoadPopUpEditPlan(String planName, String planDate, String planDescription, String planTime,String planToken) {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_create_doings,null);


        etNamePlan = contarctPopView.findViewById(R.id.etNamePlan);
        etPlanDescription = contarctPopView.findViewById(R.id.etPlanDescription);
        btDate = contarctPopView.findViewById(R.id.btDate);
        btTime = contarctPopView.findViewById(R.id.btTime);
        btCancel = contarctPopView.findViewById(R.id.btCancel);
        btAddPlan = contarctPopView.findViewById(R.id.btAddPlan);

        etNamePlan.setText(planName);
        etPlanDescription.setText(planDescription);
        btDate.setText(planDate);
        btTime.setText(planTime);

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSelectDate();
            }
        });

        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker();
            }
        });



        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PlanName = etNamePlan.getText().toString();
                String PlanDescription = etPlanDescription.getText().toString();
                String PlanDate = btDate.getText().toString();
                String PlanTime = btTime.getText().toString();

                HashMap hashMap = new HashMap();
                hashMap.put("PlanName",PlanName);
                hashMap.put("PlanDescription",PlanDescription);
                hashMap.put("PlanDate",PlanDate);
                hashMap.put("PlanTime",PlanTime);

                mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(planToken).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(PlanActivity.this, "Edit Plan Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PlanActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();


    }

    private void AddSubPlan(String planToken) {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_create_doings,null);

        etNamePlan = contarctPopView.findViewById(R.id.etNamePlan);
        etPlanDescription = contarctPopView.findViewById(R.id.etPlanDescription);
        btDate = contarctPopView.findViewById(R.id.btDate);
        btTime = contarctPopView.findViewById(R.id.btTime);
        btCancel = contarctPopView.findViewById(R.id.btCancel);
        btAddPlan = contarctPopView.findViewById(R.id.btAddPlan);


        btDate.setText(getTodaysDate());



        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSelectDate();
            }
        });

        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PlanName = etNamePlan.getText().toString();
                String PlanDescription = etPlanDescription.getText().toString();
                String PlanDate = btDate.getText().toString();
                String PlanTime = btTime.getText().toString();

                HashMap hashMap = new HashMap();
                hashMap.put("Sub_PlanName",PlanName);
                hashMap.put("Sub_PlanDescription",PlanDescription);
                hashMap.put("Sub_PlanDate",PlanDate);
                hashMap.put("Sub_PlanTime",PlanTime);
                hashMap.put("SubPlanStatus","UnFinish");



                mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").child(planToken).child("Sub_Plan").push().updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(PlanActivity.this, "Add Sub-Plan Success", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PlanActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();



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

    private void openPopUpPlan() {
        dialogBuilder = new AlertDialog.Builder(this);
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_create_doings,null);

        etNamePlan = contarctPopView.findViewById(R.id.etNamePlan);
        etPlanDescription = contarctPopView.findViewById(R.id.etPlanDescription);
        btDate = contarctPopView.findViewById(R.id.btDate);
        btTime = contarctPopView.findViewById(R.id.btTime);
        btCancel = contarctPopView.findViewById(R.id.btCancel);
        btAddPlan = contarctPopView.findViewById(R.id.btAddPlan);

        btDate.setText(getTodaysDate());

        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionSelectDate();
            }
        });

        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PlanName = etNamePlan.getText().toString();
                String PlanDescription = etPlanDescription.getText().toString();
                String PlanDate = btDate.getText().toString();
                String PlanTime = btTime.getText().toString();

                HashMap hashMap = new HashMap();
                hashMap.put("PlanName",PlanName);
                hashMap.put("PlanDescription",PlanDescription);
                hashMap.put("PlanDate",PlanDate);
                hashMap.put("PlanTime",PlanTime);
                hashMap.put("PlanStatus","UnFinish");

                mGroupRef.child(GroupToken).child("Activity").child(DoingToken).child("Plan").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        dialog.dismiss();
                        Toast.makeText(PlanActivity.this, "Add Plan Complete", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PlanActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
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
                btTime.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }


    private void ActionSelectDate() {
        datePickerDialog.show();

    }

    private void LoadActivity() {
        mGroupRef.child(GroupToken).child("Activity").child(DoingToken).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String profileActivity = snapshot.child("DoingsImage").getValue().toString();
                    String NameActivity = snapshot.child("DoingsName").getValue().toString();

                    Picasso.get().load(profileActivity).into(ciDoings);
                    tvNameDoings.setText(NameActivity);


                }else {
                    Toast.makeText(PlanActivity.this, "Load Data Fails", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PlanActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}