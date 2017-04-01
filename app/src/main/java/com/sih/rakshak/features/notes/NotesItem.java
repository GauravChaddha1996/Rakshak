package com.sih.rakshak.features.notes;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ManikantaInugurthi on 01-04-2017.
 */

public class NotesItem extends RealmObject {

    @PrimaryKey
    private long id;
    private String title;
    private String description;
    private String lastViewed;

    public NotesItem() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastViewed() {
        return lastViewed;
    }

    public void setLastViewed(String lastViewed) {
        this.lastViewed = lastViewed;
    }
}
