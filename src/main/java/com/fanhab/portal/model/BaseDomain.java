package com.fanhab.portal.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Base domain (manage createdOn and updatedOn fields).
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
@SuperBuilder(toBuilder = true)
public abstract class BaseDomain {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable = false , name = "ID")
    Long id;

    @Column(nullable = true , name = "IS_DELETED")
    Boolean isDeleted = false;

    @Version
    Long version= 0L;


    /** Data created on. */

    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdOn = LocalDateTime.now();

    /** Data updated on. */

    @Column(name = "UPDATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedOn = LocalDateTime.now();

}
