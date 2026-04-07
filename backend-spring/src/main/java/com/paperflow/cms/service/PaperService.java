package com.paperflow.cms.service;

import com.paperflow.cms.domain.Conference;
import com.paperflow.cms.domain.Paper;
import com.paperflow.cms.domain.PaperStatus;
import com.paperflow.cms.repository.ConferenceRepository;
import com.paperflow.cms.repository.PaperRepository;
import com.paperflow.cms.repository.UserRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PaperService {

    private final PaperRepository paperRepository;
    private final ConferenceRepository conferenceRepository;
    private final UserRepository userRepository;
    public PaperService(PaperRepository paperRepository,
                        ConferenceRepository conferenceRepository , UserRepository userRepository) {
        this.paperRepository = paperRepository;
        this.conferenceRepository = conferenceRepository;
        this.userRepository = userRepository;
    }

    // public Paper submitPaper(String conferenceId,
    //                          String title,
    //                          String abstractText,
    //                          String track) {
    //     Conference conf = conferenceRepository.findById(conferenceId)
    //         .orElseThrow(() -> new IllegalArgumentException("Conference not found"));

    //     Paper paper = new Paper();
    //     paper.setConference(conf);
    //     paper.setTitle(title);
    //     paper.setAbstractText(abstractText);
    //     paper.setTrack(track);
    //     paper.setStatus(PaperStatus.SUBMITTED);
    //     return paperRepository.save(paper);
    // }

    public java.util.List<Paper> listPapers(String conferenceId , String authorId) {
    if (conferenceId != null && !conferenceId.isBlank() && authorId != null && !authorId.isBlank()) {
        return paperRepository.findByConference_IdAndAuthor_Id(conferenceId, authorId);
    }
    if (conferenceId != null && !conferenceId.isBlank()) {
        return paperRepository.findByConference_Id(conferenceId);
    }
    if (authorId != null && !authorId.isBlank()) {
        return paperRepository.findByAuthor_Id(authorId);
    }
    return paperRepository.findAll();
    }

    public Paper updateStatus(String paperId, PaperStatus status) {
        Paper paper = paperRepository.findById(paperId)
            .orElseThrow(() -> new IllegalArgumentException("Paper not found"));
        paper.setStatus(status);
        return paperRepository.save(paper);
    }



private String saveFile(MultipartFile file) {
    try {
        String uploadDir = "uploads/";
        Files.createDirectories(Path.of(uploadDir));

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Path.of(uploadDir + fileName);

        Files.copy(file.getInputStream(), path);

        return path.toString();

    } catch (Exception e) {
        throw new RuntimeException("File upload failed", e);
    }
}
public Paper submitPaper(
    String conferenceId,
    String title,
    String abstractText,
    String track,
    MultipartFile file , String authorId) {
    
    try {
    
        Conference conf = conferenceRepository.findById(conferenceId)
            .orElseThrow(() -> new RuntimeException("Conference not found"));

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String filePath = saveFile(file); 

    
        Paper paper = new Paper();
        paper.setTitle(title);
        paper.setAbstractText(abstractText);
        paper.setTrack(track);
        paper.setConference(conf);
        paper.setFilePath(filePath); 
        paper.setFileUrl("/files/" + file.getOriginalFilename());

        if (authorId != null && !authorId.isBlank()) {
        userRepository.findById(authorId).ifPresent(paper::setAuthor);
    }
        
        return paperRepository.save(paper);

    } catch (Exception e) {
        e.printStackTrace(); 
        throw new RuntimeException("Submission failed: " + e.getMessage());
    }
}}

