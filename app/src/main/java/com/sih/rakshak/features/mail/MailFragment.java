package com.sih.rakshak.features.mail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sih.rakshak.R;
import com.sih.rakshak.base.BaseFragment;
import com.sih.rakshak.features.FragmentIds;
import com.sih.rakshak.features.HomeActivity;

import org.jsoup.Jsoup;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.MimeMultipart;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.sih.rakshak.features.FragmentIds.INBOX;
import static com.sih.rakshak.features.FragmentIds.MAIL;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public class MailFragment extends BaseFragment {

    Message message;
    @BindView(R.id.from)
    TextView fromUser;
    @BindView(R.id.cc_bcc)
    TextView ccBcc;
    @BindView(R.id.to)
    TextView toUser;
    @BindView(R.id.dateAndTime)
    TextView dateAndTime;
    @BindView(R.id.emailSubject)
    TextView emailSubject;
    @BindView(R.id.body)
    TextView emailBody;
    @BindView(R.id.loading)
    RelativeLayout loading;
    @BindView(R.id.error)
    RelativeLayout error;
    @BindView(R.id.content)
    NestedScrollView content;
    String toAddress = "", ccAddress = "", bccAddress = "", fromAddress = "", subject = "",
            body = "", dateTime = "";

    public MailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mail, container, false);
        ButterKnife.bind(this, view);
        message = ((HomeActivity) getActivity()).getMessage();
        setData();
        return view;
    }

    private void setData() {
        loading(content, loading, error);
        try {
            Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);
            Address[] ccAddresses = message.getRecipients(Message.RecipientType.CC);
            Address[] bccAddresses = message.getRecipients(Message.RecipientType.BCC);
            Address[] fromAddresses = message.getFrom();
            if (toAddresses != null) {
                for (Address address : toAddresses) {
                    toAddress += address.toString();
                }
            }
            if (ccAddresses != null) {
                for (Address address : ccAddresses) {
                    ccAddress += address.toString();
                }
            }
            if (bccAddresses != null) {
                for (Address address : bccAddresses) {
                    bccAddress += address.toString();
                }
            }
            if (fromAddresses != null) {
                for (Address address : fromAddresses) {
                    fromAddress += address.toString();
                }
            }
            subject = message.getSubject();
            dateTime = message.getReceivedDate().toString();
            getTextFromMessage(message)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(s -> {
                        body = s;
                        toUser.setText(toAddress);
                        ccBcc.setText(ccAddress + "\n" + bccAddress);
                        fromUser.setText(fromAddress);
                        emailSubject.setText(subject);
                        dateAndTime.setText(dateTime);
                        emailBody.setText(body);
                        content(content, loading, error);
                    }, throwable -> {
                        throwable.printStackTrace();
                        error(content, loading, error);
                    });
        } catch (Exception e) {
            e.printStackTrace();
            error(content, loading, error);
        }
    }

    private Single<String> getTextFromMessage(Message message) {
        return Single.create(singleSubscriber -> {
            String result = "";
            try {
                if (message.isMimeType("text/plain")) {
                    result = message.getContent().toString();
                } else if (message.isMimeType("multipart/*")) {
                    MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                    result = getTextFromMimeMultipart(mimeMultipart);
                }
            } catch (Exception e) {
                e.printStackTrace();
                singleSubscriber.onError(e);
            }
            singleSubscriber.onSuccess(result);
        });
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws Exception {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    @Override
    public FragmentIds getFragmentId() {
        return MAIL;
    }

    @Override
    public FragmentIds getBackToFragmentId() {
        return INBOX;
    }
}
