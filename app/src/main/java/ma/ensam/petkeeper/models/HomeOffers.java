package ma.ensam.petkeeper.models;

import java.util.Date;

import ma.ensam.petkeeper.entities.enums.OfferType;
import ma.ensam.petkeeper.entities.enums.PetSpecies;

public class HomeOffers {
    private String profileUrl;
    private String userName;
    private Date creationDate;
    private String description;
    private String title;
    private PetSpecies pet;
    private OfferType type;
    private Date from;
    private Date to;
    private Long offerId;
    private Long profileId;


    public HomeOffers(Long offerId,Long profileId,String userName, String description,String title, PetSpecies pet, OfferType type, Date from, Date to, String profileUrl, Date creationDate) {
        this.offerId = offerId;
        this.profileId = profileId;
        this.userName = userName;
        this.pet = pet;
        this.type = type;
        this.from = from;
        this.to = to;
        this.description = description;
        this.title = title;
        this.profileUrl = profileUrl;
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PetSpecies getPet() {
        return pet;
    }

    public void setPet(PetSpecies pet) {
        this.pet = pet;
    }

    public OfferType getType() {
        return type;
    }

    public void setType(OfferType type) {
        this.type = type;
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

    public Long getOfferId() {
        return this.offerId;
    }
    public void setOfferId(Long offerId){
        this.offerId = offerId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
