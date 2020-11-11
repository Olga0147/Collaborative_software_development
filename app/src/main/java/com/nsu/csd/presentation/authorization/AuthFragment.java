package com.nsu.csd.presentation.authorization;

import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nsu.csd.data.remote.ApiUtils;
import com.nsu.csd.R;
import com.nsu.csd.model.ServerError;
import com.nsu.csd.model.UserLoginDTO;
import com.nsu.csd.presentation.common.Validation;
import com.nsu.csd.presentation.registration.RegFragment;
import com.nsu.csd.presentation.work.MainActivity;

import java.io.IOException;
import java.util.Objects;

public class AuthFragment extends Fragment {


    private EditText email;
    private EditText password;
    private TextView error_msg;

    Gson gson = new GsonBuilder().create();

    public void changeError_msg(String error_msg) {
        this.error_msg.setText(error_msg);
    }

    private final View.OnClickListener onClickListener_forget_pass = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        //TODO: forget_pass
        }
    };

    private final View.OnClickListener onClickListener_enter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ( Validation.isInputValid(
                    email.getText().toString(),
                    password.getText().toString() ) ) {

                UserLoginDTO userLoginDTO = new UserLoginDTO(email.getText().toString(), password.getText().toString());
                ApiUtils.getApiService().sign_in(userLoginDTO).enqueue(
                        new retrofit2.Callback<Void>(){

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
                                        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(mainIntent);
                                        getActivity().finish();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                                mainHandler.post(() -> changeError_msg("Connection error"));
                            }
                        }
                );//enqueue() Для асинхронного получения
            } else {
                changeError_msg("Bad email or password");
            }

        }
    };

    private final View.OnClickListener onClickListener_register = v -> {
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.fragment_container_id, RegFragment.newInstance()).
                addToBackStack(AuthFragment.class.getName()).
                commit();
    };

    private final View.OnClickListener onClickListener_enter_google = v -> {
    //TODO
    };

    public static AuthFragment newInstance() {
        Bundle args = new Bundle();
        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.auth_layout,container,false);

        email = view.findViewById(R.id.auth_email);
        password = view.findViewById(R.id.auth_pass);
        error_msg = view.findViewById(R.id.auth_error_msg);

        Button forget_pass = view.findViewById(R.id.auth_forget_pass_btn);
        Button enter = view.findViewById(R.id.auth_enter_btn);
        Button register = view.findViewById(R.id.auth_registration_btn);
        Button enter_google = view.findViewById(R.id.auth_enter_google_btn);

        forget_pass.setOnClickListener(onClickListener_forget_pass);
        enter.setOnClickListener(onClickListener_enter);
        register.setOnClickListener(onClickListener_register);
        enter_google.setOnClickListener(onClickListener_enter_google);

        return view;
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

}
