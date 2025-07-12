package org.example.mapper;


import org.example.model.dto.collection.CollectionDto;
import org.example.model.dto.collection.CreateCollectionDto;
import org.example.model.entity.collection.Collection;
import org.example.model.entity.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CollectionMapper {
    @Mapping(target = "createdBy", source = "createdBy.id")
    CollectionDto toCollectionDto(Collection collection);

    Set<CollectionDto> toCollectionDtoSet(Set<Collection> collections);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "collectionReports", ignore = true)
    Collection toEntity(CreateCollectionDto dto, UserInfo createdBy);
}
