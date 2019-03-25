package ch.supsi.dti.isin.meteoapp.model;

import java.util.UUID;

public class Location {
    private UUID Id;
    private String mName;
    private String mIcon;
    private String mDescription;
    private double mtemp;
    private double mtemp_min;
    private double mtemp_max;

    public Location() {
        Id = UUID.randomUUID();
    }

    public Location(UUID id, String mName) {
        Id = id;
        this.mName = mName;
    }

    public double getMtemp() {
        return mtemp;
    }

    public void setMtemp(double mtemp) {
        this.mtemp = mtemp;
    }

    public double getMtemp_min() {
        return mtemp_min;
    }

    public void setMtemp_min(double mtemp_min) {
        this.mtemp_min = mtemp_min;
    }

    public double getMtemp_max() {
        return mtemp_max;
    }

    public void setMtemp_max(double mtemp_max) {
        this.mtemp_max = mtemp_max;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}