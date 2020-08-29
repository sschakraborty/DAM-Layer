package com.sschakraborty.platform.damlayer.core.configuration.builder.model;

import com.sschakraborty.platform.damlayer.audit.annotation.AuditField;
import com.sschakraborty.platform.damlayer.audit.annotation.AuditResource;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PARCEL")
@AuditResource
public class Parcel implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    @AuditField(identifier = true)
    private int id;

    @Column(name = "FROM_ADDRESS", nullable = false)
    private String fromAddress;

    @Column(name = "TO_ADDRESS", nullable = false)
    private String toAddress;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parcel")
    private List<Item> items;

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
}