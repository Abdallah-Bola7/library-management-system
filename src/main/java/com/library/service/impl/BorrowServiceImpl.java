package com.library.service.impl;

import com.library.dto.BorrowRecordDTO;
import com.library.mapper.BorrowRecordMapper;
import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.Member;
import com.library.model.User;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import com.library.repository.UserRepository;
import com.library.service.BookService;
import com.library.service.BorrowService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class BorrowServiceImpl implements BorrowService {

    private static final Logger log = LoggerFactory.getLogger(BorrowServiceImpl.class);
    private static final int MAX_BORROW_DAYS = 30;
    private static final int MAX_BOOKS_PER_MEMBER = 5;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Override
    public BorrowRecordDTO borrowBook(Long bookId, Long memberId, Long issuedById) {
        log.info("Starting borrow process for book ID: {}, member ID: {}, issued by ID: {}", bookId, memberId, issuedById);

        // Validate book availability
        if (!bookService.isBookAvailable(bookId).orElse(false)) {
            log.warn("Book ID: {} is not available for borrowing", bookId);
            throw new IllegalStateException("Book is not available for borrowing");
        }

        // Check member's borrowing eligibility
        if (hasMemberReachedBorrowingLimit(memberId)) {
            log.warn("Member ID: {} has reached maximum borrowing limit of {} books", memberId, MAX_BOOKS_PER_MEMBER);
            throw new IllegalStateException("Member has reached maximum borrowing limit");
        }

        if (hasOverdueBooks(memberId)) {
            log.warn("Member ID: {} has overdue books", memberId);
            throw new IllegalStateException("Member has overdue books");
        }

        log.debug("Fetching entities from database");
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    log.error("Book not found with ID: {}", bookId);
                    return new EntityNotFoundException("Book not found");
                });
        
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("Member not found with ID: {}", memberId);
                    return new EntityNotFoundException("Member not found");
                });
        
        User issuedBy = userRepository.findById(issuedById)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", issuedById);
                    return new EntityNotFoundException("User not found");
                });

        LocalDateTime now = LocalDateTime.now();
        BorrowRecord borrowRecord = new BorrowRecord(
                book,
                member,
                now,
                now.plusDays(MAX_BORROW_DAYS),
                issuedBy
        );

        log.debug("Saving borrow record to database");
        borrowRecord = borrowRecordRepository.save(borrowRecord);
        
        log.debug("Updating book availability");
        bookService.updateBookAvailability(bookId, -1);

        log.info("Successfully created borrow record with ID: {} for book: '{}', borrowed by member: {}", 
                borrowRecord.getId(), book.getTitle(), member.getFirstName() + " " + member.getLastName());
        
        return borrowRecordMapper.toDTO(borrowRecord);
    }

    @Override
    public BorrowRecordDTO returnBook(Long borrowId, Long returnedToId) {
        log.info("Starting return process for borrow record ID: {}, returned to ID: {}", borrowId, returnedToId);

        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> {
                    log.error("Borrow record not found with ID: {}", borrowId);
                    return new EntityNotFoundException("Borrow record not found");
                });

        if (borrowRecord.getStatus() != BorrowRecord.BorrowStatus.BORROWED) {
            log.warn("Cannot return book with borrow ID: {} as its status is: {}", borrowId, borrowRecord.getStatus());
            throw new IllegalStateException("Book is not in borrowed state");
        }

        User returnedTo = userRepository.findById(returnedToId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", returnedToId);
                    return new EntityNotFoundException("User not found");
                });

        log.debug("Updating borrow record with return information");
        borrowRecord.setReturnDate(LocalDateTime.now());
        borrowRecord.setStatus(BorrowRecord.BorrowStatus.RETURNED);
        borrowRecord.setReturnedTo(returnedTo);

        borrowRecord = borrowRecordRepository.save(borrowRecord);
        
        log.debug("Updating book availability");
        bookService.updateBookAvailability(borrowRecord.getBook().getId(), 1);

        log.info("Successfully processed return for book: '{}', returned by member: {}", 
                borrowRecord.getBook().getTitle(), 
                borrowRecord.getMember().getFirstName() + " " + borrowRecord.getMember().getLastName());

        return borrowRecordMapper.toDTO(borrowRecord);
    }

    @Override
    public BorrowRecordDTO getBorrowRecord(Long id) {
        log.debug("Fetching borrow record with ID: {}", id);
        BorrowRecord borrowRecord = borrowRecordRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Borrow record not found with ID: {}", id);
                    return new EntityNotFoundException("Borrow record not found");
                });
        return borrowRecordMapper.toDTO(borrowRecord);
    }

    @Override
    public Page<BorrowRecordDTO> getMemberBorrowHistory(Long memberId, Pageable pageable) {
        log.debug("Fetching borrow history for member ID: {}", memberId);
        return borrowRecordRepository.findByMemberId(memberId, pageable)
                .map(borrowRecordMapper::toDTO);
    }

    @Override
    public Page<BorrowRecordDTO> getBookBorrowHistory(Long bookId, Pageable pageable) {
        log.debug("Fetching borrow history for book ID: {}", bookId);
        return borrowRecordRepository.findByBookId(bookId, pageable)
                .map(borrowRecordMapper::toDTO);
    }

    @Override
    public List<BorrowRecordDTO> getOverdueBooks(LocalDateTime asOf) {
        log.debug("Fetching overdue books as of: {}", asOf);
        return borrowRecordRepository.findOverdueBooks(asOf).stream()
                .map(borrowRecordMapper::toDTO)
                .toList();
    }

    @Override
    public boolean hasMemberReachedBorrowingLimit(Long memberId) {
        log.debug("Checking if member ID: {} has reached borrowing limit", memberId);
        Long currentBorrowings = borrowRecordRepository.countCurrentBorrowings(memberId);
        boolean hasReachedLimit = currentBorrowings >= MAX_BOOKS_PER_MEMBER;
        
        if (hasReachedLimit) {
            log.debug("Member ID: {} has reached borrowing limit. Current borrowings: {}", memberId, currentBorrowings);
        }
        
        return hasReachedLimit;
    }

    @Override
    public boolean hasOverdueBooks(Long memberId) {
        log.debug("Checking if member ID: {} has overdue books", memberId);
        List<BorrowRecord> currentBorrowings = borrowRecordRepository.findCurrentBorrowings(memberId);
        LocalDateTime now = LocalDateTime.now();
        boolean hasOverdue = currentBorrowings.stream()
                .anyMatch(br -> br.getDueDate().isBefore(now));
        
        if (hasOverdue) {
            log.debug("Member ID: {} has overdue books", memberId);
        }
        
        return hasOverdue;
    }

    @Override
    public void extendBorrowPeriod(Long borrowId, LocalDateTime newDueDate) {
        log.info("Starting borrow period extension for borrow ID: {} to new due date: {}", borrowId, newDueDate);

        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> {
                    log.error("Borrow record not found with ID: {}", borrowId);
                    return new EntityNotFoundException("Borrow record not found");
                });

        if (borrowRecord.getStatus() != BorrowRecord.BorrowStatus.BORROWED) {
            log.warn("Cannot extend period for borrow ID: {} as its status is: {}", borrowId, borrowRecord.getStatus());
            throw new IllegalStateException("Cannot extend period for non-borrowed book");
        }

        if (borrowRecord.getDueDate().isBefore(LocalDateTime.now())) {
            log.warn("Cannot extend period for overdue book. Borrow ID: {}, Current due date: {}", 
                    borrowId, borrowRecord.getDueDate());
            throw new IllegalStateException("Cannot extend period for overdue book");
        }

        long daysToExtend = ChronoUnit.DAYS.between(borrowRecord.getDueDate(), newDueDate);
        if (daysToExtend > MAX_BORROW_DAYS) {
            log.warn("Extension period ({} days) exceeds maximum allowed days ({})", daysToExtend, MAX_BORROW_DAYS);
            throw new IllegalStateException("Extension period exceeds maximum allowed days");
        }

        log.debug("Updating due date from {} to {}", borrowRecord.getDueDate(), newDueDate);
        borrowRecord.setDueDate(newDueDate);
        borrowRecordRepository.save(borrowRecord);

        log.info("Successfully extended borrow period for book: '{}', borrowed by member: {}", 
                borrowRecord.getBook().getTitle(), 
                borrowRecord.getMember().getFirstName() + " " + borrowRecord.getMember().getLastName());
    }

    @Override
    public Page<BorrowRecordDTO> getAllBorrowRecords(Pageable pageable) {
        log.debug("Fetching all borrow records with pagination");
        return borrowRecordRepository.findAll(pageable)
                .map(borrowRecordMapper::toDTO);
    }

    @Override
    public Page<BorrowRecordDTO> getCurrentBorrowings(Pageable pageable) {
        log.debug("Fetching current borrowings with pagination");
        return borrowRecordRepository.findByStatus(BorrowRecord.BorrowStatus.BORROWED, pageable)
                .map(borrowRecordMapper::toDTO);
    }

    @Override
    public Page<BorrowRecordDTO> getOverdueBorrowings(Pageable pageable) {
        log.debug("Fetching overdue borrowings with pagination");
        LocalDateTime now = LocalDateTime.now();
        return borrowRecordRepository.findOverdueBorrowings(now, pageable)
                .map(borrowRecordMapper::toDTO);
    }
} 