package com.sschakraborty.platform.damlayer.core.configuration.builder.model;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditField;
import com.sschakraborty.platform.damlayer.audit.annotation.AuditResource;
import com.sschakraborty.platform.damlayer.core.Model;

import javax.persistence.*;

@Entity
@Table(name = "ITEM")
@AuditResource
public class Item implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @AuditField(identifier = true)
    private int id;

    @Column(name = "NAME")
    @AuditField(identifier = true)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARCEL_ID", nullable = false)
    @AuditField
    private Parcel parcel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }
}