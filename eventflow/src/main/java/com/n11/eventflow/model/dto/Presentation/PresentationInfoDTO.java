package com.n11.eventflow.model.dto.Presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresentationInfoDTO {
    private String name;
    private int duration;
}