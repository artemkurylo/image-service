package com.artemkurylo.imageservice.rest.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Data
public class Image extends BaseClass {
    @ManyToOne
    private Account account;
    @ManyToOne
    private Tag tag;
    private String name;
    private String contentType;
    private Long size;
    private String resourceUrl;
}
