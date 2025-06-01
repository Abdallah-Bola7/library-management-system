package com.library.mapper;

import com.library.dto.BorrowRecordDTO;
import com.library.model.BorrowRecord;
import com.library.model.Book;
import com.library.model.Member;
import com.library.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BorrowRecordMapper {

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "memberId", source = "member.id")
    @Mapping(target = "issuedById", source = "issuedBy.id")
    @Mapping(target = "returnedToId", source = "returnedTo.id")
    BorrowRecordDTO toDTO(BorrowRecord borrowRecord);

    @InheritInverseConfiguration
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "member", ignore = true)
    @Mapping(target = "issuedBy", ignore = true)
    @Mapping(target = "returnedTo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BorrowRecord toEntity(BorrowRecordDTO borrowRecordDTO);

    default Long map(Book value) {
        return value != null ? value.getId() : null;
    }

    default Long map(Member value) {
        return value != null ? value.getId() : null;
    }

    default Long map(User value) {
        return value != null ? value.getId() : null;
    }
} 