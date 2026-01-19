package com.uici.classmanager.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Annonce implements Parcelable {

    public static final String TYPE_AUCUNE = "AUCUNE";
    public static final String TYPE_IMAGE = "IMAGE";
    public static final String TYPE_AUDIO = "AUDIO";
    public static final String TYPE_VIDEO = "VIDEO";

    private long id;
    private String titre;
    private String contenu;
    private String datePublication;
    private long classeId;
    private String typeRessource;
    private String cheminRessource;

    public Annonce(String titre, String contenu, String datePublication, long classeId) {
        this.titre = titre;
        this.contenu = contenu;
        this.datePublication = datePublication;
        this.classeId = classeId;
        this.typeRessource = TYPE_AUCUNE;
        this.cheminRessource = null;
    }

    protected Annonce(Parcel in) {
        id = in.readLong();
        titre = in.readString();
        contenu = in.readString();
        datePublication = in.readString();
        classeId = in.readLong();
        typeRessource = in.readString();
        cheminRessource = in.readString();
    }

    public static final Creator<Annonce> CREATOR = new Creator<Annonce>() {
        @Override
        public Annonce createFromParcel(Parcel in) {
            return new Annonce(in);
        }

        @Override
        public Annonce[] newArray(int size) {
            return new Annonce[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titre);
        dest.writeString(contenu);
        dest.writeString(datePublication);
        dest.writeLong(classeId);
        dest.writeString(typeRessource);
        dest.writeString(cheminRessource);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(String datePublication) {
        this.datePublication = datePublication;
    }

    public long getClasseId() {
        return classeId;
    }

    public void setClasseId(long classeId) {
        this.classeId = classeId;
    }

    public String getTypeRessource() {
        return typeRessource;
    }

    public void setTypeRessource(String typeRessource) {
        this.typeRessource = typeRessource;
    }

    public String getCheminRessource() {
        return cheminRessource;
    }

    public void setCheminRessource(String cheminRessource) {
        this.cheminRessource = cheminRessource;
    }
}
