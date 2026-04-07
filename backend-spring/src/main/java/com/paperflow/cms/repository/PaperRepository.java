package com.paperflow.cms.repository;

import com.paperflow.cms.domain.Paper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperRepository extends JpaRepository<Paper, String> {
    java.util.List<Paper> findByConference_Id(String conferenceId);
    java.util.List<Paper> findByAuthor_Id(String authorId);
    java.util.List<Paper> findByConference_IdAndAuthor_Id(String conferenceId, String authorId);
}

