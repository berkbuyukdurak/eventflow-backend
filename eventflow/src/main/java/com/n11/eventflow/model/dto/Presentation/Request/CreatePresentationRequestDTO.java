package com.n11.eventflow.model.dto.Presentation.Request;

import com.n11.eventflow.util.dictionaries.ValidationDictionary;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePresentationRequestDTO {
        @NotBlank(message = "Name " + ValidationDictionary.BLANK_FIELD)
        private String name;

        @Min(value = 1, message = "Duration " + ValidationDictionary.GREATER_THAN_ZERO)
        private int duration;
}
