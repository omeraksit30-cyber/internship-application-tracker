package com.omeraksit.internshiptracker.repository;

import com.omeraksit.internshiptracker.domain.ApplicationStatus;
import com.omeraksit.internshiptracker.domain.InternshipApplication;
import com.omeraksit.internshiptracker.domain.WorkMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Long> {

	@Query("""
			SELECT application
			FROM InternshipApplication application
			WHERE (:status IS NULL OR application.status = :status)
			  AND (:workMode IS NULL OR application.workMode = :workMode)
			  AND (
			      :search IS NULL
			      OR LOWER(application.companyName) LIKE LOWER(CONCAT('%', :search, '%'))
			      OR LOWER(application.positionTitle) LIKE LOWER(CONCAT('%', :search, '%'))
			  )
			""")
	Page<InternshipApplication> search(
			@Param("status") ApplicationStatus status,
			@Param("workMode") WorkMode workMode,
			@Param("search") String search,
			Pageable pageable
	);
}
