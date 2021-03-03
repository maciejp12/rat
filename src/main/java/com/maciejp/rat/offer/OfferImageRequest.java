package com.maciejp.rat.offer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfferImageRequest {

    private String encodedFile;

    private String fileType;

    public OfferImageRequest(@JsonProperty("file") String encodedFile,
                             @JsonProperty("filetype") String fileType) {
        this.encodedFile = encodedFile;
        this.fileType = fileType;
    }

    public String getEncodedFile() {
        return encodedFile;
    }

    public void setEncodedFile(String encodedFile) {
        this.encodedFile = encodedFile;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "OfferImageRequest{" +
                "encodedFile='" + encodedFile + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
