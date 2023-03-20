package com.example.a5dayappchat2.Activity.Sticker_Sotre;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.a5dayappchat2.Activity.SetupActivity;
import com.example.a5dayappchat2.Activity.Sticker_Sotre.Payment.Chooes_bank;
import com.example.a5dayappchat2.Activity.StikcerBasketListViewHolder;
import com.example.a5dayappchat2.Activity.Utills.StickerBasket;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentStickerBasketBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

import io.github.muddz.styleabletoast.StyleableToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sticker_Basket#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sticker_Basket extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Sticker_Basket() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Sticker_Basket.
     */
    // TODO: Rename and change types and number of parameters
    public static Sticker_Basket newInstance(String param1, String param2) {
        Sticker_Basket fragment = new Sticker_Basket();
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


    FragmentStickerBasketBinding binding;
    DatabaseReference mStickerStoreRef,mStickerBasket,mOrder;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseRecyclerOptions<StickerBasket> optionsStickerBK;
    FirebaseRecyclerAdapter<StickerBasket, StikcerBasketListViewHolder> adapterStickerBK;

    ArrayList<String> listStickerKey = new ArrayList<>() ;
    ArrayList<Integer> lisIntPrice = new ArrayList<>() ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStickerBasketBinding.inflate(inflater, container, false);
        mStickerBasket = FirebaseDatabase.getInstance().getReference().child("Sticker Basket");
        mStickerStoreRef = FirebaseDatabase.getInstance().getReference().child("Sticker");
        mOrder = FirebaseDatabase.getInstance().getReference().child("Order");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        binding.recyclerViewBasketlist.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.tvDeleteIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteItem();
            }
        });
        
        binding.btBuyingStikcer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateOrder();
            }
        });

        LoadStickerBaseketList();


        return binding.getRoot();
    }



    private void CreateOrder() {
        if(listStickerKey.isEmpty()){
            StyleableToast.makeText(getActivity(), getString(R.string.Please_Choose_Sticker), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
        }else {
            HashMap hashMap = new HashMap();
            hashMap.put("OderStatus","ChosePaymentWay");

            final String key = mOrder.push().getKey();


            mOrder.child(mUser.getUid()).child(key).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Log.d("ASDDADADAD",listStickerKey.toString());

                    for (int i = 0; i<listStickerKey.size();i++){
                        AddStickerKeyInOrder(listStickerKey.get(i),key);
                        Log.d("DASDAD",listStickerKey.get(i));
                    }

                    listStickerKey.clear();

                    Intent intent = new Intent(getActivity(),StickerPayment.class);
                    intent.putExtra("OrderKey", key);

                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    private void AddStickerKeyInOrder(String s, String key) {
        HashMap hashMap = new HashMap();
        hashMap.put("StickerId",s);

        mOrder.child(mUser.getUid()).child(key).child("StickerId").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                mStickerBasket.child(mUser.getUid()).child(s).removeValue();
            }
        });
    }

    private void DeleteItem() {
        for(int i = 0; i<listStickerKey.toArray().length;i++){
            mStickerBasket.child(mUser.getUid()).child(listStickerKey.get(i).toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    LoadStickerBaseketList();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    private void LoadStickerBaseketList() {
        Query query = mStickerBasket.child(mUser.getUid());
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

                            String Check = model.getStikcerStatusCheck().toString();
                            String Id = model.getStickerId().toString();


                            if(StickerPrice.isEmpty()){
                                holder.tvPrice.setVisibility(View.GONE);
                            }



                            if(Check.equals("false")){
                                binding.constraintLayoutListBasket.setVisibility(View.GONE);
                            }else {
                                binding.constraintLayoutListBasket.setVisibility(View.VISIBLE);
                            }


                            if(Check.equals("true")){
                                holder.rbSlectStikcer.setChecked(true);
                                lisIntPrice.add(Integer.valueOf(StickerPrice));
                                listStickerKey.add(model.getStickerId().toString());
                                ShowSumPrice();
                            }else {
                                holder.rbSlectStikcer.setChecked(false);
                                lisIntPrice.remove(Integer.valueOf(StickerPrice));
                                listStickerKey.remove(model.getStickerId().toString());
                                ShowSumPrice();
                            }


                            holder.rbSlectStikcer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    if(Check.equals("true")){
                                        holder.rbSlectStikcer.setChecked(false);
                                        boolean CheckValue = false;
                                        UploadData(CheckValue,Id);
                                        listStickerKey.remove(getRef(position).getKey());



                                    }else {
                                        holder.rbSlectStikcer.setChecked(true);
                                        boolean CheckValue = true;
                                        UploadData(CheckValue,Id);
                                        listStickerKey.add(getRef(position).getKey());


                                    }

                                }
                            });




                            Picasso.get().load(ImageIcon).into(holder.ciIvStikcerIcon);
                            holder.tvInfoSticker.setText(NameSticker);
                            holder.tvPrice.setText(StickerPrice+" ฿");

                            Log.d("ASDDADADAD",listStickerKey.toString());


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
        binding.recyclerViewBasketlist.setAdapter(adapterStickerBK);
    }

    private void ShowSumPrice() {

        int sum = 0;
        for(int i : lisIntPrice) {
            sum += i;
        }

        String sumText = String.valueOf(sum);

        binding.sumPrice.setText(sumText +" ฿");

    }

    private void UploadData(boolean checkValue, String id) {
        if(!checkValue){
            HashMap hashMap = new HashMap();
            hashMap.put("StikcerStatusCheck","false");


            mStickerBasket.child(mUser.getUid()).child(id).updateChildren(hashMap);
        }else {
            HashMap hashMap = new HashMap();
            hashMap.put("StikcerStatusCheck","true");


            mStickerBasket.child(mUser.getUid()).child(id).updateChildren(hashMap);
        }
    }
}