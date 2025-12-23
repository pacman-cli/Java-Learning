package com.pacman.hospital.domain.medicalrecord.service.impl;

import com.pacman.hospital.ai.summarization.service.SummarizationService;
import com.pacman.hospital.common.storage.StorageService;
import com.pacman.hospital.domain.medicalrecord.dto.MedicalRecordDto;
import com.pacman.hospital.domain.medicalrecord.mapper.MedicalRecordMapper;
import com.pacman.hospital.domain.medicalrecord.model.MedicalRecord;
import com.pacman.hospital.domain.medicalrecord.respository.MedicalRecordRepository;
import com.pacman.hospital.domain.medicalrecord.service.MedicalRecordService;
import com.pacman.hospital.domain.patient.model.Patient;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.exception.ResourceNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final SummarizationService summarizationService;
    private final StorageService storageService;

    public MedicalRecordServiceImpl(
        MedicalRecordRepository medicalRecordRepository,
        PatientRepository patientRepository,
        SummarizationService summarizationService,
        StorageService storageService
    ) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.summarizationService = summarizationService;
        this.storageService = storageService;
    }

    @Override
    public MedicalRecordDto createRecord(MedicalRecordDto medicalRecordDto) {
        Patient patient = patientRepository
            .findById(medicalRecordDto.getPatientId())
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Patient not found with id: " +
                        medicalRecordDto.getPatientId()
                )
            );

        MedicalRecord entity = MedicalRecordMapper.toEntity(medicalRecordDto);
        entity.setPatient(patient);
        MedicalRecord result = medicalRecordRepository.save(entity);
        // optional: generate summary from content and save as short title (or separate field)-->This part is from AI
        if (result.getContent() != null && !result.getContent().isBlank()) {
            try {
                String summary = summarizationService.summarize(
                    result.getContent()
                );
                if (result.getTitle() != null && !result.getTitle().isBlank()) {
                    result.setTitle(
                        summary.length() > 200
                            ? summary.substring(0, 200)
                            : summary //if length is greater than
                        // 200, then substring(0,200) to keep it short
                    );
                    medicalRecordRepository.save(result); // save the updated record with summary
                }
            } catch (Exception e) {
                //log error but do not fail the whole operation
                System.err.println(
                    "Error generating summary: " + e.getMessage()
                );
            }
        }
        return MedicalRecordMapper.toDto(result);
    }

    @Override
    public MedicalRecordDto updateRecord(Long id, MedicalRecordDto dto) {
        MedicalRecord existing = medicalRecordRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "MedicalRecord not " + "found with id: " + id
                )
            );

        MedicalRecordMapper.updateEntityFromDto(dto, existing);

        if (dto.getPatientId() != null) {
            // Allow updating patient association if patientId is provided
            Patient patient = patientRepository
                .findById(dto.getPatientId())
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Patient not found with id: " + dto.getPatientId()
                    )
                );
            existing.setPatient(patient);
        }
        medicalRecordRepository.save(existing);
        return MedicalRecordMapper.toDto(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordDto getRecordById(Long id) {
        MedicalRecord entity = medicalRecordRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "MedicalRecord not found with id: " + id
                )
            );
        return MedicalRecordMapper.toDto(entity);
    }

    @Override
    public void deleteRecord(Long id) {
        MedicalRecord existing = medicalRecordRepository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "MedicalRecord not found with id: " + id
                )
            );
        medicalRecordRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordDto> getAllRecords() {
        return medicalRecordRepository
            .findAll()
            .stream()
            .map(MedicalRecordMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordDto> getRecordsForPatient(Long patientId) {
        return medicalRecordRepository
            .findByPatientIdOrderByCreatedAtDesc(patientId)
            .stream()
            .map(MedicalRecordMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public MedicalRecordDto attachFileToRecord(
        Long recordId,
        MultipartFile file
    ) {
        MedicalRecord existing = medicalRecordRepository
            .findById(recordId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "MedicalRecord not found with id: " + recordId
                )
            );

        try {
            //we have interface of storage service, so we can use it to save file to storage
            String filePath = storageService.store(
                file,
                "medical-records/" + recordId
            );
            // store relative path with leading slash for clarity or as you prefer
            existing.setFilePath(filePath);
            MedicalRecord saved = medicalRecordRepository.save(existing);
            return MedicalRecordMapper.toDto(saved);
        } catch (Exception e) {
            throw new ResourceNotFoundException("File not found: " + recordId);
        }
    }
}
