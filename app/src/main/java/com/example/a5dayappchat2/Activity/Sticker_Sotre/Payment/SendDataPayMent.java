package com.example.a5dayappchat2.Activity.Sticker_Sotre.Payment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentSendDataPayMentBinding;
import com.example.a5dayappchat2.databinding.FragmentShowPayWayBinding;
import com.github.pheerathach.ThaiQRPromptPay;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.HashMap;

import io.github.muddz.styleabletoast.StyleableToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendDataPayMent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendDataPayMent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SendDataPayMent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendDataPayMent.
     */
    // TODO: Rename and change types and number of parameters
    public static SendDataPayMent newInstance(String param1, String param2) {
        SendDataPayMent fragment = new SendDataPayMent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    FragmentSendDataPayMentBinding binding;

    String OrderKey;
    private static final int REQUEST_CODE = 101;

    DatabaseReference mOrder,mPayment;

    StorageReference storageRef;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String BankNumber ;
    String PPNumber ;

    Uri ImagePayment;

    boolean Sussesful;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSendDataPayMentBinding.inflate(inflater, container, false);

        OrderKey = getActivity().getIntent().getStringExtra("OrderKey");

        mOrder = FirebaseDatabase.getInstance().getReference().child("Order");
        mPayment = FirebaseDatabase.getInstance().getReference().child("Payment");

        storageRef = FirebaseStorage.getInstance().getReference().child("PaymentImage");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

binding.ImagePaymentpicker.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE);
    }
});
        
        ShowPaymentWay();



        binding.btNext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String NameHolder = binding.tvNameHolder.getText().toString();
                String Note = binding.note.getText().toString();

                if(ImagePayment == null || NameHolder.isEmpty()){
                    StyleableToast.makeText(getActivity(), getString(R.string.Please_complete_the_information), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }else {

                    storageRef.child(mUser.getUid()).child(OrderKey).putFile(ImagePayment).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                storageRef.child(mUser.getUid()).child(OrderKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        HashMap hashMap = new HashMap();
                                        hashMap.put("NameHolder",NameHolder);
                                        hashMap.put("Note",Note);
                                        hashMap.put("ImagePayment",uri.toString());

                                        mOrder.child(mUser.getUid()).child(OrderKey).child("SendPayment").updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                HashMap hashMap = new HashMap();
                                                hashMap.put("OderStatus","ShowStatusPayment");

                                                mOrder.child(mUser.getUid()).child(OrderKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                                    @Override
                                                    public void onSuccess(Object o) {
                                                        Sussesful = true;
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Sussesful = false;
                                                    }
                                                });



                                            }
                                        });

                                    }
                                });
                            }

                        }
                    });


                }

                if(Sussesful) {

                    ShowStatusPayment showStatusPayment = new ShowStatusPayment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayoutMain, showStatusPayment); // give your fragment container id in first parameter
                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                    transaction.commit();
                }else {
                    StyleableToast.makeText(getActivity(), getString(R.string.Please_complete_the_information), Toast.LENGTH_LONG, R.style.ErrorAppToast).show();
                }






            }
        });


        return binding.getRoot();
    }



    private void ShowPaymentWay() {
        mOrder.child(mUser.getUid()).child(OrderKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Prompt Pay").exists()){


                    for (DataSnapshot child : snapshot.child("Prompt Pay").getChildren()) {

                        PPNumber = child.getKey();  //Pega o nome de cada tipo de arte


                    }

                    mPayment.child("Prompt Pay").child(PPNumber).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String Prompt_Pay_Name_Holder = snapshot.child("Prompt_Pay_Name_Holder").getValue().toString();
                            String Prompt_Pay_Phone_Number = snapshot.child("Prompt_Pay_Phone_Number").getValue().toString();


                            binding.hearderPayment.setText(Prompt_Pay_Name_Holder);
                            binding.numPayment.setText(Prompt_Pay_Phone_Number);


                            binding.Bankimage.setImageResource(R.drawable.pormtpaylogo);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }else if(snapshot.child("Bank Number").exists()){



                    for (DataSnapshot child : snapshot.child("Bank Number").getChildren()) {

                        BankNumber = child.getKey();  //Pega o nome de cada tipo de arte


                    }


                    mPayment.child("Bank Number").child(BankNumber).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String Bank_Holder = snapshot.child("Bank_Holder").getValue().toString();
                            String Bank_Number = snapshot.child("Bank_Number").getValue().toString();
                            String Bank_Number_Image = snapshot.child("Bank_Number_Image").getValue().toString();



                            binding.hearderPayment.setText(Bank_Holder);
                            binding.numPayment.setText(Bank_Number);
                            Picasso.get().load(Bank_Number_Image).into(binding.Bankimage);

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ImagePayment = data.getData();
            binding.showimagepayment.setImageURI(ImagePayment);

        }
    }
}




