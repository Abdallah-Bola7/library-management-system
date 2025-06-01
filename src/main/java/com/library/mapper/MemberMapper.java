package com.library.mapper;

import com.library.dto.MemberDTO;
import com.library.model.Member;
import com.library.model.BorrowRecord;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    @Mapping(target = "borrowRecordIds", expression = "java(mapBorrowRecordIds(member.getBorrowRecords()))")
    MemberDTO toDTO(Member member);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "borrowRecords", ignore = true)
    Member toEntity(MemberDTO memberDTO);

    default Set<Long> mapBorrowRecordIds(Set<BorrowRecord> borrowRecords) {
        if (borrowRecords == null) {
            return null;
        }
        return borrowRecords.stream()
                .map(BorrowRecord::getId)
                .collect(Collectors.toSet());
    }
} 