package com.n11.eventflow.service;

import com.n11.eventflow.exception.InvalidParameterException;
import com.n11.eventflow.model.dto.Presentation.PresentationInfoDTO;
import com.n11.eventflow.model.dto.Presentation.Response.CreatePresentationResponseDTO;
import com.n11.eventflow.model.dto.Presentation.PresentationListDTO;
import com.n11.eventflow.model.entity.Presentation;
import com.n11.eventflow.repository.PresentationRepository;
import com.n11.eventflow.constants.dictionaries.ExceptionDictionary;
import com.n11.eventflow.util.mapper.MapperUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class PresentationService {

    private final PresentationRepository presentationRepository;
    private final MapperUtil mapperUtil;

    private static final Logger logger = LogManager.getLogger(PresentationService.class);

    public PresentationService(PresentationRepository presentationRepository, MapperUtil mapperUtil) {
        this.presentationRepository = presentationRepository;
        this.mapperUtil = mapperUtil;
    }

    public CreatePresentationResponseDTO createPresentation(String name, int duration) {
        if (ObjectUtils.isEmpty(name) || ObjectUtils.isEmpty(duration)) {
            logger.error(ExceptionDictionary.INVALID_PARAMETERS);
            throw new InvalidParameterException(ExceptionDictionary.INVALID_PARAMETERS);
        }

        Presentation presentation = new Presentation(name, duration);
        presentationRepository.save(presentation);

        return mapperUtil.convertToDTO(presentation, CreatePresentationResponseDTO.class);
    }

    public PresentationListDTO getAllPresentation() {
        List<Presentation> presentationList = presentationRepository.findAll();

        List<PresentationInfoDTO> dtoList = presentationList.stream()
                .map(presentation -> mapperUtil.convertToDTO(presentation, PresentationInfoDTO.class))
                .toList();

        return new PresentationListDTO(dtoList);
    }
}
