package com.library.repository;

import com.library.model.BorrowRecord;
import com.library.model.BorrowRecord.BorrowStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    Page<BorrowRecord> findByMemberId(Long memberId, Pageable pageable);
    
    Page<BorrowRecord> findByBookId(Long bookId, Pageable pageable);
    
    List<BorrowRecord> findByStatus(BorrowStatus status);
    
    @Query("SELECT br FROM BorrowRecord br WHERE br.dueDate < :now AND br.status = 'BORROWED'")
    List<BorrowRecord> findOverdueBooks(@Param("now") LocalDateTime now);
    
    @Query("SELECT br FROM BorrowRecord br WHERE br.member.id = :memberId AND br.status = 'BORROWED'")
    List<BorrowRecord> findCurrentBorrowings(@Param("memberId") Long memberId);
    
    @Query("SELECT COUNT(br) FROM BorrowRecord br WHERE br.member.id = :memberId AND br.status = 'BORROWED'")
    Long countCurrentBorrowings(@Param("memberId") Long memberId);
    
    @Query("SELECT COUNT(br) FROM BorrowRecord br WHERE br.book.id = :bookId AND br.status = 'BORROWED'")
    Long countCurrentBorrowingsForBook(@Param("bookId") Long bookId);
    
    @Query("SELECT br FROM BorrowRecord br WHERE br.dueDate BETWEEN :start AND :end")
    List<BorrowRecord> findBorrowingsDueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT br FROM BorrowRecord br WHERE br.member.id = :memberId AND br.book.id = :bookId AND br.status = 'BORROWED'")
    List<BorrowRecord> findActiveBorrowingByMemberAndBook(@Param("memberId") Long memberId, @Param("bookId") Long bookId);
} 