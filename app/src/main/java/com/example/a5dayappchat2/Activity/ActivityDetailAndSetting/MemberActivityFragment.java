package com.example.a5dayappchat2.Activity.ActivityDetailAndSetting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.a5dayappchat2.Activity.ActivityDeailAndSetting;
import com.example.a5dayappchat2.Activity.Utills.MemberOfActivity;
import com.example.a5dayappchat2.Activity.ViewHolderMemberActivity;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentMemberActivityBinding;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberActivityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MemberActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MemberActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemberActivityFragment newInstance(String param1, String param2) {
        MemberActivityFragment fragment = new MemberActivityFragment();
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

    private FragmentMemberActivityBinding binding;

    DatabaseReference mGroupRef,mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseRecyclerOptions<MemberOfActivity> MemberActivityOptions;

    FirebaseRecyclerAdapter<MemberOfActivity, ViewHolderMemberActivity> MemberActivityAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentMemberActivityBinding.inflate(inflater, container, false);

        mGroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        binding.recyclerViewMember.setLayoutManager(new LinearLayoutManager(getActivity()));

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        ActivityDeailAndSetting activityDeailAndSetting = (ActivityDeailAndSetting) getActivity();

        String GroupToken = activityDeailAndSetting.getDataGroupToken();
        String DoingToken = activityDeailAndSetting.getDataDoingToken();
        
        LoadMember(GroupToken,DoingToken);

        binding.include.tvDetailActivity.setText("Members Activity");

        return binding.getRoot();
    }

    private void LoadMember(String groupToken, String doingToken) {
        Query query = mGroupRef.child(groupToken).child("Activity").child(doingToken).child("PeopleOfActivity");
        MemberActivityOptions = new FirebaseRecyclerOptions.Builder<MemberOfActivity>().setQuery(query, MemberOfActivity.class).build();
        MemberActivityAdapter = new FirebaseRecyclerAdapter<MemberOfActivity, ViewHolderMemberActivity>(MemberActivityOptions) {


            @Override
            protected void onBindViewHolder(@NonNull ViewHolderMemberActivity holder, int position, @NonNull MemberOfActivity model) {
                String UserId = getRef(position).getKey().toString();

                mUserRef.child(UserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String UserName = snapshot.child("username").getValue().toString();
                        String profileImage = snapshot.child("profileImage").getValue().toString();

                        Picasso.get().load(profileImage).into(holder.civProfileActvityMember);
                        holder.tvNameActiviyMember.setText(UserName);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @NonNull
            @Override
            public ViewHolderMemberActivity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_member_activity, parent, false);
                return new ViewHolderMemberActivity(view);
            }
        };

        MemberActivityAdapter.startListening();
        binding.recyclerViewMember.setAdapter(MemberActivityAdapter);

    }
}