package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_records")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    private LocalDateTime borrowDate;

    @NotNull
    private LocalDateTime dueDate;

    private LocalDateTime returnDate;

    @Enumerated(EnumType.STRING)
    private BorrowStatus status = BorrowStatus.BORROWED;

    @ManyToOne
    @JoinColumn(name = "issued_by_id")
    private User issuedBy;

    @ManyToOne
    @JoinColumn(name = "returned_to_id")
    private User returnedTo;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum BorrowStatus {
        BORROWED,
        RETURNED,
        OVERDUE,
        LOST
    }

    public BorrowRecord(Book book, Member member, LocalDateTime borrowDate, LocalDateTime dueDate, User issuedBy) {
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.issuedBy = issuedBy;
    }
} 