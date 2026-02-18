package com.atelier.atelierstore.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass

public abstract class BaseItem {
    @Id
    private String id;
    private String name;

    protected BaseItem() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    protected BaseItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract void displayInfo();
}
