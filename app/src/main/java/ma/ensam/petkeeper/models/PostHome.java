package ma.ensam.petkeeper.models;

public class PostHome {
    private String userName;
    private String description;
    private String pet;
    private String type;
    private String from;
    private String to;
    private String gender;
    private String duration;

    public PostHome(String userName, String pet, String type, String from,String to,String description,  String gender, String duration) {
        this.userName = userName;
        this.pet = pet;
        this.type = type;
        this.from = from;
        this.to = to;
        this.gender = gender;
        this.duration = duration;
        this.description = description;
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

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
