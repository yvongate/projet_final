package com.uici.classmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

public class POI implements Parcelable {

    private long id;
    private String nom;
    private String type;
    private double latitude;
    private double longitude;
    private String description;
    private long classeId;

    public POI(String nom, String type, double latitude, double longitude, String description, long classeId) {
        this.nom = nom;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.classeId = classeId;
    }

    protected POI(Parcel in) {
        id = in.readLong();
        nom = in.readString();
        type = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        description = in.readString();
        classeId = in.readLong();
    }

    public static final Creator<POI> CREATOR = new Creator<POI>() {
        @Override
        public POI createFromParcel(Parcel in) {
            return new POI(in);
        }

        @Override
        public POI[] newArray(int size) {
            return new POI[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nom);
        dest.writeString(type);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(description);
        dest.writeLong(classeId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getClasseId() {
        return classeId;
    }

    public void setClasseId(long classeId) {
        this.classeId = classeId;
    }
}
