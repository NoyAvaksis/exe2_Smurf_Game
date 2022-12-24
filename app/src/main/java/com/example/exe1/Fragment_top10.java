package com.example.exe1;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;


public class Fragment_top10 extends Fragment{

    private MaterialTextView recordsNames[];
    private MaterialTextView recordsScore[];
    private Records records;


    public Fragment_top10(){
        recordsNames= new MaterialTextView[DataManager.NUM_OF_RECORDS];
        recordsScore= new MaterialTextView[DataManager.NUM_OF_RECORDS];
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        findViews(view);
        initViews();

        return view;
    }

    private void initViews() {
        for (int i = 0; i < records.numOfRecords() ; i++) {
            recordsNames[i].setText(""+records.getDetailsByIndex(i).getName());
            recordsScore[i].setText(""+records.getDetailsByIndex(i).getScore());
        }

    }

    private void findViews(View view) {
        records= DataManager.getDataManager().getTopRecords();
        for (int i =0; i < DataManager.NUM_OF_RECORDS; i++) {
            recordsNames[i]= (MaterialTextView) getTopViews("name",(i+1),view,"LBL");
            recordsScore[i]=(MaterialTextView) getTopViews("score",(i+1),view,"LBL");
        }
    }


    private View getTopViews(String col, int place , View view, String viewType) {
        return view.findViewById(view.getResources().getIdentifier("list_"+viewType+"_"+col+""+place,"id",getActivity().getPackageName()));
    }

}
