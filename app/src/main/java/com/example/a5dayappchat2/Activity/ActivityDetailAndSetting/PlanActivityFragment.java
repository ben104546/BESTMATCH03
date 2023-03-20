package com.example.a5dayappchat2.Activity.ActivityDetailAndSetting;

import static com.example.a5dayappchat2.R.drawable.bg_button_acitivity_disable;
import static com.example.a5dayappchat2.R.drawable.bg_button_color_disable;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.ActivityDeailAndSetting;
import com.example.a5dayappchat2.Activity.SetupActivity;
import com.example.a5dayappchat2.Activity.Utills.Plan;
import com.example.a5dayappchat2.Activity.Utills.Sub_Plan;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentPlanActivityBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import io.github.muddz.styleabletoast.StyleableToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanActivityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String groupToken;
    private String doingToken;

    public PlanActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanActivityFragment newInstance(String param1, String param2) {
        PlanActivityFragment fragment = new PlanActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private FragmentPlanActivityBinding binding;
    DatabaseReference mGroupRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;


    int hour, minute;

    private DatePickerDialog datePickerDialog;

    String positionplane;

    Boolean AllTaskIsNotDone ;

    String PlanKey;

    String Sub_plan_Token;


    String GroupToken;
    String DoingToken;


    boolean PUTBUTTONCHSETTING;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPlanActivityBinding.inflate(inflater, container, false);


        ActivityDeailAndSetting activityDeailAndSetting = (ActivityDeailAndSetting) getActivity();

        GroupToken= activityDeailAndSetting.getDataGroupToken();
        DoingToken= activityDeailAndSetting.getDataDoingToken();


        // Inflate the layout for this fragment

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        ShowPlan(GroupToken,DoingToken);


        binding.include.tvDetailActivity.setText("Plan Activity");

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

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);

        binding.fbtCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mGroupRef.child(groupToken).child("Activity").child(doingToken).child("PeopleOfActivity").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){
                            String psiton = snapshot.child("Doing_position").getValue().toString();

                            if(psiton.equals("Doing_Header")){
                                CreatePlan(GroupToken,DoingToken);
                            }else {
                                StyleableToast.makeText(getActivity(), getString(R.string.Youcannotfun), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                            }
                        }else {
                            StyleableToast.makeText(getActivity(), getString(R.string.Youcannotfun), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(activityDeailAndSetting, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });


        return binding.getRoot();

    }




    private void CreatePlan(String groupToken, String doingToken) {

        dialogBuilder = new AlertDialog.Builder(getActivity());
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_create_doings,null);

        etNamePlan = contarctPopView.findViewById(R.id.etNamePlan);
        etPlanDescription = contarctPopView.findViewById(R.id.etPlanDescription);
        btDate = contarctPopView.findViewById(R.id.btDate);
        btTime = contarctPopView.findViewById(R.id.btTime);
        btCancel = contarctPopView.findViewById(R.id.btCancel);
        btAddPlan = contarctPopView.findViewById(R.id.btAddPlan);

        mGroupRef.child(groupToken).child("Activity").child(doingToken).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("ActivityStatus").getValue().toString().equals("OneDay")){
                        btDate.setText(snapshot.child("DoingsDate").getValue().toString());
                        btDate.setBackgroundColor(btDate.getContext().getResources().getColor(R.color.BGBUTTOn));
                        btDate.setClickable(false);

                    }else {
                        btDate.setClickable(true);
                    }
                }else {
                    Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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


                if(PlanName.isEmpty()){
                    StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_name), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }else if(PlanDescription.isEmpty()){
                    StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_des), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }else  if (PlanDate == getString(R.string.add_date)){
                    StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_date), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }else if(PlanTime == getString(R.string.add_time)){
                    StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_time), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }else {

                    HashMap hashMap = new HashMap();
                    hashMap.put("PlanName",PlanName);
                    hashMap.put("PlanDescription",PlanDescription);
                    hashMap.put("PlanDate",PlanDate);
                    hashMap.put("PlanTime",PlanTime);
                    hashMap.put("PlanStatus","UnFinish");

                    mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Add Plan Complete", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }


    FirebaseRecyclerOptions<Plan> options;
    FirebaseRecyclerAdapter<Plan,ViewHolderActivityNew> adapter;





    ImageView btDeletePlan,btCancelDelete;

    private void ShowPopupDeletePlan(String groupToken, String doingToken) {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_delete_plan,null);

        btDeletePlan = contarctPopView.findViewById(R.id.btDeletePlan);
        btCancelDelete = contarctPopView.findViewById(R.id.btCancelDelete);


        btDeletePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(PlanKey).removeValue();
                ShowPlan(groupToken,doingToken);
                dialog.dismiss();

            }
        });

        btCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });



        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();
    }


    private void EditMainPlan(String groupToken, String doingToken, String planToken) {
        mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(planToken).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String PlanName = snapshot.child("PlanName").getValue().toString();
                    String PlanDate = snapshot.child("PlanDate").getValue().toString();
                    String PlanDescription = snapshot.child("PlanDescription").getValue().toString();
                    String PlanTime = snapshot.child("PlanTime").getValue().toString();

                    dialogBuilder = new AlertDialog.Builder(getActivity());
                    final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_create_doings,null);


                    etNamePlan = contarctPopView.findViewById(R.id.etNamePlan);
                    etPlanDescription = contarctPopView.findViewById(R.id.etPlanDescription);
                    btDate = contarctPopView.findViewById(R.id.btDate);
                    btTime = contarctPopView.findViewById(R.id.btTime);
                    btCancel = contarctPopView.findViewById(R.id.btCancel);
                    btAddPlan = contarctPopView.findViewById(R.id.btAddPlan);
                    btAddPlan.setText("Edit Plan");

                    etNamePlan.setText(PlanName);
                    etPlanDescription.setText(PlanDescription);
                    btDate.setText(PlanDate);
                    btTime.setText(PlanTime);

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


                            if(PlanName.isEmpty()){
                                StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_name), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                            }else if(PlanDescription.isEmpty()){
                                StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_des), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                            }else  if (PlanDate == getString(R.string.add_date)){
                                StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_date), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                            }else if(PlanTime == getString(R.string.add_time)){
                                StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_time), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                            }else {
                                HashMap hashMap = new HashMap();
                                hashMap.put("PlanName",PlanName);
                                hashMap.put("PlanDescription",PlanDescription);
                                hashMap.put("PlanDate",PlanDate);
                                hashMap.put("PlanTime",PlanTime);

                                mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(planToken).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(getActivity(), "Edit Plan Success", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });

                    dialogBuilder.setView(contarctPopView);
                    dialog = dialogBuilder.create();
                    dialog.show();
                }else {
//
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }



    EditText etNamePlan,etPlanDescription;
    Button btDate,btTime,btCancel,btAddPlan,btAddSubPlan,btEditPlan,btAddPeople;

    private void AddSubPlan(String planToken, String groupToken, String doingToken) {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_create_doings,null);

        etNamePlan = contarctPopView.findViewById(R.id.etNamePlan);
        etPlanDescription = contarctPopView.findViewById(R.id.etPlanDescription);
        btDate = contarctPopView.findViewById(R.id.btDate);
        btTime = contarctPopView.findViewById(R.id.btTime);
        btCancel = contarctPopView.findViewById(R.id.btCancel);
        btAddPlan = contarctPopView.findViewById(R.id.btAddPlan);
        btAddPlan.setText("ADD SUB-TASK");


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


                if(PlanName.isEmpty()){
                    StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_name), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }else if(PlanDescription.isEmpty()){
                    StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_des), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }else  if (PlanDate == getString(R.string.add_date)){
                    StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_date), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }else if(PlanTime == getString(R.string.add_time)){
                    StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_time), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }else {
                    HashMap hashMap = new HashMap();
                    hashMap.put("Sub_PlanName",PlanName);
                    hashMap.put("Sub_PlanDescription",PlanDescription);
                    hashMap.put("Sub_PlanDate",PlanDate);
                    hashMap.put("Sub_PlanTime",PlanTime);
                    hashMap.put("SubPlanStatus","UnFinish");


                    mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(planToken).child("Sub_Plan").push().updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            ShowPlan(GroupToken,DoingToken);
                            Toast.makeText(getActivity(), "Add Sub-Plan Success", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }



            }

        });


        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();
    }


    FirebaseRecyclerOptions<Sub_Plan> optionssubplan;
    FirebaseRecyclerAdapter<Sub_Plan,ViewHolderActivityNew> adaptersubplan;


    private void LoadSubPlan(String groupToken, String doingToken, String planToken) {

        try {
            ViewHolderActivityNew.recyclerViewSubTask.setLayoutManager(new LinearLayoutManager(getActivity()));
        }catch (Exception e){
            return;
        }

        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.cardView9);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMarginStart(50);
        ViewHolderActivityNew.recyclerViewSubTask.setLayoutParams(params);

        Query query = mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(planToken).child("Sub_Plan").orderByChild("Sub_PlanDate");
        optionssubplan = new FirebaseRecyclerOptions.Builder<Sub_Plan>().setQuery(query, Sub_Plan.class).build();
        adaptersubplan = new FirebaseRecyclerAdapter<Sub_Plan, ViewHolderActivityNew>(optionssubplan) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderActivityNew holder, int position, @NonNull Sub_Plan model) {
                Sub_plan_Token = getRef(position).getKey().toString();

                mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(planToken).child("Sub_Plan").child(Sub_plan_Token).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            getRef(position);
                            holder.tvPlanName.setText(model.getSub_PlanName());
                            holder.tvPlanDate.setText(model.getSub_PlanDate());

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //
                                }
                            });

                            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {

                                    Sub_plan_Token = getRef(position).getKey().toString();
                                    return false;
                                }
                            });


                            holder.tbPlanAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    holder.cardView10.setVisibility(View.VISIBLE);
                                }
                            });

                            holder.tbClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    holder.cardView10.setVisibility(View.GONE);
                                }
                            });

                            holder.btEditPlan.setText("EDIT SUB-TASK");

                            holder.btEditPlan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    EDITSUBPLAN(groupToken, doingToken, planToken, Sub_plan_Token);

                                }
                            });

                            holder.btAddPeople.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//
                                }
                            });

                            holder.btDeleteSubPlan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ShowPopupDeleteSubPlan(groupToken, doingToken, planToken, Sub_plan_Token);
                                }
                            });

                            String StatusSUB = snapshot.child("SubPlanStatus").getValue().toString();


                            if (StatusSUB.equals("Finish")) {
                                holder.rbPlanCheck.setChecked(true);

                            } else if (StatusSUB.equals("UnFinish")) {
                                holder.rbPlanCheck.setChecked(false);

                            } else {
                                Toast.makeText(getActivity(), "NOT Finish AND UnFinishs", Toast.LENGTH_SHORT).show();
                            }


                            while (StatusSUB.equals("UnFinish")) {
                                AllTaskIsNotDone = true;
                                break;
                            }

                            while (!StatusSUB.equals("UnFinish")) {
                                AllTaskIsNotDone = false;
                                break;
                            }

                            Toast.makeText(getActivity(), AllTaskIsNotDone.toString(), Toast.LENGTH_SHORT).show();


                            holder.rbPlanCheck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (StatusSUB.equals("Finish")) {
                                        holder.rbPlanCheck.setChecked(false);

                                        HashMap hashMap = new HashMap();
                                        hashMap.put("SubPlanStatus", "UnFinish");

                                        mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(planToken).child("Sub_Plan").child(Sub_plan_Token).updateChildren(hashMap);


                                    } else if (StatusSUB.equals("UnFinish")) {
                                        holder.rbPlanCheck.setChecked(true);
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("SubPlanStatus", "Finish");

                                        mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(planToken).child("Sub_Plan").child(Sub_plan_Token).updateChildren(hashMap);

                                    } else {
                                        Toast.makeText(getActivity(), "NOT A AND B", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        } else {
                            getRef(position);
                            holder.itemView.setVisibility(View.GONE);
                            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                getRef(position);
                holder.tvPlanName.setText(model.getSub_PlanName());
                holder.tvPlanDate.setText(model.getSub_PlanDate());


            }

            @NonNull
            @Override
            public ViewHolderActivityNew onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_sub_plan_activity, parent, false);
                return new ViewHolderActivityNew(view);
            }
        };


        adaptersubplan.startListening();
        ViewHolderActivityNew.recyclerViewSubTask.setAdapter(adaptersubplan);


    }


    private void ShowPopupDeleteSubPlan(String groupToken, String doingToken, String planToken, String sub_plan_Token) {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_delete_plan,null);

        btDeletePlan = contarctPopView.findViewById(R.id.btDeletePlan);
        btCancelDelete = contarctPopView.findViewById(R.id.btCancelDelete);


        btDeletePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(planToken).child("Sub_Plan").child(Sub_plan_Token).removeValue();
                ShowPlan(groupToken,doingToken);
                dialog.dismiss();
            }
        });

        btCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        dialog.dismiss();
            }
        });

        dialogBuilder.setView(contarctPopView);
        dialog = dialogBuilder.create();
        dialog.show();


    }



    private void EDITSUBPLAN(String groupToken, String doingToken, String planToken, String sub_plan_token) {
        mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(planToken).child("Sub_Plan").child(sub_plan_token).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String SubPlanName = snapshot.child("Sub_PlanName").getValue().toString();
                String SubPlanDate = snapshot.child("Sub_PlanDate").getValue().toString();
                String SubPlanDescription = snapshot.child("Sub_PlanDescription").getValue().toString();
                String SubPlanTime = snapshot.child("Sub_PlanTime").getValue().toString();



                dialogBuilder = new AlertDialog.Builder(getActivity());
                final  View contarctPopView = getLayoutInflater().inflate(R.layout.pop_up_create_doings,null);


                etNamePlan = contarctPopView.findViewById(R.id.etNamePlan);
                etPlanDescription = contarctPopView.findViewById(R.id.etPlanDescription);
                btDate = contarctPopView.findViewById(R.id.btDate);
                btTime = contarctPopView.findViewById(R.id.btTime);
                btCancel = contarctPopView.findViewById(R.id.btCancel);
                btAddPlan = contarctPopView.findViewById(R.id.btAddPlan);
                btAddPlan.setText("EDIT SUB-PLAN");

                etNamePlan.setText(SubPlanName);
                etPlanDescription.setText(SubPlanDescription);
                btDate.setText(SubPlanDate);
                btTime.setText(SubPlanTime);

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

                        if(SubPlanName.isEmpty()){
                            StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_name), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                        }else if(SubPlanDescription.isEmpty()){
                            StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_des), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                        }else  if (SubPlanDate == getString(R.string.add_date)){
                            StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_date), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                        }else if(SubPlanTime == getString(R.string.add_time)){
                            StyleableToast.makeText(getActivity(), getString(R.string.Please_enter_plan_time), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                        }else {
                            HashMap hashMap = new HashMap();
                            hashMap.put("Sub_PlanName",SubPlanName);
                            hashMap.put("Sub_PlanDescription",SubPlanDescription);
                            hashMap.put("Sub_PlanDate",SubPlanDate);
                            hashMap.put("Sub_PlanTime",SubPlanTime);

                            mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(planToken).child("Sub_Plan").child(sub_plan_token).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(getActivity(), "Edit Sub Plan Success", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });

                dialogBuilder.setView(contarctPopView);
                dialog = dialogBuilder.create();
                dialog.show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

        private void ActionSelectDate() {
            datePickerDialog.show();

        }


    private void ShowPlan(String groupToken, String doingToken) {
        this.groupToken = groupToken;
        this.doingToken = doingToken;


        Query query = mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").orderByChild("PlanDate");
        options = new FirebaseRecyclerOptions.Builder<Plan>().setQuery(query, Plan.class).build();
        adapter = new FirebaseRecyclerAdapter<Plan, ViewHolderActivityNew>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderActivityNew holder, int position, @NonNull Plan model) {
                getRef(position);

                String planToken = getRef(position).getKey().toString();

                holder.tvPlanName.setText(model.getPlanName());
                holder.tvPlanDate.setText(model.getPlanDate());

                PlanKey = getRef(position).getKey().toString();

                LoadSubPlan(groupToken,doingToken,PlanKey);

                positionplane = getRef(position).getKey().toString();

                mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(PlanKey).child("PlanStatus").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            String Status = snapshot.getValue().toString();



                            if(Status.equals("Finish")){
                                holder.rbPlanCheck.setChecked(true);

                            }else if(Status.equals("UnFinish")){
                                holder.rbPlanCheck.setChecked(false);
                            }else {
                                Toast.makeText(getActivity(), "NOT Finish AND UnFinishs"+Status, Toast.LENGTH_SHORT).show();
                            }



                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(PlanKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String Status = snapshot.child("PlanStatus").getValue().toString() ;

                            holder.rbPlanCheck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    mGroupRef.child(groupToken).child("Activity").child(doingToken).child("PeopleOfActivity").child(mUser.getUid()).child("Doing_position").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                            if(snapshot2.exists()){
                                                String Position = snapshot2.getValue().toString();

                                                if(Position.equals("Doing_Header")){
                                                    if (Status.equals("Finish")) {
                                                        holder.rbPlanCheck.setChecked(false);
                                                        HashMap hashMap = new HashMap();
                                                        hashMap.put("PlanStatus", "UnFinish");

                                                        mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(PlanKey).updateChildren(hashMap);

                                                    } else {

                                                        for (DataSnapshot childSnapshot2 : snapshot.child("Sub_Plan").getChildren()){
                                                            String key2 = childSnapshot2.getKey();
                                                            mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(PlanKey).child("Sub_Plan").child(key2).child("SubPlanStatus").addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists()){
                                                                        String subplanstatus = snapshot.getValue().toString();
                                                                        if(subplanstatus.equals("UnFinish")){
                                                                            holder.rbPlanCheck.setChecked(false);
                                                                            HashMap hashMap = new HashMap();
                                                                            hashMap.put("PlanStatus", "UnFinish");

                                                                            mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(PlanKey).updateChildren(hashMap);

                                                                            StyleableToast.makeText(getActivity(), "SubPlan still Unfinish ", Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                                                                            return;
                                                                        }else {

                                                                            return;
                                                                        }
                                                                    }

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {
                                                                    Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }

                                                        holder.rbPlanCheck.setChecked(true);
                                                        HashMap hashMap = new HashMap();
                                                        hashMap.put("PlanStatus", "Finish");


                                                        mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(PlanKey).updateChildren(hashMap);


                                                    }
                                                }else {
                                                    holder.rbPlanCheck.setChecked(false);
                                                    HashMap hashMap = new HashMap();
                                                    hashMap.put("PlanStatus", "UnFinish");

                                                    mGroupRef.child(groupToken).child("Activity").child(doingToken).child("Plan").child(PlanKey).updateChildren(hashMap);

                                                    StyleableToast.makeText(getActivity(), "You do not have the right to administer the Plan.", Toast.LENGTH_LONG, R.style.ErrorAppToast).show();

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            });



                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), ""+PlanKey, Toast.LENGTH_SHORT).show();

                    }
                });

                holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        PlanKey = getRef(position).getKey().toString();
                        return false;
                    }
                });



                holder.btAddSubPlan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddSubPlan(planToken,groupToken,doingToken);
                    }
                });

                holder.btEditPlan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditMainPlan(groupToken,doingToken,planToken);

                    }
                });

                holder.btAddPeople.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                holder.btDeletePlan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowPopupDeletePlan(groupToken,doingToken);
                    }
                });


                holder.tbPlanAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.layoutSettingPlan.setVisibility(View.VISIBLE);
                        PUTBUTTONCHSETTING = true;



                    }
                });



                holder.tbClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.layoutSettingPlan.setVisibility(View.GONE);
                        PUTBUTTONCHSETTING = false;

                    }
                });



            }


            @NonNull
            @Override
            public ViewHolderActivityNew onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_plan_activity, parent, false);
                return new ViewHolderActivityNew(view);

            }


        };

        adapter.startListening();
        binding.recyclerView.setAdapter(adapter);


    }


}