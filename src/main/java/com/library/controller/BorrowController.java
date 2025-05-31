package com.library.controller;

import com.library.dto.BorrowRecordDTO;
import com.library.security.UserPrincipal;
import com.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/borrow")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @PostMapping("/{bookId}/member/{memberId}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<BorrowRecordDTO> borrowBook(
            @PathVariable Long bookId,
            @PathVariable Long memberId,
            Authentication authentication) {
        Long issuedById = getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(borrowService.borrowBook(bookId, memberId, issuedById));
    }

    @PutMapping("/{borrowId}/return")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<BorrowRecordDTO> returnBook(
            @PathVariable Long borrowId,
            Authentication authentication) {
        Long returnedToId = getUserIdFromAuthentication(authentication);
        return ResponseEntity.ok(borrowService.returnBook(borrowId, returnedToId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<BorrowRecordDTO> getBorrowRecord(@PathVariable Long id) {
        return ResponseEntity.ok(borrowService.getBorrowRecord(id));
    }

    @GetMapping("/member/{memberId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Page<BorrowRecordDTO>> getMemberBorrowHistory(
            @PathVariable Long memberId,
            Pageable pageable) {
        return ResponseEntity.ok(borrowService.getMemberBorrowHistory(memberId, pageable));
    }

    @GetMapping("/book/{bookId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Page<BorrowRecordDTO>> getBookBorrowHistory(
            @PathVariable Long bookId,
            Pageable pageable) {
        return ResponseEntity.ok(borrowService.getBookBorrowHistory(bookId, pageable));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<List<BorrowRecordDTO>> getOverdueBooks(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime asOf) {
        if (asOf == null) {
            asOf = LocalDateTime.now();
        }
        return ResponseEntity.ok(borrowService.getOverdueBooks(asOf));
    }

    @PutMapping("/{borrowId}/extend")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Void> extendBorrowPeriod(
            @PathVariable Long borrowId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDueDate) {
        borrowService.extendBorrowPeriod(borrowId, newDueDate);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/member/{memberId}/limit-reached")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> hasMemberReachedBorrowingLimit(@PathVariable Long memberId) {
        return ResponseEntity.ok(borrowService.hasMemberReachedBorrowingLimit(memberId));
    }

    @GetMapping("/member/{memberId}/has-overdue")
    @PreAuthorize("hasRole('STAFF') or hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> hasOverdueBooks(@PathVariable Long memberId) {
        return ResponseEntity.ok(borrowService.hasOverdueBooks(memberId));
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId();
    }
} 