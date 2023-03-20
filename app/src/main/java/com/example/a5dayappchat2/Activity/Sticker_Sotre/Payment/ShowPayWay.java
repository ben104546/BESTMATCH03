package com.example.a5dayappchat2.Activity.Sticker_Sotre.Payment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.a5dayappchat2.Activity.DataHolder.DataHolder;
import com.example.a5dayappchat2.Activity.DataHolder.KeyPayment;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentShowPayWayBinding;
import com.github.pheerathach.ThaiQRPromptPay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowPayWay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowPayWay extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowPayWay() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowPayWay.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowPayWay newInstance(String param1, String param2) {
        ShowPayWay fragment = new ShowPayWay();
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

    FragmentShowPayWayBinding binding;
    String OrderKey;
    DatabaseReference mOrder,mPayment;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String BankNumber ;
    String PPNumber ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowPayWayBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        OrderKey = getActivity().getIntent().getStringExtra("OrderKey");

        mOrder = FirebaseDatabase.getInstance().getReference().child("Order");
        mPayment = FirebaseDatabase.getInstance().getReference().child("Payment");

        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap hashMap = new HashMap();
                hashMap.put("OderStatus","SendDataPayMent");

                mOrder.child(mUser.getUid()).child(OrderKey).updateChildren(hashMap);


                SendDataPayMent sendDataPayMent = new SendDataPayMent();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayoutMain, sendDataPayMent ); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });




        Log.d("DASDADAD",OrderKey);


        
        ShowPayWayMt();

/*
        try {
            ShowPromtPay();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }

 */
        return binding.getRoot();


    }



    private void ShowPayWayMt() {
      mOrder.child(mUser.getUid()).child(OrderKey).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {

              String PDPrice = snapshot.child("ProuctPrice").getValue().toString();



              if(snapshot.child("Prompt Pay").exists()){
                  binding.constraintLayoutPP.setVisibility(View.VISIBLE);


                  for (DataSnapshot child : snapshot.child("Prompt Pay").getChildren()) {

                      PPNumber = child.getKey();  //Pega o nome de cada tipo de arte


                  }

                  mPayment.child("Prompt Pay").child(PPNumber).addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          String Prompt_Pay_Name_Holder = snapshot.child("Prompt_Pay_Name_Holder").getValue().toString();
                          String Prompt_Pay_Phone_Number = snapshot.child("Prompt_Pay_Phone_Number").getValue().toString();


                          binding.tvPPHolder.setText(Prompt_Pay_Name_Holder);
                          binding.tvPPNumber.setText(Prompt_Pay_Phone_Number);

                          ThaiQRPromptPay qr2 = new ThaiQRPromptPay.Builder().staticQR().creditTransfer().mobileNumber(Prompt_Pay_Phone_Number).amount(new BigDecimal(PDPrice)).build();
                          String content2 = qr2.generateContent();

                          MultiFormatWriter mWriter = new MultiFormatWriter();

                          try {

                              BitMatrix mMatrix = mWriter.encode(content2, BarcodeFormat.QR_CODE, 400,400);

                              BarcodeEncoder mEncoder = new BarcodeEncoder();
                              Bitmap mBitmap = mEncoder.createBitmap(mMatrix);

                              binding.ivQrPP.setImageBitmap(mBitmap);

                          }catch (WriterException e) {
                              e.printStackTrace();
                          }


                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                  });


              }else if(snapshot.child("Bank Number").exists()){

                  binding.constraintLayoutBanknum.setVisibility(View.VISIBLE);



                  for (DataSnapshot child : snapshot.child("Bank Number").getChildren()) {

                      BankNumber = child.getKey();  //Pega o nome de cada tipo de arte


                  }


                  mPayment.child("Bank Number").child(BankNumber).addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {

                          String Bank_Holder = snapshot.child("Bank_Holder").getValue().toString();
                          String Bank_Number = snapshot.child("Bank_Number").getValue().toString();
                          String Bank_EWallet = snapshot.child("Bank_EWallet").getValue().toString();

                          binding.tvBNHolder.setText(Bank_Holder);
                          binding.tvBNNumber.setText(Bank_Number);

                          ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().staticQR().creditTransfer().eWalletId(Bank_EWallet).amount(new BigDecimal(PDPrice)).build();
                          String content = qr.generateContent();

                          MultiFormatWriter mWriter = new MultiFormatWriter();

                          try {

                              BitMatrix mMatrix = mWriter.encode(content, BarcodeFormat.QR_CODE, 400,400);

                              BarcodeEncoder mEncoder = new BarcodeEncoder();
                              Bitmap mBitmap = mEncoder.createBitmap(mMatrix);

                              binding.tvQRBankNUM.setImageBitmap(mBitmap);

                          }catch (WriterException e) {
                              e.printStackTrace();
                          }




                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {
                          Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  });




              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
              Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
          }
      });



    }




    private void ShowPromtPay() throws IOException, WriterException {
        int Price = 100;
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().staticQR().creditTransfer().eWalletId("004999124636332").amount(new BigDecimal(Price)).build();
        String content = qr.generateContent();

        ThaiQRPromptPay qr2 = new ThaiQRPromptPay.Builder().staticQR().creditTransfer().mobileNumber("0643398718").amount(new BigDecimal("10.00")).build();
        String content2 = qr2.generateContent();

        MultiFormatWriter mWriter = new MultiFormatWriter();

        try {

            BitMatrix mMatrix = mWriter.encode(content2, BarcodeFormat.QR_CODE, 400,400);

            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);

           // binding.imageView27.setImageBitmap(mBitmap);

        }catch (WriterException e) {
            e.printStackTrace();
        }


       Log.i("DASDASDADA",content);
       Log.i("DASDASDADA",content2);




    }
}