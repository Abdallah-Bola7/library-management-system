package com.library.mapper;

import com.library.dto.MemberDTO;
import com.library.model.Member;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface MemberMapper {

    MemberDTO toDTO(Member member);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "borrowRecords", ignore = true)
    Member toEntity(MemberDTO memberDTO);
} 