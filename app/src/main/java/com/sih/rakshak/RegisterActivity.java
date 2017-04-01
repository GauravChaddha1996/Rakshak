package com.sih.rakshak;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.sih.rakshak.features.HomeActivity;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import devliving.online.securedpreferencestore.SecuredPreferenceStore;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.username_til)
    TextInputLayout usernameTil;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.password_til)
    TextInputLayout passwordTil;
    @BindView(R.id.signup_button)
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signup_button)
    void signUp() {
        Single.create(singleSubscriber -> {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "email=" + username.getText().toString()
                    + "&password=" + password.getText().toString());
            Request request = new Request.Builder()
                    .url("https://mail.rakshak.ml/admin/mail/users/add")
                    .post(body)
                    .addHeader("authorization", "Basic dXNAcmFrc2hhay5tbDoyMjFCX3NpaDIwMTc=")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                singleSubscriber.onSuccess(response);
            } catch (IOException e) {
                e.printStackTrace();
                singleSubscriber.onError(e);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> {
                    Response response = (Response) o;
                    if (response.code() == 200) {
                        saveUsernamePassword();
                        try {
                            Snackbar snackbar = Snackbar.make(password, response.body().string(), Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("Go to inbox", v -> {
                                startActivity(new Intent(this, HomeActivity.class));
                            }).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("tag", String.valueOf(response.code()));
                        Log.d("tag", String.valueOf(response.message()));
                        Snackbar.make(password, "Error", Snackbar.LENGTH_LONG).show();
                    }
                }, Throwable::printStackTrace);
    }

    private void saveUsernamePassword() {
        try {
            SecuredPreferenceStore prefStore = SecuredPreferenceStore.getSharedInstance(getApplicationContext());
            prefStore.edit().putString("username", username.getText().toString()).apply();
            prefStore.edit().putString("password", password.getText().toString()).apply();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

    }

}
