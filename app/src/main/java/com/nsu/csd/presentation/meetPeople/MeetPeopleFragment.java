package com.nsu.csd.presentation.meetPeople;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nsu.csd.R;
import com.nsu.csd.model.MeetingInfoDto;
import com.nsu.csd.presentation.detailedEvent.DetailedEventFragment;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class MeetPeopleFragment extends Fragment {

    public static MeetPeopleFragment newInstance() {
        Bundle args = new Bundle();
        MeetPeopleFragment fragment = new MeetPeopleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    String meet_id;
    List<MeetingInfoDto.ParticipantsDTO> people;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Gson gson = new Gson();

        Bundle arguments = getActivity().getIntent().getExtras();
        if(arguments!=null){
            meet_id  = arguments.get("meet_id").toString();
            String p = arguments.get("people").toString();
            Type type = new TypeToken<List<MeetingInfoDto.ParticipantsDTO>>(){}.getType();
            people = gson.fromJson(p,type);
        }

        StringBuilder str = new StringBuilder();
        for (MeetingInfoDto.ParticipantsDTO person : people) {
            str.append(person.getFirstName()).append(" ").append(person.getLastName()).append("\n");
        }

        View view = inflater.inflate(R.layout.people,container,false);

        TextView textView = view.findViewById(R.id.people);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(str);

        return view;
    }

}
