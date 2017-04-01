package com.sih.rakshak.features.inbox;

import android.util.Log;

import com.sih.rakshak.features.CONSTANTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import rx.Single;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public class InboxPresenter implements InboxPresenterVI {

    InboxVI vi;

    public InboxPresenter(InboxVI vi) {
        this.vi = vi;
    }

    @Override
    public Properties getProps() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", CONSTANTS.host);
        props.put("mail.smtp.port", "587");
        return props;
    }

    @Override
    public Session getSession() {
        return Session.getInstance(getProps(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(CONSTANTS.password, CONSTANTS.password);
                    }
                });
    }

    @Override
    public Store getStore() {
        try {
            return getSession().getStore("imaps");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Single<List<Message>> fetchObservable() {
        return Single.create(singleSubscriber -> {
            try {
                Store store = getStore();
                store.connect(CONSTANTS.host, CONSTANTS.username, CONSTANTS.password);
                Folder inbox = store.getFolder("inbox");
                Folder bin = store.getFolder("Trash");
                if (!bin.exists()) {
                    bin.create(Folder.HOLDS_MESSAGES);
                }
                inbox.open(Folder.READ_WRITE);
                int messageCount = inbox.getMessageCount();
                Log.d("InboxPresenter", String.valueOf(messageCount));
                List<Message> list;
                Message[] messages;
                if (messageCount > 5) {
                    messages = inbox.getMessages((vi.getPageNumber() * 5) - 4, vi.getPageNumber() * 5);
                } else {
                    messages = inbox.getMessages();
                }
                list = new ArrayList<>();
                for (Message message : messages) {
                    list.add(message);
                    message.getSubject();
                }
                singleSubscriber.onSuccess(list);
            } catch (Exception e) {
                singleSubscriber.onError(e);
            }
        });
    }

    public Single<String> deleteObservable(Message message) {
        return Single.create(singleSubscriber -> {
            try {
                try {
                    Store store1 = getStore();
                    store1.connect(CONSTANTS.host, CONSTANTS.username, CONSTANTS.password);
                    Folder inbox1 = store1.getFolder("inbox");
                    Folder bin1 = store1.getFolder("Trash");
                    if (!bin1.exists()) {
                        bin1.create(Folder.HOLDS_MESSAGES);
                    }
                    inbox1.open(Folder.READ_WRITE);
                    inbox1.copyMessages(new Message[]{message}, bin1);
                    message.setFlag(Flags.Flag.DELETED, true);
                    inbox1.close(true);
                    store1.close();
                    singleSubscriber.onSuccess("Mail Deleted Successfully");
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                singleSubscriber.onError(new Throwable("Mail Cannot be deleted"));
            }
        });
    }

}
