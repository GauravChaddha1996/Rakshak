package com.sih.rakshak.features.inbox;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public interface InboxPresenterVI {
    Properties getProps();
    Session getSession();
    Store getStore();

}
