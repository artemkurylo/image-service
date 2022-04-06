package com.artemkurylo.imageservice.rest.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@ToString(callSuper = true)
@Data
public class Account extends BaseClass {
    @NotBlank(message = "Name field cannot be blank")
    private String fullName;
    @Email(message = "Email field cannot be blank")
    private String email;
    @OneToMany
    private List<Image> imageList = new ArrayList<>();
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creationDate;
    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;
}
