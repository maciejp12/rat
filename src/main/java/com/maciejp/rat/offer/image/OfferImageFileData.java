package com.maciejp.rat.offer.image;

public class OfferImageFileData {

    private final String fileName;

    private final String imageType;

    public OfferImageFileData(String fileName, String imageType) {
        this.fileName = fileName;
        this.imageType = imageType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getImageType() {
        return imageType;
    }

    public String getFullFileName() {
        return fileName + imageType;
    }
}
