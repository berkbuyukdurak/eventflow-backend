package com.n11.eventflow.service;

import com.n11.eventflow.exception.InvalidParameterException;
import com.n11.eventflow.model.dto.Presentation.Response.CreatePresentationResponseDTO;
import com.n11.eventflow.model.entity.Presentation;
import com.n11.eventflow.repository.PresentationRepository;
import com.n11.eventflow.util.dictionaries.ExceptionDictionary;
import com.n11.eventflow.util.mapper.MapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final MapperUtil mapperUtil;
    public PresentationService(PresentationRepository presentationRepository, MapperUtil mapperUtil) {
        this.presentationRepository = presentationRepository;
        this.mapperUtil = mapperUtil;
    }

    public CreatePresentationResponseDTO createPresentation (String name, int duration) {
        if (ObjectUtils.isEmpty(name) || ObjectUtils.isEmpty(duration)) {
            throw new InvalidParameterException(ExceptionDictionary.INVALID_PARAMETERS);
        }

        Presentation presentation = new Presentation(name, duration);
        presentationRepository.save(presentation);

        return mapperUtil.convertToDTO(presentation, CreatePresentationResponseDTO.class);
    }
}