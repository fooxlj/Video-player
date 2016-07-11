package com.myapp.fooxlj.video.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Created by fooxlj on 23/04/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "thumbnailURL",
        "date_publication",
        "date_watched"
})

public class VideoItem {
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }
    @JsonProperty("thumbnailURL")
    public String getThumbnailURL() {
        return thumbnailURL;
    }
    @JsonProperty("thumbnailURL")
    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
    @JsonProperty("id")
    public String getId() {
        return id;
    }
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }
    @JsonProperty("date_publication")
    public String getDate_publication() {
        return date_publication;
    }
    @JsonProperty("date_publication")
    public void setDate_publication(String date_publication) {
        this.date_publication = date_publication;
    }
    @JsonProperty("date_watched")
    public String getDate_watched() {
        return date_watched;
    }
    @JsonProperty("date_watched")
    public void setDate_watched(String date_watched) {
        this.date_watched = date_watched;
    }

    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("thumbnailURL")
    private String thumbnailURL;
    @JsonProperty("date_publication")
    private String date_publication;
    @JsonProperty("date_watched")
    private String date_watched;


}
