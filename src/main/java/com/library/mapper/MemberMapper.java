package com.library.mapper;

import com.library.dto.MemberDTO;
import com.library.model.Member;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "borrowRecordIds", expression = "java(member.getBorrowRecords() == null ? null : member.getBorrowRecords().stream().map(record -> record.getId()).collect(java.util.stream.Collectors.toSet()))")
    MemberDTO toDTO(Member member);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "borrowRecords", ignore = true)
    Member toEntity(MemberDTO memberDTO);
} 