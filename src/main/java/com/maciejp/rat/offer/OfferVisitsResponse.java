package com.maciejp.rat.offer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OfferVisitsResponse {

    private Integer visits;

    public OfferVisitsResponse(@JsonProperty("visits") Integer visits) {
        this.visits = visits;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }
}
