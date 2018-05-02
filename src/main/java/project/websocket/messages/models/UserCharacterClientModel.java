package project.websocket.messages.models;

@SuppressWarnings("unused")
public class UserCharacterClientModel {
    private String name;
    private String description;
    private String race;
    private String role;
    private Integer level;
    private Integer charIndexInCharist;

    public UserCharacterClientModel(String name, String description, String race, String role, Integer level) {
        this.name = name;
        this.description = description;
        this.race = race;
        this.role = role;
        this.level = level;
    }

    public UserCharacterClientModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCharIndexInCharist() {
        return charIndexInCharist;
    }

    public void setCharIndexInCharist(Integer charIndexInCharist) {
        this.charIndexInCharist = charIndexInCharist;
    }
}
