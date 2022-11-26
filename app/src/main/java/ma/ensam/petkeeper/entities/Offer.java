package ma.ensam.petkeeper.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import ma.ensam.petkeeper.entities.enums.OfferType;
import ma.ensam.petkeeper.entities.enums.PetSpecies;

@Entity(tableName = "offers")
public class Offer {

    @PrimaryKey(autoGenerate = true)
    private long offerId;

    private OfferType type;

    private PetSpecies pet;

    private String title;

    private String description;

    private String image_url;

    private Date fromDate;

    private Date toDate;

    private Date creationDate;

    private long profileCreatorId;

    public Offer(OfferType type, PetSpecies pet, String title, String description, String image_url,
                 Date fromDate, Date toDate, Date creationDate, long profileCreatorId) {
        this.type = type;
        this.pet = pet;
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.creationDate = creationDate;
        this.profileCreatorId = profileCreatorId;
    }

    public Offer(long offerId, OfferType type, PetSpecies pet, String title, String description, String image_url, Date fromDate, Date toDate, Date creationDate, long profileCreatorId, Profile creator) {
        this.offerId = offerId;
        this.type = type;
        this.pet = pet;
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.creationDate = creationDate;
        this.profileCreatorId = profileCreatorId;
    }

    public long getOfferId() {
        return offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public long getId() {
        return offerId;
    }

    public void setId(long id) {
        this.offerId = id;
    }

    public OfferType getType() {
        return type;
    }

    public void setType(OfferType type) {
        this.type = type;
    }

    public PetSpecies getPet() {
        return pet;
    }

    public void setPet(PetSpecies pet) {
        this.pet = pet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getProfileCreatorId() {
        return profileCreatorId;
    }

    public void setProfileCreatorId(long profileCreatorId) {
        this.profileCreatorId = profileCreatorId;
    }
}
