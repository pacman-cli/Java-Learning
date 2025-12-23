package com.portfolio.service;

import com.portfolio.entity.ContactMessage;
import com.portfolio.repository.ContactMessageRepository;
import com.portfolio.dto.ContactMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactMessageService {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    public List<ContactMessageDTO> getAllMessages() {
        return contactMessageRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ContactMessageDTO getMessageById(Long id) {
        return contactMessageRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public ContactMessageDTO createMessage(ContactMessageDTO contactMessageDTO) {
        ContactMessage contactMessage = convertToEntity(contactMessageDTO);
        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
        return convertToDTO(savedMessage);
    }

    public ContactMessageDTO updateMessage(Long id, ContactMessageDTO contactMessageDTO) {
        return contactMessageRepository.findById(id)
                .map(existingMessage -> {
                    existingMessage.setName(contactMessageDTO.getName());
                    existingMessage.setEmail(contactMessageDTO.getEmail());
                    existingMessage.setSubject(contactMessageDTO.getSubject());
                    existingMessage.setMessage(contactMessageDTO.getMessage());
                    ContactMessage updatedMessage = contactMessageRepository.save(existingMessage);
                    return convertToDTO(updatedMessage);
                })
                .orElse(null);
    }

    public void deleteMessage(Long id) {
        contactMessageRepository.deleteById(id);
    }

    private ContactMessageDTO convertToDTO(ContactMessage contactMessage) {
        return new ContactMessageDTO(
                contactMessage.getId(),
                contactMessage.getName(),
                contactMessage.getEmail(),
                contactMessage.getSubject(),
                contactMessage.getMessage());
    }

    private ContactMessage convertToEntity(ContactMessageDTO contactMessageDTO) {
        return new ContactMessage(
                contactMessageDTO.getName(),
                contactMessageDTO.getEmail(),
                contactMessageDTO.getSubject(),
                contactMessageDTO.getMessage());
    }
}