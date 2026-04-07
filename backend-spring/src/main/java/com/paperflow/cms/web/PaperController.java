package com.paperflow.cms.web;

import com.paperflow.cms.domain.Paper;
import com.paperflow.cms.service.PaperService;
import com.paperflow.cms.web.dto.PaperDtos;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/paperflow/v1")
public class PaperController {

    private final PaperService paperService;

    public PaperController(PaperService paperService) {
        this.paperService = paperService;
    }

    @GetMapping("/papers")
    public ResponseEntity<PaperDtos.ListPapersResponse> list(
        @RequestParam(name = "conferenceId", required = false) String conferenceId,
    @RequestParam(name = "authorId", required = false) String authorId) {
        List<Paper> papers = paperService.listPapers(conferenceId,authorId);
        List<PaperDtos.PaperSummary> data = papers.stream()
            .map(p -> new PaperDtos.PaperSummary(
                p.getId(),
                p.getTitle(),
                p.getStatus().name(),
                p.getConference().getId(),
                p.getConference().getTitle()
            ))
            .toList();
        return ResponseEntity.ok(new PaperDtos.ListPapersResponse(data, data.size()));
    }

    // @PostMapping("/papers")
    // public ResponseEntity<PaperDtos.SubmitPaperResponse> submit(
    //     @RequestBody PaperDtos.SubmitPaperRequest request
    // ) {
    //     Paper paper = paperService.submitPaper(
    //         request.conferenceId(),
    //         request.title(),
    //         request.paperAbstract(),
    //         request.track()
    //     );
    //     return ResponseEntity.ok(
    //         new PaperDtos.SubmitPaperResponse(
    //             paper.getId(),
    //             paper.getStatus().name()
    //         )
    //     );
    // }

// @PostMapping(value = "/papers", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
// public Paper submitPaper(
//     @RequestParam String conferenceId,
//     @RequestParam String title,
//     @RequestParam String abstractText,
//     @RequestParam String track,
//     @RequestParam MultipartFile file
// ) {
//     return paperService.submitPaper(conferenceId, title, abstractText, track, file);
// }
   @PostMapping(value = "/papers/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<PaperDtos.SubmitPaperResponse> uploadPaper(
    @RequestParam(name = "conferenceId") String conferenceId,
    @RequestParam(name = "title") String title,
    @RequestParam(name = "abstractText") String abstractText,
    @RequestParam(name = "track") String track,
    @RequestParam(name = "file") MultipartFile file,
    @RequestParam(name = "authorId", required = false) String authorId) {

    // System.out.println(" CONTROLLER HIT");

    Paper paper = paperService.submitPaper(
        conferenceId,
        title,
        abstractText,
        track,
        file,
        authorId 
    );

    // System.out.println("SERVICE RETURNED");

    return ResponseEntity.ok(
        new PaperDtos.SubmitPaperResponse(
            paper.getId(),
            paper.getStatus().name()
        )
    );
}
}

