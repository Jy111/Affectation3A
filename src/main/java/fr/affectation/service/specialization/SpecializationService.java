package fr.affectation.service.specialization;

import java.util.List;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Master;
import fr.affectation.domain.specialization.Specialization;
import org.springframework.transaction.annotation.Transactional;

public interface SpecializationService {
	
	public void save(Specialization specialization);

    @Transactional(readOnly = true)
    List<String> findMasterAbbreviations();

    public void delete(Specialization specialization);

    @Transactional(readOnly = true)
    String findNameFromMAbbreviation(String abbreviation);

    public void deleteAll();
	
	public JobSector getJobSectorByAbbreviation(String abbreviation);
	
	public ImprovementCourse getImprovementCourseByAbbreviation(String abbreviation);

    @Transactional(readOnly = true)
    Master getMasterByAbbreviation(String abbreviation);

    public int getSpecializationByAbbreviation(String abbreviation);

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    List<Master> findMasters();

    public List<String> findJobSectorAbbreviations();

	public List<String> findImprovementCourseAbbreviations();

	public List<JobSector> findJobSectors();

	public List<ImprovementCourse> findImprovementCourses();
	
	public List<String> findJobSectorStringsForForm();
	
	public List<String> findImprovementCourseStringsForForm();

    @Transactional(readOnly = true)
    List<String> findMasterStringsForForm();

    public String getAbbreviationFromStringForForm(String forForm);
	
	public String findNameFromIcAbbreviation(String abbreviation);
	
	public String findNameFromJsAbbreviation(String abbreviation);


}

