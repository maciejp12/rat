package com.maciejp.rat.offer;

public class OfferValidator {

    private final int minTitleLength = 3;

    private final int maxTitleLength = 100;

    private final int minDescriptionLength = 0;

    private final int maxDescriptionLength = 300;

    public boolean validateTitleLength(String title) {
        int len = title.length();
        return len >= minTitleLength && len <= maxTitleLength;
    }

    public boolean validateDescriptionLength(String description) {
        int len = description.length();
        return len >= minDescriptionLength && len <= maxDescriptionLength;
    }

    public boolean validatePrice(Float price) {
        return price > 0.f && price < Math.pow(10, 13);
    }

    public String getTitleErrorMessage() {
        return "Title must be between " + minTitleLength + " and " + maxTitleLength + " characters long";
    }

    public String getDescriptionErrorMessage() {
        return "Description must be between " + minDescriptionLength + " and " + maxDescriptionLength + " characters long";
    }

    public String getPriceErrorMessage() {
        return "Price must be greater than 0 and lesser than " + Math.pow(10, 13);
    }
}
