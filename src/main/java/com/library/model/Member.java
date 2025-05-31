package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "members")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    private String phone;
    private String address;

    @Column(unique = true)
    private String membershipNumber;

    @Enumerated(EnumType.STRING)
    private MembershipStatus status = MembershipStatus.ACTIVE;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<BorrowRecord> borrowRecords = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum MembershipStatus {
        ACTIVE,
        SUSPENDED,
        EXPIRED
    }

    public Member(String firstName, String lastName, String email, String membershipNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.membershipNumber = membershipNumber;
    }
} 