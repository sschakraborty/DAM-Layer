package com.sschakraborty.platform.damlayer.core.configuration.builder.model;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditField;
import com.sschakraborty.platform.damlayer.audit.annotation.AuditResource;
import com.sschakraborty.platform.damlayer.audit.core.AuditModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PARCEL")
@AuditResource
public class Parcel implements AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @AuditField(identifier = true)
    private int id;

    @Column(name = "FROM_ADDRESS", nullable = false)
    @AuditField(identifier = true)
    private String fromAddress;

    @Column(name = "TO_ADDRESS", nullable = false)
    @AuditField(identifier = true)
    private String toAddress;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parcel")
    @AuditField
    private List<Item> items;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @AuditField
    private Item primaryItem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Item getPrimaryItem() {
        return primaryItem;
    }

    public void setPrimaryItem(Item primaryItem) {
        this.primaryItem = primaryItem;
    }
}