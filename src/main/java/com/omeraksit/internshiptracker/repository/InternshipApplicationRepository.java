package com.omeraksit.internshiptracker.repository;

import com.omeraksit.internshiptracker.domain.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Long> {
}
