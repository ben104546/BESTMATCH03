package com.example.a5dayappchat2.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a5dayappchat2.Activity.Preferences.PreferencesDoingToken;
import com.example.a5dayappchat2.Activity.Preferences.PreferencesGroup;
import com.example.a5dayappchat2.Activity.Utills.Group;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentFNActivityBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FNActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FNActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FNActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FNActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static FNActivity newInstance(String param1, String param2) {
        FNActivity fragment = new FNActivity();
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
    private FragmentFNActivityBinding binding;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mGroupRef;

    FirebaseRecyclerOptions<Group> Groupoptions;
    FirebaseRecyclerAdapter<Group,FindGroupViewHolder> Groupadapter;

    FirebaseRecyclerOptions<CalendarList> optionsActivity;
    FirebaseRecyclerAdapter<CalendarList,CalendarListViewHolder> adapterActivity;

    PreferencesGroup preferencesGroup;
    PreferencesDoingToken preferencesDoingToken;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentFNActivityBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        binding.recycleViewGroup.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewActivity.setLayoutManager(new LinearLayoutManager(getActivity()));



        preferencesGroup = new PreferencesGroup(getActivity().getApplicationContext());
        preferencesDoingToken = new PreferencesDoingToken(getActivity().getApplicationContext());

        loadGroupToken("");

         return binding.getRoot();

    }

    private void loadGroupToken(String s) {
        Query query = mGroupRef.orderByChild("GroupName").startAt(s).endAt(s + "\uf8ff");
        Groupoptions = new FirebaseRecyclerOptions.Builder<Group>().setQuery(query, Group.class).build();
        Groupadapter = new FirebaseRecyclerAdapter<Group, FindGroupViewHolder>(Groupoptions) {
            @Override
            protected void onBindViewHolder(@NonNull FindGroupViewHolder holder, int position, @NonNull Group model) {

                String groupKey = getRef(position).getKey();

                getRef(position);
                Toast.makeText(getActivity(), "Load Group Success", Toast.LENGTH_SHORT).show();
                Picasso.get().load(model.getGroupProfileImage()).into(holder.circleImageProfileGroup);
                holder.textGroupName.setText(model.getGroupName());


                LoadActivityToken(groupKey);

                binding.recycleViewGroup.setVisibility(View.GONE);

            }

            @NonNull
            @Override
            public FindGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_group_cell, parent, false);
                return new FindGroupViewHolder(view);
            }
        };
        Groupadapter.startListening();
        binding.recycleViewGroup.setAdapter(Groupadapter);
    }

    private void LoadActivityToken(String groupKey) {
        mGroupRef.child(groupKey).child("Activity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Query query = mGroupRef.child(groupKey).child("Activity").orderByChild("DoingsTime");
                    optionsActivity = new FirebaseRecyclerOptions.Builder<CalendarList>().setQuery(query, CalendarList.class).build();
                    adapterActivity = new FirebaseRecyclerAdapter<CalendarList, CalendarListViewHolder>(optionsActivity) {
                        @Override
                        protected void onBindViewHolder(@NonNull CalendarListViewHolder holder, int position, @NonNull CalendarList model) {
                            String activityKey = getRef(position).getKey().toString();

                            DataSnapshot Member = snapshot.child(activityKey).child("PeopleOfActivity").child(mUser.getUid());

                            if(Member.exists()){


                                String DateActivity = snapshot.child(activityKey).child("DoingsDate").getValue().toString();
                                String NewDateActivity = DateActivity.replace(" ","-");

                                String DoingsTime = snapshot.child(activityKey).child("DoingsTime").getValue().toString();
                                String Date = NewDateActivity+" "+DoingsTime;

                                String currentDate = new SimpleDateFormat("dd-MMMM-yyyy HH:mm", Locale.getDefault()).format(new Date());

                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");

                                String Status = "Noting";

                                try {

                                    Date date = sdf.parse(Date);
                                    Date date2 = sdf.parse(currentDate);

                                    Date date3 ;
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(date);
                                    c.add(Calendar.DATE, 1);
                                    date3 = c.getTime();

                                    long currentTime = date2.getTime();
                                    long newYearDate = date.getTime();
                                    long newYearDateAddOne = date3.getTime();

                                    long countDownToNewYear = newYearDate - currentTime;
                                    long countDownToNewYearAddOne = newYearDateAddOne - currentTime;

                                    holder.myCountdown.start(countDownToNewYear);
                                    holder.myCountdownHours.setVisibility(View.GONE);


                                    if(countDownToNewYear <= 0){
                                        Status = "Activity Over";

                                        if(countDownToNewYearAddOne <= 0){
                                            Status = "Activity Over";

                                        }else {

                                            Status = "Activity Started";
                                            holder.myCountdown.setVisibility(View.GONE);
                                            holder.myCountdownHours.setVisibility(View.VISIBLE);
                                            holder.myCountdownHours.start(countDownToNewYearAddOne);

                                        }
                                    }else {
                                        Status = "Activity Not Over";
                                        holder.myCountdown.setVisibility(View.VISIBLE);
                                        holder.myCountdownHours.setVisibility(View.GONE);

                                    }



                                    Toast.makeText(getActivity(), ""+Status, Toast.LENGTH_SHORT).show();

                                }catch (ParseException e){
                                    e.printStackTrace();
                                }

                                if(Status.equals("Activity Not Over")){
                                    getRef(position);
                                    holder.itemView.setVisibility(View.GONE);
                                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                                }else if(Status.equals("Activity Started")) {
                                    getRef(position);
                                    holder.itemView.setVisibility(View.GONE);
                                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

                                }else {
                                    getRef(position);
                                    Picasso.get().load(model.getDoingsImage()).into(holder.firstUserProfileImage);
                                    holder.tvNameDoingsList.setText(model.getDoingsName());
                                    holder.tvTimeDoings.setText(model.getDoingsDate());
                                }



                            }else {
                                getRef(position);
                                holder.itemView.setVisibility(View.GONE);
                                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                            }


                        }

                        @NonNull
                        @Override
                        public CalendarListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_doings_chat_cell_uncomplete, parent, false);
                            return new CalendarListViewHolder(view);
                        }
                    };
                    adapterActivity.startListening();
                    binding.recyclerViewActivity.setAdapter(adapterActivity);

                }else {
                    Toast.makeText(getActivity(), ""+ Constant.ERROR_TEXT_NOT_EXISTS, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}