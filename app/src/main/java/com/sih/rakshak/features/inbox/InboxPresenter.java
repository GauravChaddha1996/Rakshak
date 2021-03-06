package com.sih.rakshak.features.inbox;

import android.content.Context;
import android.util.Log;

import com.sih.rakshak.features.CONSTANTS;
import com.sih.rakshak.features.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;

import rx.Single;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public class InboxPresenter implements InboxPresenterVI {

    private final Context context;
    InboxVI vi;

    public InboxPresenter(Context context, InboxVI vi) {
        this.context = context;
        this.vi = vi;
    }


    @Override
    public Store getStore() {
        try {
            return Utils.getSession(context).getStore("imaps");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Single<List<Message>> fetchObservable() {
        return Single.create(singleSubscriber -> {
            try {
                Store store = getStore();
                Log.d("tag", CONSTANTS.getUsername(context));
                Log.d("tag", CONSTANTS.getPassword(context));
                store.connect(CONSTANTS.getImapHost(context), CONSTANTS.getUsername(context), CONSTANTS.getPassword(context));
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
                if (messageCount > 10) {
                    messages = inbox.getMessages((vi.getPageNumber() * 10) - 9, vi.getPageNumber() * 10);
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

    public Single<List<Message>> fetchMoreObservable() {
        return Single.create(singleSubscriber -> {
            try {
                vi.showProgressBar();
                Store store = getStore();
                store.connect(CONSTANTS.getImapHost(context), CONSTANTS.getUsername(context), CONSTANTS.getPassword(context));
                Folder inbox = store.getFolder("inbox");
                Folder bin = store.getFolder("Trash");
                if (!bin.exists()) {
                    bin.create(Folder.HOLDS_MESSAGES);
                }
                inbox.open(Folder.READ_WRITE);
                int messageCount = inbox.getMessageCount();
                Log.d("InboxPresenter", String.valueOf(messageCount));
                List<Message> list;
                Message[] messages = null;
                if (messageCount > 10) {
                    int start = (vi.getPageNumber() * 10) - 9;
                    int end = vi.getPageNumber() * 10;
                    if (end > messageCount) {
                        end = messageCount;
                    }
                    if (end >= start) {
                        messages = (Message[]) inbox.getMessages(start, end);
                    } else {
                        singleSubscriber.onError(new Throwable("End of list"));
                    }
                } else {
                    messages = (Message[]) inbox.getMessages();
                }
                list = new ArrayList<>();
                if (messages != null) {
                    for (Message message : messages) {
                        list.add(message);
                        message.getSubject();
                        message.getFrom();
                        message.getReceivedDate();
                    }
                    singleSubscriber.onSuccess(list);
                }

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
                    store1.connect(CONSTANTS.getImapHost(context), CONSTANTS.getUsername(context), CONSTANTS.getPassword(context));
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
