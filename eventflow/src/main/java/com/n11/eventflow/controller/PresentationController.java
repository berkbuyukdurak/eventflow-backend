package com.n11.eventflow.controller;

import com.n11.eventflow.model.dto.Presentation.Request.CreatePresentationRequestDTO;
import com.n11.eventflow.model.dto.Presentation.Response.CreatePresentationResponseDTO;
import com.n11.eventflow.service.PresentationService;
import com.n11.eventflow.util.GenericResponseHandler;
import com.n11.eventflow.util.dictionaries.ResponseDictionary;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/presentation")
public class PresentationController {
    private final PresentationService presentationService;

    public PresentationController(PresentationService presentationService) {
        this.presentationService = presentationService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPresentation(@Valid @RequestBody CreatePresentationRequestDTO createPresentationRequestDTO) {
        CreatePresentationResponseDTO response = presentationService.createPresentation(createPresentationRequestDTO.getName(), createPresentationRequestDTO.getDuration());
        if (ObjectUtils.isEmpty(response)) {
            return GenericResponseHandler.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseDictionary.INTERNAL_SERVER_ERROR);
        }
        return GenericResponseHandler.successResponse(HttpStatus.CREATED, response);
    }
}
