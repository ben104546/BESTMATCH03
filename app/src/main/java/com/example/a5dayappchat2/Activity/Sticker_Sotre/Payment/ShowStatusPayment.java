package com.example.a5dayappchat2.Activity.Sticker_Sotre.Payment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.a5dayappchat2.Activity.Sticker_Sotre.MenuStickerMain;
import com.example.a5dayappchat2.Activity.StikcerBasketListViewHolder;
import com.example.a5dayappchat2.Activity.Utills.StickerBasket;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentSendDataPayMentBinding;
import com.example.a5dayappchat2.databinding.FragmentShowStatusPaymentBinding;
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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowStatusPayment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowStatusPayment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowStatusPayment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowStatusPayment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowStatusPayment newInstance(String param1, String param2) {
        ShowStatusPayment fragment = new ShowStatusPayment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    FragmentShowStatusPaymentBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FirebaseRecyclerOptions<StickerBasket> optionsStickerBK;
    FirebaseRecyclerAdapter<StickerBasket, StikcerBasketListViewHolder> adapterStickerBK;


    DatabaseReference mOrder;
    String OrderKey;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    DatabaseReference mStickerStoreRef;

    ArrayList<Integer> lisIntPrice = new ArrayList<>() ;

    String sumText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowStatusPaymentBinding.inflate(inflater, container, false);


        mStickerStoreRef = FirebaseDatabase.getInstance().getReference().child("Sticker");

        OrderKey = getActivity().getIntent().getStringExtra("OrderKey");
        mOrder = FirebaseDatabase.getInstance().getReference().child("Order");


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        binding.btBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MenuStickerMain.class);
                startActivity(intent);
            }
        });

        binding.recyclerViewSS.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewWW.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewFF.setLayoutManager(new LinearLayoutManager(getActivity()));

        ShowViewStatus();
        
        LoadStickerViewSS();
        LoadStickerViewWW();
        LoadStickerViewFF();

        return binding.getRoot();
    }

    private void LoadStickerViewFF() {
        Query query = mOrder.child(mUser.getUid()).child(OrderKey).child("StickerId");
        optionsStickerBK = new FirebaseRecyclerOptions.Builder<StickerBasket>().setQuery(query,StickerBasket.class).build();
        adapterStickerBK = new FirebaseRecyclerAdapter<StickerBasket, StikcerBasketListViewHolder>(optionsStickerBK) {

            @Override
            protected void onBindViewHolder(@NonNull StikcerBasketListViewHolder holder, int position, @NonNull StickerBasket model) {

                mStickerStoreRef.child(model.getStickerId()).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){


                            String NameSticker = snapshot.child("NameSticker").getValue().toString();
                            String ImageIcon = snapshot.child("ImageIcon").getValue().toString();
                            String StickerPrice = snapshot.child("StickerPrice").getValue().toString();

                            /*
                            String Check = model.getStikcerStatusCheck().toString();
                            String Id = model.getStickerId().toString();
                             */

                            lisIntPrice.add(Integer.valueOf(StickerPrice));

                            Picasso.get().load(ImageIcon).into(holder.ciIvStikcerIcon);
                            holder.tvInfoSticker.setText(NameSticker);
                            holder.tvPrice.setText(StickerPrice+" ฿");
                            holder.rbSlectStikcer.setVisibility(View.GONE);

                            ShowSumPrice();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public StikcerBasketListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_basket_sticker,parent,false);

                return new StikcerBasketListViewHolder(view);
            }
        };

        adapterStickerBK.startListening();
        binding.recyclerViewFF.setAdapter(adapterStickerBK);
    }

    private void LoadStickerViewWW() {
        Query query = mOrder.child(mUser.getUid()).child(OrderKey).child("StickerId");
        optionsStickerBK = new FirebaseRecyclerOptions.Builder<StickerBasket>().setQuery(query,StickerBasket.class).build();
        adapterStickerBK = new FirebaseRecyclerAdapter<StickerBasket, StikcerBasketListViewHolder>(optionsStickerBK) {

            @Override
            protected void onBindViewHolder(@NonNull StikcerBasketListViewHolder holder, int position, @NonNull StickerBasket model) {

                mStickerStoreRef.child(model.getStickerId()).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(getActivity(), model.getStickerId(), Toast.LENGTH_SHORT).show();

                            String NameSticker = snapshot.child("NameSticker").getValue().toString();
                            String ImageIcon = snapshot.child("ImageIcon").getValue().toString();
                            String StickerPrice = snapshot.child("StickerPrice").getValue().toString();

                            /*
                            String Check = model.getStikcerStatusCheck().toString();
                            String Id = model.getStickerId().toString();
                             */

                            lisIntPrice.add(Integer.valueOf(StickerPrice));

                            Picasso.get().load(ImageIcon).into(holder.ciIvStikcerIcon);
                            holder.tvInfoSticker.setText(NameSticker);
                            holder.tvPrice.setText(StickerPrice+" ฿");
                            holder.rbSlectStikcer.setVisibility(View.GONE);

                            ShowSumPrice();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public StikcerBasketListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_basket_sticker,parent,false);

                return new StikcerBasketListViewHolder(view);
            }
        };

        adapterStickerBK.startListening();
        binding.recyclerViewWW.setAdapter(adapterStickerBK);


    }

    private void LoadStickerViewSS() {
        Query query = mOrder.child(mUser.getUid()).child(OrderKey).child("StickerId");
        optionsStickerBK = new FirebaseRecyclerOptions.Builder<StickerBasket>().setQuery(query,StickerBasket.class).build();
        adapterStickerBK = new FirebaseRecyclerAdapter<StickerBasket, StikcerBasketListViewHolder>(optionsStickerBK) {

            @Override
            protected void onBindViewHolder(@NonNull StikcerBasketListViewHolder holder, int position, @NonNull StickerBasket model) {

                mStickerStoreRef.child(model.getStickerId()).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Toast.makeText(getActivity(), model.getStickerId(), Toast.LENGTH_SHORT).show();

                            String NameSticker = snapshot.child("NameSticker").getValue().toString();
                            String ImageIcon = snapshot.child("ImageIcon").getValue().toString();
                            String StickerPrice = snapshot.child("StickerPrice").getValue().toString();

                            /*
                            String Check = model.getStikcerStatusCheck().toString();
                            String Id = model.getStickerId().toString();
                             */

                            lisIntPrice.add(Integer.valueOf(StickerPrice));

                            Picasso.get().load(ImageIcon).into(holder.ciIvStikcerIcon);
                            holder.tvInfoSticker.setText(NameSticker);
                            holder.tvPrice.setText(StickerPrice+" ฿");
                            holder.rbSlectStikcer.setVisibility(View.GONE);

                            ShowSumPrice();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public StikcerBasketListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_basket_sticker,parent,false);

                return new StikcerBasketListViewHolder(view);
            }
        };

        adapterStickerBK.startListening();
        binding.recyclerViewSS.setAdapter(adapterStickerBK);

    }

    private void ShowSumPrice() {
        int sum = 0;
        for(int i : lisIntPrice) {
            sum += i;
        }

        sumText = String.valueOf(sum);

        binding.textViewSumSS.setText(sumText +" ฿");
        binding.textViewSumWW.setText(sumText +" ฿");
        binding.textViewFF.setText(sumText +" ฿");
    }

    private void ShowViewStatus() {
        mOrder.child(mUser.getUid()).child(OrderKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String OderStatus = snapshot.child("OderStatus").getValue().toString();

                    if(OderStatus.equals("ShowStatusPayment")){
                        binding.CsWait.setVisibility(View.VISIBLE);
                        binding.CsF.setVisibility(View.GONE);
                        binding.CsSuccesful.setVisibility(View.GONE);
                    }else if(OderStatus.equals("InCorrect")){
                        binding.CsF.setVisibility(View.VISIBLE);
                        binding.CsWait.setVisibility(View.GONE);
                        binding.CsSuccesful.setVisibility(View.GONE);
                    }else if(OderStatus.equals("Correct")){
                        binding.CsSuccesful.setVisibility(View.VISIBLE);
                        binding.CsF.setVisibility(View.GONE);
                        binding.CsWait.setVisibility(View.GONE);
                    }else {
                        //
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}