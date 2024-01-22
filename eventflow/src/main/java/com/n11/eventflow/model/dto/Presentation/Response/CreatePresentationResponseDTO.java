package com.n11.eventflow.model.dto.Presentation.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePresentationResponseDTO {
    private String name;
    private int duration;
}
