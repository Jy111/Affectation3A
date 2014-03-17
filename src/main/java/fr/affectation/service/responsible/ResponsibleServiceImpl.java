package fr.affectation.service.responsible;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fr.affectation.service.specialization.SpecializationService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.affectation.domain.specialization.ImprovementCourse;
import fr.affectation.domain.specialization.JobSector;
import fr.affectation.domain.specialization.Specialization;

@Service
public class ResponsibleServiceImpl implements ResponsibleService {
	
	@Inject
	private SessionFactory sessionFactory;

    @Inject
    private SpecializationService specializationService;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<String> findResponsibles() {
		String queryIc = "select improvementCourse.responsibleLogin from ImprovementCourse improvementCourse";
		String queryJs = "select jobSector.responsibleLogin from JobSector jobSector";
        String queryM = "select master.responsibleLogin from Master master";
		Session session = sessionFactory.getCurrentSession();
		List<String> icResponsibles = (List<String>) session.createQuery(queryIc).list();
		List<String> jsResponsibles = (List<String>) session.createQuery(queryJs).list();
        List<String> mResponsibles = (List<String>) session.createQuery(queryM).list();
		List<String> allResponsible = new ArrayList<String>();
		for (String login : icResponsibles){
			allResponsible.add(login);
		}
		for (String login : jsResponsibles){
			allResponsible.add(login);
		}
        for (String login : mResponsibles){
            allResponsible.add(login);
        }
		return allResponsible;
	}
    @Override
    @Transactional(readOnly = true)
    public ArrayList<String> whichSpecialization(String login){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ImprovementCourse.class);
        criteria.add(Restrictions.eq("responsibleLogin", login));

        ArrayList<String> improvementCourseAndJobSectorListAbbreviation = new ArrayList<String>();

        for(ImprovementCourse improvementCourse : (List<ImprovementCourse>)criteria.list())
        {
            improvementCourseAndJobSectorListAbbreviation.add(improvementCourse.getAbbreviation());
        }

        criteria = session.createCriteria(JobSector.class);
        criteria.add(Restrictions.eq("responsibleLogin", login));

        for(JobSector jobSector : (List<JobSector>)criteria.list()){
            improvementCourseAndJobSectorListAbbreviation.add(jobSector.getAbbreviation());
        }

        return improvementCourseAndJobSectorListAbbreviation;



    }

    @Override
    @Transactional(readOnly = true)
    public ArrayList<String> whichImprovementCourse(String login){
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ImprovementCourse.class);
        criteria.add(Restrictions.eq("responsibleLogin", login));

        ArrayList<String> improvementCourseListAbbreviation = new ArrayList<String>();

        for(ImprovementCourse improvementCourse : (List<ImprovementCourse>)criteria.list())
        {
            improvementCourseListAbbreviation.add(improvementCourse.getAbbreviation());
        }

        return improvementCourseListAbbreviation;



    }

	@Override
	@Transactional(readOnly = true)
	public String forWhichSpecialization(String login) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ImprovementCourse.class);
		criteria.add(Restrictions.eq("responsibleLogin", login));
		ImprovementCourse improvementCourse = (ImprovementCourse) criteria.uniqueResult();
		if (improvementCourse != null){
			return improvementCourse.getAbbreviation();
		}
		else {
			criteria = session.createCriteria(JobSector.class);
			criteria.add(Restrictions.eq("responsibleLogin", login));
			JobSector jobSector = (JobSector) criteria.uniqueResult();
			return jobSector.getAbbreviation();
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public int forWhichSpecializationType(String login) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ImprovementCourse.class);
		criteria.add(Restrictions.eq("responsibleLogin", login));
		ImprovementCourse improvementCourse = (ImprovementCourse) criteria.uniqueResult();
		if (improvementCourse != null){
			return Specialization.IMPROVEMENT_COURSE;
		}
		else {
			return Specialization.JOB_SECTOR;
		}
	}


	@Override
	@Transactional(readOnly = true)
	public boolean isResponsible(String login){
		return findResponsibles().contains(login);
	}

    @Override
    @Transactional(readOnly = true)
    public boolean isResponsibleFor(String login, String abbreviation){
        ArrayList<String> improvementCourseAndJobSectorListAbbreviation = whichSpecialization(login);
        return improvementCourseAndJobSectorListAbbreviation.contains(abbreviation);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCoResponsibleFor(String login, String abbreviation){
        ArrayList<String> improvementCourseListAbbreviation = whichImprovementCourse(login);
        boolean coResponsible = false;
        for (String anImprovementCourseAbbreviation : improvementCourseListAbbreviation) {
            ImprovementCourse improvementCourse = specializationService.getImprovementCourseByAbbreviation(anImprovementCourseAbbreviation);
            if (improvementCourse.getSuperIc().equals(specializationService.getImprovementCourseByAbbreviation(abbreviation).getSuperIc())) {
                coResponsible = true;
            }
        }
        return coResponsible;
    }



}
