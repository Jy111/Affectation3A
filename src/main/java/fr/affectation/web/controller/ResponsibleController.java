package fr.affectation.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.affectation.domain.specialization.Specialization;
import fr.affectation.domain.student.SimpleStudentWithValidation;
import fr.affectation.service.configuration.ConfigurationService;
import fr.affectation.service.responsible.ResponsibleService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.student.StudentService;
import fr.affectation.service.validation.ValidationService;

@Controller
@RequestMapping("/responsable")
public class ResponsibleController {

	@Inject
	private SpecializationService specializationService;

	@Inject
	private ResponsibleService responsibleService;

	@Inject
	private StudentService studentService;
	
	@Inject
	private ValidationService validationService;

	@Inject
	private ConfigurationService configurationService;

	@RequestMapping({"/", ""})
	public String mainPage(Model model) {
		if (configurationService.isRunning()){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String login = auth.getName();

            ArrayList<String> abbreviation = responsibleService.whichSpecialization(login);
            //int abbreviationSize = abbreviation.size();
            model.addAttribute("allAbbreviations", abbreviation);
            //model.addAttribute("abbreviationListSize", abbreviationSize);

			return "responsable/selection";

		}
		else{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String login = auth.getName();
			String abbreviation = responsibleService.forWhichSpecialization(login);
			Specialization specialization = responsibleService.forWhichSpecializationType(login) == Specialization.IMPROVEMENT_COURSE ? specializationService
					.getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
			model.addAttribute("specialization", specialization);
			return "responsable/config/norunning";
		}
	}

	@RequestMapping("/{abbreviation}/{order}")
	public String showResultForChoice(@PathVariable int order, @PathVariable String abbreviation, Model model) {
		if (configurationService.isRunning()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String login = auth.getName();
			//String abbreviation = responsibleService.forWhichSpecialization(login);
            //Specialization specialization = specializationService.getImprovementCourseByAbbreviation(abbreviation);
			Specialization specialization = responsibleService.whichSpecializationType(login, abbreviation) == Specialization.IMPROVEMENT_COURSE ? specializationService
					.getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
			model.addAttribute("specialization", specialization);
			if (configurationService.isSubmissionAvailable()) {
				model.addAttribute("allStudents", studentService.findSimpleStudentsByOrderChoiceAndSpecialization(order, specialization));
				model.addAttribute("state", "before");
				return "responsable/choix";
			} else {
				if ((configurationService.isValidating()) && (order == 1)) {
					List<SimpleStudentWithValidation> studentsWithValidation = studentService.findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(
							order, specialization);
					model.addAttribute("allStudents", studentsWithValidation);
					return "responsable/choix-validation";
				} else {
					model.addAttribute("allStudents", studentService.findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(order, specialization));
					model.addAttribute("state", "after");
					return "responsable/choix";
				}
			}
		} else {
			return "redirect:/responsable";
		}
	}




	
	@RequestMapping(value = "/{abbreviation}/inverse-validation", method=RequestMethod.GET)
	public @ResponseBody
	String inverseValidation(@RequestParam String login, @PathVariable String abbreviation, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginRespo = auth.getName();
		int type = responsibleService.whichSpecializationType(loginRespo, abbreviation);
		boolean validated = true;
		if (type == Specialization.IMPROVEMENT_COURSE){	
			validated = validationService.isValidatedIc(login);
			validationService.updateIcValidation(login, !validated);
		}
		else{
			validated = validationService.isValidatedJs(login);
			validationService.updateJsValidation(login, !validated);
		}
		return validated ? "false" : "true";
	}

	@RequestMapping("/{abbreviation}/student/{login}")
	public String displayStudent(@PathVariable String login, @PathVariable String abbreviation, Model model, HttpServletRequest request) {
		if (configurationService.isRunning()){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loginRespo = auth.getName();
			//String abbreviation = responsibleService.forWhichSpecialization(loginRespo);
			Specialization specialization = responsibleService.whichSpecializationType(loginRespo, abbreviation) == Specialization.IMPROVEMENT_COURSE ? specializationService
					.getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
			model.addAttribute("specialization", specialization);
			model.addAttribute("student", studentService.retrieveStudentByLogin(login, request.getSession().getServletContext().getRealPath("/")));
			return "responsable/student";
		}
		else{
			return "redirect:/responsable";
		}
	}

	@RequestMapping("/run/statistics/{abbreviation}/choice{choice}")
	public String pieChartsForChoice(@PathVariable int choice, @PathVariable String abbreviation, Model model) {
		if (configurationService.isRunning()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loginRespo = auth.getName();
			//String abbreviation = responsibleService.forWhichSpecialization(loginRespo);
			Specialization specialization = responsibleService.whichSpecializationType(loginRespo, abbreviation) == Specialization.IMPROVEMENT_COURSE ? specializationService
					.getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
			model.addAttribute("specialization", specialization);
			model.addAttribute("choiceNumber", choice);
			model.addAttribute("simpleImprovementCourses", studentService.findSimpleIcStats(choice));
			model.addAttribute("simpleJobSectors", studentService.findSimpleJsStats(choice));
			model.addAttribute("allIc", specializationService.findImprovementCourses());
			model.addAttribute("allJs", specializationService.findJobSectors());
			return "responsable/run/statistics/choice";
		} else {
			return "redirect:/responsable/";
		}
	}

	@RequestMapping("/run/statistics/{abbreviation}/repartition-other-choice{choice}")
	public String pieChartsForOtherChoice(@PathVariable int choice, @PathVariable String abbreviation, Model model) {
		if (configurationService.isRunning()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loginRespo = auth.getName();
			//String abbreviation = responsibleService.forWhichSpecialization(loginRespo);
			Specialization specialization = responsibleService.whichSpecializationType(loginRespo, abbreviation) == Specialization.IMPROVEMENT_COURSE ? specializationService
					.getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
			model.addAttribute("specialization", specialization);
			model.addAttribute("choiceNumber", choice);
			model.addAttribute("specializations", studentService.findChoiceRepartitionKnowingOne(1, choice, specialization));
			return "responsable/run/statistics/repartition-other-choice";
		} else {
			return "redirect:/responsable/";
		}
	}

	@RequestMapping("/run/statistics/{abbreviation}/inverse-repartition")
	public String pieChartsForInverseRepartition(@PathVariable String abbreviation, Model model) {
		if (configurationService.isRunning()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loginRespo = auth.getName();
			//String abbreviation = responsibleService.forWhichSpecialization(loginRespo);
			Specialization specialization = responsibleService.whichSpecializationType(loginRespo, abbreviation) == Specialization.IMPROVEMENT_COURSE ? specializationService
					.getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
			model.addAttribute("specialization", specialization);
			model.addAttribute("inverseSpecializations", studentService.findInverseRepartition(specialization));
			return "responsable/run/statistics/inverse-repartition";
		} else {
			return "redirect:/responsable/";
		}
	}
}