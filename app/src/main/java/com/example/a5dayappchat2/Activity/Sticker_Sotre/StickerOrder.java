package com.example.a5dayappchat2.Activity.Sticker_Sotre;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a5dayappchat2.Activity.OrderStickerActivityViewHolder;
import com.example.a5dayappchat2.Activity.PaymentViewHolder;
import com.example.a5dayappchat2.Activity.StikcerBasketListViewHolder;
import com.example.a5dayappchat2.Activity.Utills.Order;
import com.example.a5dayappchat2.Activity.Utills.Payment;
import com.example.a5dayappchat2.Activity.Utills.StickerBasket;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentStickerBasketBinding;
import com.example.a5dayappchat2.databinding.FragmentStickerOrderBinding;
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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StickerOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StickerOrder extends Fragment {

    FragmentStickerOrderBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StickerOrder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StickerOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static StickerOrder newInstance(String param1, String param2) {
        StickerOrder fragment = new StickerOrder();
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

    DatabaseReference mOrder,mPayment,mStickerStoreRef;


    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String OrderKey;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStickerOrderBinding.inflate(inflater, container, false);

        mOrder = FirebaseDatabase.getInstance().getReference().child("Order");
        mPayment = FirebaseDatabase.getInstance().getReference().child("Payment");
        OrderKey = getActivity().getIntent().getStringExtra("OrderKey");
        mStickerStoreRef = FirebaseDatabase.getInstance().getReference().child("Sticker");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        binding.recyclerViewOrderMain.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        ShowOrder();
        
        return binding.getRoot();







    }

    FirebaseRecyclerOptions<Order> optionsOd;
    FirebaseRecyclerAdapter<Order, OrderStickerActivityViewHolder> adapterOd;

    FirebaseRecyclerOptions<StickerBasket> optionsStickerBK;
    FirebaseRecyclerAdapter<StickerBasket, StikcerBasketListViewHolder> adapterStickerBK;

    private void ShowOrder() {

        Query query = mOrder.child(mUser.getUid());

        optionsOd = new FirebaseRecyclerOptions.Builder<Order>().setQuery(query,Order.class).build();
        adapterOd = new FirebaseRecyclerAdapter<Order, OrderStickerActivityViewHolder>(optionsOd) {
            @Override
            protected void onBindViewHolder(@NonNull OrderStickerActivityViewHolder holder, int position, @NonNull Order model) {

                holder.recyclerViewProductlist.setLayoutManager(new LinearLayoutManager(getActivity()));

                String OrderKeyII = getRef(position).getKey().toString();

                String Status = model.getOderStatus();
                String Price = model.getProuctPrice();

                holder.tvSumPrice.setText(Price+" ฿");

                if(Status.equals("ChosePaymentWay")){
                    holder.btrstatusorder.setText("ChosePaymentWay");
                }else          if(Status.equals("ShowPayWay")){
                    holder.btrstatusorder.setText("Pay");
                }else         if(Status.equals("SendDataPayMent")){
                    holder.btrstatusorder.setText("Payment Notification");
                }else         if(Status.equals("ShowStatusPayment")){
                    holder.btrstatusorder.setText("Waiting For Review");
                }else         if(Status.equals("Correct")){
                    holder.btrstatusorder.setText("Order is Correct");
                    holder.btrstatusorder.setBackgroundColor(Color.parseColor("#6BD63D"));
                }else         if(Status.equals("InCorrect")){
                    holder.btrstatusorder.setText("Order is InCorrect");
                    holder.btrstatusorder.setBackgroundColor(Color.parseColor("#E94560"));
                }


                holder.btrstatusorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(getActivity(),StickerPayment.class);
                        intent.putExtra("OrderKey", OrderKeyII);
                        startActivity(intent);


                    }
                });

                    Query query = mOrder.child(mUser.getUid()).child(OrderKeyII).child("StickerId");
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


                                    Picasso.get().load(ImageIcon).into(holder.ciIvStikcerIcon);
                                    holder.tvInfoSticker.setText(NameSticker);
                                    holder.tvPrice.setText(StickerPrice+" ฿");
                                    holder.rbSlectStikcer.setVisibility(View.GONE);



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
                holder.recyclerViewProductlist.setAdapter(adapterStickerBK);
            }


            @NonNull
            @Override
            public OrderStickerActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_cell_order,parent,false);

                return new OrderStickerActivityViewHolder(view);
            }
        };

        adapterOd.startListening();
        binding.recyclerViewOrderMain.setAdapter(adapterOd);
    }
}