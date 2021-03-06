package com.sih.rakshak;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sih.rakshak.features.HomeActivity;
import com.sih.rakshak.features.Utils;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
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
import rx.Observable;
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
    @BindView(R.id.imaphost)
    EditText imaphost;
    @BindView(R.id.imapPort)
    EditText imapPort;
    @BindView(R.id.smtphost)
    EditText smtphost;
    @BindView(R.id.smtpPort)
    EditText smtpPort;
    @BindView(R.id.rakshak)
    AppCompatRadioButton rakshakServiceButton;
    @BindView(R.id.gmail)
    AppCompatRadioButton gmailServiceButton;
    @BindView(R.id.yahooService)
    AppCompatRadioButton yahooService;
    @BindView(R.id.otherService)
    AppCompatRadioButton otherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setRadioButtons();
    }

    private void setRadioButtons() {

        rakshakServiceButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                imaphost.setText("mail.rakshak.ml");
                imapPort.setText("993");
                smtphost.setText("mail.rakshak.ml");
                smtpPort.setText("587");
                gmailServiceButton.setChecked(false);
                yahooService.setChecked(false);
                otherService.setChecked(false);
            }
        });
        gmailServiceButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                imaphost.setText("imap.gmail.com");
                imapPort.setText("993");
                smtphost.setText("smtp.gmail.com");
                smtpPort.setText("587");
                rakshakServiceButton.setChecked(false);
                yahooService.setChecked(false);
                otherService.setChecked(false);
            }

        });
        yahooService.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                imaphost.setText("imap.mail.yahoo.com");
                imapPort.setText("993");
                smtphost.setText("smtp.mail.yahoo.com");
                smtpPort.setText("587");

                rakshakServiceButton.setChecked(false);
                gmailServiceButton.setChecked(false);
                otherService.setChecked(false);
            }
        });
        otherService.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                imaphost.setText("");
                imapPort.setText("");
                smtphost.setText("");
                smtpPort.setText("");

                rakshakServiceButton.setChecked(false);
                gmailServiceButton.setChecked(false);
                yahooService.setChecked(false);
            }

        });
    }

    @OnClick(R.id.signup_button)
    void signUp() {
        if (rakshakServiceButton.isChecked()) {
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
                                Snackbar.make(password, response.body().string() +
                                        ". Just wrapping up.", Snackbar.LENGTH_INDEFINITE).show();
                                Observable.just(1)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(integer -> {
                                            pgpKeysStore(username.getText().toString());
                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (response.code() == 400) {
                            Toast.makeText(this, "Public key already exists", Toast.LENGTH_LONG).show();
                            saveUsernamePassword();
                            startActivity(new Intent(this, HomeActivity.class));
                            Log.d("tag", String.valueOf(response.code()));
                            Log.d("tag", String.valueOf(response.message()));
                        } else {
                            Log.d("tag", String.valueOf(response.code()));
                            Log.d("tag", String.valueOf(response.message()));
                            try {
                                Snackbar.make(password, response.body().string(), Snackbar.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }, Throwable::printStackTrace);
        } else {
            pgpKeysStore(username.getText().toString());
        }
    }

    void pgpKeysStore(String email_id) {
        saveUsernamePassword();
        KeyPair keyPair = Utils.getKeyPair();
        Log.d("public key", String.valueOf(keyPair.getPublic()));
        String publicKey = "";
        try {
            publicKey = Base64.encodeToString(Utils.toByteArray(keyPair.getPublic()), Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("tag", publicKey);
        String finalPublicKey = publicKey;
        Single.create(singleSubscriber -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://139.59.31.108:5000/addpublic?email_id=" + email_id + "&public_key=" + finalPublicKey)
                    .get()
                    .addHeader("cache-control", "no-cache")
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
                        savePgpKeys(keyPair);
                        try {
                            startActivity(new Intent(this, HomeActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (response.code() == 400) {
                        Toast.makeText(this, "Public key already exists", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, HomeActivity.class));
                        Log.d("tag", String.valueOf(response.code()));
                        Log.d("tag", String.valueOf(response.message()));
                    }
                }, Throwable::printStackTrace);
    }

    private void savePgpKeys(KeyPair keyPair) {
        try {
            SecuredPreferenceStore prefStore = SecuredPreferenceStore.getSharedInstance(getApplicationContext());
            prefStore.edit().putString("publicKey", Base64.encodeToString(Utils.toByteArray(keyPair.getPublic()), Base64.DEFAULT)).apply();
            prefStore.edit().putString("privateKey", Base64.encodeToString(Utils.toByteArray(keyPair.getPrivate()), Base64.DEFAULT)).apply();
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

    private void saveUsernamePassword() {
        try {
            SecuredPreferenceStore prefStore = SecuredPreferenceStore.getSharedInstance(getApplicationContext());
            prefStore.edit().putString("username", username.getText().toString()).apply();
            prefStore.edit().putString("password", password.getText().toString()).apply();
            prefStore.edit().putString("imaphost", imaphost.getText().toString()).apply();
            prefStore.edit().putString("imapport", imapPort.getText().toString()).apply();
            prefStore.edit().putString("smtphost", smtphost.getText().toString()).apply();
            prefStore.edit().putString("smtpport", smtpPort.getText().toString()).apply();
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
