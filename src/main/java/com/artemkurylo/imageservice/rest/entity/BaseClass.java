package com.artemkurylo.imageservice.rest.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@Data
public class BaseClass {
    @Id
    @Type(type="uuid-char")
    private UUID uuid = UUID.randomUUID();
}
