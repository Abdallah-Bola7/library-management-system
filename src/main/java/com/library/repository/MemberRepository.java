package com.library.repository;

import com.library.model.Member;
import com.library.model.Member.MembershipStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMembershipNumber(String membershipNumber);
    
    Optional<Member> findByEmail(String email);
    
    Page<Member> findByStatus(MembershipStatus status, Pageable pageable);
    
    @Query("SELECT m FROM Member m WHERE m.firstName LIKE %:name% OR m.lastName LIKE %:name%")
    Page<Member> findByName(@Param("name") String name, Pageable pageable);
    
    @Query("SELECT m FROM Member m JOIN m.borrowRecords br WHERE br.status = 'OVERDUE'")
    List<Member> findMembersWithOverdueBooks();
    
    @Query("SELECT COUNT(br) FROM Member m JOIN m.borrowRecords br WHERE m.id = :memberId AND br.status = 'BORROWED'")
    Long countCurrentBorrowings(@Param("memberId") Long memberId);
    
    Boolean existsByEmail(String email);
    
    Boolean existsByMembershipNumber(String membershipNumber);
} 