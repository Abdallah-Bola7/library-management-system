package com.library.service;

import com.library.dto.BorrowRecordDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowService {
    BorrowRecordDTO borrowBook(Long bookId, Long memberId, Long issuedById);
    BorrowRecordDTO returnBook(Long borrowId, Long returnedToId);
    BorrowRecordDTO getBorrowRecord(Long id);
    Page<BorrowRecordDTO> getMemberBorrowHistory(Long memberId, Pageable pageable);
    Page<BorrowRecordDTO> getBookBorrowHistory(Long bookId, Pageable pageable);
    List<BorrowRecordDTO> getOverdueBooks(LocalDateTime asOf);
    boolean hasMemberReachedBorrowingLimit(Long memberId);
    boolean hasOverdueBooks(Long memberId);
    void extendBorrowPeriod(Long borrowId, LocalDateTime newDueDate);
} 