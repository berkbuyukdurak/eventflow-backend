package com.n11.eventflow.model.dto.Presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresentationListDTO {
    private List<PresentationInfoDTO> presentations;
}
