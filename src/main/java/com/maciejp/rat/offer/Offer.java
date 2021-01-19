package com.maciejp.rat.offer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Offer {

    private final long id;

    private String title;

    private String description;

    private float price;

    private String creator;

    private Timestamp creationDate;

    public Offer(@JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY) long id,
                 @JsonProperty("title") String title,
                 @JsonProperty("description") String description,
                 @JsonProperty("price") float price,
                 @JsonProperty(value = "creator", access = JsonProperty.Access.READ_ONLY) String creator,
                 @JsonProperty(value = "creation_date", access = JsonProperty.Access.READ_ONLY) Timestamp timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.creator = creator;
        this.creationDate = timestamp;
    }

    public long getId() {
        return id;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", creator='" + creator + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
