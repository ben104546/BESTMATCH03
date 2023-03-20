package com.example.a5dayappchat2.Activity.Sticker_Sotre;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a5dayappchat2.Activity.StickerStoreViewHolder;
import com.example.a5dayappchat2.Activity.Utills.StickerStore;
import com.example.a5dayappchat2.R;
import com.example.a5dayappchat2.databinding.FragmentAllDoingsBinding;
import com.example.a5dayappchat2.databinding.FragmentStickerStoreMainBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StickerStoreMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StickerStoreMain extends Fragment {
    FragmentStickerStoreMainBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StickerStoreMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StickerStoreMain.
     */
    // TODO: Rename and change types and number of parameters
    public static StickerStoreMain newInstance(String param1, String param2) {
        StickerStoreMain fragment = new StickerStoreMain();
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

    DatabaseReference mStickerStoreRef;

    FirebaseRecyclerOptions<StickerStore> optionsSticker;
    FirebaseRecyclerAdapter<StickerStore, StickerStoreViewHolder> adapterSticker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStickerStoreMainBinding.inflate(inflater, container, false);

        mStickerStoreRef = FirebaseDatabase.getInstance().getReference().child("Sticker");

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        binding.recyclerViewStickerStore.setLayoutManager(layoutManager);
        LoadSticker("");

        return binding.getRoot();
    }

    private void LoadSticker(String s) {
        Query query = mStickerStoreRef.orderByChild("NameSticker").startAt(s).endAt(s+"\uf8ff");
        optionsSticker = new FirebaseRecyclerOptions.Builder<StickerStore>().setQuery(query,StickerStore.class).build();
        adapterSticker = new FirebaseRecyclerAdapter<StickerStore, StickerStoreViewHolder>(optionsSticker) {


            @Override
            protected void onBindViewHolder(@NonNull StickerStoreViewHolder holder, int position, @NonNull StickerStore model) {


                holder.tvStickerName.setText(model.getNameSticker());
                Picasso.get().load(model.getImageIcon()).into(holder.ivStickerIcon);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String StickerKey = getRef(position).getKey().toString();

                        Intent intent = new Intent(getActivity(),StickerDetail.class);
                        intent.putExtra("StickerKey",StickerKey);
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public StickerStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_cell_sticker_store,parent,false);

                return new StickerStoreViewHolder(view);
            }
        };
        adapterSticker.startListening();
        binding.recyclerViewStickerStore.setAdapter(adapterSticker);
    }
}