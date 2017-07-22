package com.appspot.reservandeat_171704.backend.mensajeria;

import java.util.List;
import java.util.Map;

/**
 * @author Gilberto Pacheco Gallegos
 */
public class Mensaje {

    private List<String> registration_ids;
    private Map<String, String> data;

    public Mensaje() {
    }

    public Mensaje(List<String> registration_ids, Map<String, String> data) {
        this.registration_ids = registration_ids;
        this.data = data;
    }

    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
