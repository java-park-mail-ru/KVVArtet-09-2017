package project.gamemechanics.services.databaseModels;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BagDatabaseModel {
    private Integer id;
    private String name;
    private String description;
    private List<SlotImpl> slotImpl = new ArrayList<>();

    public BagDatabaseModel(@NotNull Integer id, @NotNull String name, @NotNull String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public BagDatabaseModel(@NotNull String name, @NotNull String description) {
        this.id = 0;
        this.name = name;
        this.description = description;
    }

    BagDatabaseModel(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<SlotImpl> getSlotImpl() {
        return slotImpl;
    }
}
