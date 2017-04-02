package com.sih.rakshak.features.sendmail;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.sih.rakshak.R;
import com.sih.rakshak.features.CONSTANTS;
import com.sih.rakshak.features.Utils;

import java.io.IOException;

import javax.crypto.SecretKey;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SendMailActivity extends AppCompatActivity {

    @BindView(R.id.from)
    EditText from;
    @BindView(R.id.from_til)
    TextInputLayout fromTil;
    @BindView(R.id.to)
    EditText to;
    @BindView(R.id.to_til)
    TextInputLayout toTil;
    @BindView(R.id.subject)
    EditText subject;
    @BindView(R.id.subject_til)
    TextInputLayout subjectTil;
    @BindView(R.id.body)
    EditText body;
    @BindView(R.id.body_til)
    TextInputLayout bodyTil;
    @BindView(R.id.send_button)
    Button sendButton;
    private String publicKey;
    private SecretKey secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        ButterKnife.bind(this);
        from.setText(CONSTANTS.getUsername(this));
        createListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void createListeners() {
        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (from.getText().toString().trim().isEmpty() ||
                        !Patterns.EMAIL_ADDRESS.matcher(from.getText().toString()).matches()) {
                    fromTil.setError("No mail to send from");
                } else {
                    fromTil.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (to.getText().toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(
                        to.getText().toString()).matches()) {
                    toTil.setError("Enter a correct email address");
                } else {
                    toTil.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        subject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (subject.getText().toString().trim().isEmpty()) {
                    subjectTil.setError("Please enter a subject");
                } else {
                    subjectTil.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        body.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (body.getText().toString().trim().isEmpty()) {
                    bodyTil.setError("Enter a correct college name");
                } else {
                    bodyTil.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean errorChecks() {
        boolean toReturn = true;
        if (from.getText().toString().trim().isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(from.getText().toString()).matches()) {
            toReturn = false;
        }
        if (to.getText().toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(
                to.getText().toString()).matches()) {
            toReturn = false;
        }
        if (subject.getText().toString().trim().isEmpty()) {
            toReturn = false;
        }
        if (body.getText().toString().trim().isEmpty()) {
            toReturn = false;
        }
        return toReturn;
    }


    @OnClick(R.id.send_button)
    void sendEmail() {
        if (errorChecks()) {
            try {
                Session session = Utils.getSmtpSession(this);
                // Create a default MimeMessage object.
                Message message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from.getText().toString()));

                // Set To: header field of the header.
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to.getText().toString()));

                // Set Subject: header field
                message.setSubject(encrypt(subject.getText().toString()));

                getPublicKey(to.getText().toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(response -> {
                            if (response.code() == 200) {
                                Log.d("tag",response.body().toString());
                                Log.d("tag",response.message());
                            }
                        }, Throwable::printStackTrace);
                // Now set the actual message
                message.setText(encrypt(body.getText().toString()));
                // Send message
                Observable.just(1)
                        .observeOn(Schedulers.io())
                        .subscribe(integer -> {
                            try {
                                Transport.send(message);
                                System.out.println("Sent message successfully....");
                                Snackbar.make(from, "Sent mail successfully", Snackbar.LENGTH_LONG).show();
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                        }, Throwable::printStackTrace);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String encrypt(String s) {
        secretKey = AESEncryption.getSecretEncryptionKey();
        return AESEncryption.encryptText(s, secretKey);
    }

    public Single<Response> getPublicKey(String email) {
        return Single.create(singleSubscriber -> {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "q={\"email_id\":\"" + email + "\"}");
            Request request = new Request.Builder()
                    .url("https://pgpusers-af24.restdb.io/rest/user-details-test?q=%7B%22field%22%3A%22value%22%7D")
                    .get()
                    .addHeader("content-type", "application/json")
                    .addHeader("x-apikey", "9b63be897efd04ca200d56700a7f1bdc93247")
                    .addHeader("cache-control", "no-cache")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                singleSubscriber.onSuccess(response);
            } catch (IOException e) {
                singleSubscriber.onError(e);
                e.printStackTrace();
            }
        });
    }
}
