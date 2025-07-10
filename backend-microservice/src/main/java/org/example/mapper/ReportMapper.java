package org.example.mapper;

import org.example.model.dto.report.AvailableReportsDto;
import org.example.model.dto.report.ReportMetadataDto;
import org.example.model.entity.report.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportMapper {
    List<AvailableReportsDto> toAvailableReportsDtoList(List<Report> reports);

    @Mapping(target = "connectionId", source = "connection.id")
    ReportMetadataDto toReportMetadataDto(Report report);
}
