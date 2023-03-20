package com.example.a5dayappchat2.Activity.Sticker_Sotre.Payment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.a5dayappchat2.Activity.DataHolder.DataHolder;
import com.example.a5dayappchat2.Activity.DataHolder.DataKeyPaymentChoose;
import com.example.a5dayappchat2.Activity.DataHolder.KeyPayment;
import com.example.a5dayappchat2.Activity.PaymentViewHolder;
import com.example.a5dayappchat2.Activity.SetupActivity;
import com.example.a5dayappchat2.Activity.Sticker_Sotre.StickerPayment;
import com.example.a5dayappchat2.Activity.StikcerBasketListViewHolder;
import com.example.a5dayappchat2.Activity.Utills.Payment;
import com.example.a5dayappchat2.Activity.Utills.StickerBasket;
import com.example.a5dayappchat2.Activity.constant.Constant;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentChooesBankBinding;
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
import java.util.HashMap;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chooes_bank#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chooes_bank extends Fragment {

    FragmentChooesBankBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Chooes_bank() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chooes_bank.
     */
    // TODO: Rename and change types and number of parameters
    public static Chooes_bank newInstance(String param1, String param2) {
        Chooes_bank fragment = new Chooes_bank();
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


    ArrayList<String> spinnerArray = new ArrayList<String>();


    DatabaseReference mPayment,mOrder,mStickerStoreRef;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String sumText;
    String OrderKey;

    String KeyPayWayPN, KeyPayWayPP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChooesBankBinding.inflate(inflater, container, false);

        mPayment = FirebaseDatabase.getInstance().getReference().child("Payment");
        mOrder = FirebaseDatabase.getInstance().getReference().child("Order");
        mStickerStoreRef = FirebaseDatabase.getInstance().getReference().child("Sticker");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        binding.recyclerViewPaymentlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewPDList.setLayoutManager(new LinearLayoutManager(getActivity()));

        OrderKey = getActivity().getIntent().getStringExtra("OrderKey");



        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(KeyPayWayPN != null){
                    UpDateDataOrderPn();

                    ShowPayWay showPayWay = new ShowPayWay();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayoutMain, showPayWay ); // give your fragment container id in first parameter
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();

                }else if(KeyPayWayPP != null){
                    UpDateDataOrderPp();

                    ShowPayWay showPayWay = new ShowPayWay();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayoutMain, showPayWay ); // give your fragment container id in first parameter
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();

                }else {
                    StyleableToast.makeText(getActivity(), getString(R.string.Please_complete_the_information), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }







            }
        });

        LoadHearderPayment();
        LoadproductSticker(OrderKey);




        return binding.getRoot();

    }



    private void UpDateDataOrderPp() {
        HashMap hashMap = new HashMap();
        hashMap.put("OderStatus","ShowPayWay");
        hashMap.put("ProuctPrice",sumText);

        mOrder.child(mUser.getUid()).child(OrderKey).updateChildren(hashMap);

        HashMap hashMap2 = new HashMap();
        hashMap2.put("PaymentWay",KeyPayWayPP);
        mOrder.child(mUser.getUid()).child(OrderKey).child("Prompt Pay").child(KeyPayWayPP).updateChildren(hashMap2);

    }

    private void UpDateDataOrderPn() {
        HashMap hashMap = new HashMap();
        hashMap.put("OderStatus","ShowPayWay");
        hashMap.put("ProuctPrice",sumText);
        mOrder.child(mUser.getUid()).child(OrderKey).updateChildren(hashMap);

        HashMap hashMap2 = new HashMap();
        hashMap2.put("PaymentWay",KeyPayWayPN);
        mOrder.child(mUser.getUid()).child(OrderKey).child("Bank Number").child(KeyPayWayPN).updateChildren(hashMap2);



    }

    ArrayList<Integer> lisIntPrice = new ArrayList<>() ;

    FirebaseRecyclerOptions<Payment> optionsPM;
    FirebaseRecyclerAdapter<Payment, PaymentViewHolder> adapterPM;

    FirebaseRecyclerOptions<StickerBasket> optionsStickerBK;
    FirebaseRecyclerAdapter<StickerBasket, StikcerBasketListViewHolder> adapterStickerBK;


    private void LoadproductSticker(String orderKey) {

        Query query = mOrder.child(mUser.getUid()).child(orderKey).child("StickerId");
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
        binding.recyclerViewPDList.setAdapter(adapterStickerBK);

    }

    private void ShowSumPrice() {
        int sum = 0;
        for(int i : lisIntPrice) {
            sum += i;
        }

         sumText = String.valueOf(sum);

        binding.tvSumPDPrice.setText("" + "total amount"+sumText +" ฿");
    }

    private void LoadHearderPayment() {
        mPayment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {

                    spinnerArray.add(child.getKey());   //Pega o nome de cada tipo de arte


                }

                



                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item,
                        spinnerArray);

                binding.spinnerChoosePayment.setAdapter(spinnerArrayAdapter);

                binding.spinnerChoosePayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = binding.spinnerChoosePayment.getSelectedItem().toString();

                        if(item.equals("Bank Number")){
                            LoadDataBank();
                        }else if(item.equals("Prompt Pay")) {
                            LoadDataPromptPay();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void LoadDataPromptPay() {

        Query query = mPayment.child("Prompt Pay");

        optionsPM = new FirebaseRecyclerOptions.Builder<Payment>().setQuery(query, Payment.class).build();
        adapterPM = new FirebaseRecyclerAdapter<Payment, PaymentViewHolder>(optionsPM) {

            @Override
            protected void onBindViewHolder(@NonNull PaymentViewHolder holder, int position, @NonNull Payment model) {
                holder.PaymentHead.setText(model.getPrompt_Pay_Name_Holder());
                holder.PaymentSecond.setText(model.getPrompt_Pay_Phone_Number());
                holder.ivPaymentIcon.setImageResource(R.drawable.pormtpaylogo);

                holder.rbChooseWayPayMent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         KeyPayWayPP = getRef(position).getKey().toString();

                        Log.d("SADASDASD",KeyPayWayPP);

                    }
                });

            }

            @NonNull
            @Override
            public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_cell_payment_way, parent, false);
                return new PaymentViewHolder(view);
            }
        };

        adapterPM.startListening();
        binding.recyclerViewPaymentlist.setAdapter(adapterPM);


    }

    private void LoadDataBank() {

        Query query = mPayment.child("Bank Number");
        optionsPM = new FirebaseRecyclerOptions.Builder<Payment>().setQuery(query, Payment.class).build();
        adapterPM = new FirebaseRecyclerAdapter<Payment, PaymentViewHolder>(optionsPM) {


            @Override
            protected void onBindViewHolder(@NonNull PaymentViewHolder holder, int position, @NonNull Payment model) {
                holder.PaymentHead.setText(model.getBank_Holder());
                holder.PaymentSecond.setText(model.getBank_Number());
                Picasso.get().load(model.getBank_Number_Image()).into(holder.ivPaymentIcon);

                holder.rbChooseWayPayMent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         KeyPayWayPN = getRef(position).getKey().toString();

                    }
                });


            }

            @NonNull
            @Override
            public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_cell_payment_way, parent, false);
                return new PaymentViewHolder(view);
            }
        };

        adapterPM.startListening();
        binding.recyclerViewPaymentlist.setAdapter(adapterPM);

    }
}