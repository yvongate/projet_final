package com.uici.classmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Classe implements Parcelable {
    private long id;
    private String nom;
    private String anneeAcademique;
    private String description;
    private long enseignantId;

    public Classe(long id, String nom, String anneeAcademique, String description, long enseignantId) {
        this.id = id;
        this.nom = nom;
        this.anneeAcademique = anneeAcademique;
        this.description = description;
        this.enseignantId = enseignantId;
    }

    public Classe(String nom, String anneeAcademique, String description, long enseignantId) {
        this.nom = nom;
        this.anneeAcademique = anneeAcademique;
        this.description = description;
        this.enseignantId = enseignantId;
    }

    protected Classe(Parcel in) {
        id = in.readLong();
        nom = in.readString();
        anneeAcademique = in.readString();
        description = in.readString();
        enseignantId = in.readLong();
    }

    public static final Creator<Classe> CREATOR = new Creator<Classe>() {
        @Override
        public Classe createFromParcel(Parcel in) {
            return new Classe(in);
        }

        @Override
        public Classe[] newArray(int size) {
            return new Classe[size];
        }
    };

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

    public String getAnneeAcademique() {
        return anneeAcademique;
    }

    public void setAnneeAcademique(String anneeAcademique) {
        this.anneeAcademique = anneeAcademique;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getEnseignantId() {
        return enseignantId;
    }

    public void setEnseignantId(long enseignantId) {
        this.enseignantId = enseignantId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nom);
        dest.writeString(anneeAcademique);
        dest.writeString(description);
        dest.writeLong(enseignantId);
    }
}
