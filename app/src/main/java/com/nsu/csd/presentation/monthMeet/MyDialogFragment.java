package com.nsu.csd.presentation.monthMeet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.nsu.csd.R;

public class MyDialogFragment extends AppCompatDialogFragment {

    String meet_id;

    public MyDialogFragment(String inviteKey) {
        meet_id = inviteKey;
    }

    //Java
// Показан только метод, без предыдущего кода для фрагмента
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        TextView title = new TextView(getContext());
        title.setText("Пригласите друзей");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.event_color));
        title.setTextSize(23);

        TextView msg = new TextView(getContext());
        msg.setText(meet_id);
        msg.setPadding(10, 10, 10, 10);
        msg.setTextColor(getResources().getColor(R.color.text_color));
        msg.setGravity(Gravity.CENTER);
        msg.setTextSize(18);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCustomTitle(title);
        builder.setView(msg);

        return builder.create();
    }
}
