package com.nsu.csd.presentation.registration;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsu.csd.data.remote.ApiUtils;
import com.nsu.csd.R;
import com.nsu.csd.model.ServerError;
import com.nsu.csd.model.UserRegistrationDTO;
import com.nsu.csd.presentation.common.Validation;

import java.io.IOException;

public class RegFragment extends Fragment {

    private EditText email;
    private EditText first_name;
    private EditText last_name;
    private EditText password;
    private EditText password_duplicate;
    private TextView error_msg;
    private Button register;

    Gson gson = new GsonBuilder().create();

    private final View.OnClickListener onClickListener_register = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(!Validation.isEmailValid(email.getText().toString())){
                changeError_msg("Bad email");

            }else if(!Validation.isPasswordsValid(
                    password.getText().toString(),
                    password_duplicate.getText().toString()) ){
                changeError_msg("Bad password");

            }else if(!Validation.isUserValid(
                    first_name.getText().toString(),
                    last_name.getText().toString())){
                changeError_msg("Bad first or last name");

            }else  {
                UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                        email.getText().toString(),
                        first_name.getText().toString(),
                        last_name.getText().toString(),
                        password.getText().toString()
                );

                ApiUtils.getApiService().sign_up(userRegistrationDTO).enqueue(
                        new retrofit2.Callback<Void>() {
                            //используем Handler, чтобы показывать ошибки в Main потоке, т.к. наши коллбеки возвращаются в рабочем потоке
                            final Handler mainHandler = new Handler(getActivity().getMainLooper());

                            @Override
                            public void onResponse(retrofit2.Call<Void> call, final retrofit2.Response<Void> response) {
                                mainHandler.post(() -> {
                                    if (!response.isSuccessful()) {
                                        try {
                                            ServerError serverError = gson.fromJson(response.errorBody().string(), ServerError.class);
                                            changeError_msg(serverError.getMessage());
                                        } catch (IOException e) {
                                            changeError_msg("sorry, some error exists");
                                            //TODO: log
                                        }
                                    } else {
                                        showMessage(R.string.registration_success);
                                        getFragmentManager().popBackStack();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                                mainHandler.post(() -> changeError_msg("Connection error"));
                            }
                        });
            }
        }
    };

    public static RegFragment newInstance() {
        Bundle args = new Bundle();
        RegFragment fragment = new RegFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reg_layout,container,false);

        email = view.findViewById(R.id.reg_email);
        last_name = view.findViewById(R.id.reg_last_name);
        first_name = view.findViewById(R.id.reg_first_name);
        password = view.findViewById(R.id.reg_pass);
        password_duplicate = view.findViewById(R.id.reg_pass_duplicate);
        error_msg = view.findViewById(R.id.reg_error_msg);

        register = view.findViewById(R.id.reg_register_btn);
        register.setOnClickListener(onClickListener_register);

        return view;
    }

    public void changeError_msg(String error_msg) {
        this.error_msg.setText(error_msg);
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }
}
