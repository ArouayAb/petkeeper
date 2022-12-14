package ma.ensam.petkeeper.models;

import java.util.Date;

public class OfferProfile {
    private long id;
    private String OfferType;
    private String profileUrl;
    private String userName;
    private Date creationDate;
    private String titre;
    private String petSpecies;
    private Date from;
    private Date to;

    public OfferProfile(long id, String offerType, String profileUrl, String userName, Date creationDate, String titre, String petSpecies, Date from, Date to) {
        this.id = id;
        OfferType = offerType;
        this.profileUrl = profileUrl;
        this.userName = userName;
        this.creationDate = creationDate;
        this.titre = titre;
        this.petSpecies = petSpecies;
        this.from = from;
        this.to = to;
    }

    public String getOfferType() {
        return OfferType;
    }

    public void setOfferType(String offerType) {
        OfferType = offerType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getPetSpecies() {
        return petSpecies;
    }

    public void setPetSpecies(String petSpecies) {
        this.petSpecies = petSpecies;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}