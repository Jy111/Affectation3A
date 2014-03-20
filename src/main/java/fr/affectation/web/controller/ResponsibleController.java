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

            ArrayList<String> abbreviationList = responsibleService.whichSpecialization(login);
            if (abbreviationList.size() != 1){
                model.addAttribute("allAbbreviations", abbreviationList);
                return "responsable/selection";
            }
            else {
                String abbreviation = abbreviationList.get(0);
                model.addAttribute("abbreviation", abbreviation);
                return "redirect:/responsable/{abbreviation}/1";
            }
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
            if (responsibleService.isResponsibleFor(login, abbreviation) || responsibleService.isCoResponsibleFor(login, abbreviation)){
                Specialization specialization;

                if (specializationService.getSpecializationByAbbreviation(abbreviation) == Specialization.IMPROVEMENT_COURSE) {
                    model.addAttribute("abbFromSuper", specializationService.findImprovementCoursesAbbreviationsFromSuperIc(abbreviation));
                    specialization = specializationService.getImprovementCourseByAbbreviation(abbreviation);
                }
                else {
                    specialization = specializationService.getJobSectorByAbbreviation(abbreviation);
                }
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
            } else{
                return "redirect:/responsable";
            }
		} else {
			return "redirect:/responsable";
		}
	}

    @RequestMapping("/{abbreviation}")
    public String autoShowResultForChoice1(@PathVariable String abbreviation, Model model){
        if (configurationService.isRunning()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String login = auth.getName();
            if (responsibleService.isResponsibleFor(login, abbreviation) || responsibleService.isCoResponsibleFor(login, abbreviation)){

                Specialization specialization = specializationService.getSpecializationByAbbreviation(abbreviation) == Specialization.IMPROVEMENT_COURSE ? specializationService
                        .getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
                model.addAttribute("specialization", specialization);
                model.addAttribute("abbFromSuper", specializationService.findImprovementCoursesAbbreviationsFromSuperIc(abbreviation));
                if (configurationService.isSubmissionAvailable()) {
                    model.addAttribute("allStudents", studentService.findSimpleStudentsByOrderChoiceAndSpecialization(1, specialization));
                    model.addAttribute("state", "before");
                    return "responsable/choix";
                } else {
                    if (configurationService.isValidating()) {
                        List<SimpleStudentWithValidation> studentsWithValidation = studentService.findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(
                                1, specialization);
                        model.addAttribute("allStudents", studentsWithValidation);
                        return "responsable/choix-validation";
                    } else {
                        model.addAttribute("allStudents", studentService.findSimpleStudentsWithValidationByOrderChoiceAndSpecialization(1, specialization));
                        model.addAttribute("state", "after");
                        return "responsable/choix";
                    }
                }
            } else{
                return "redirect:/responsable";
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
        if (responsibleService.isResponsibleFor(loginRespo, abbreviation) || responsibleService.isCoResponsibleFor(login, abbreviation)){
            int type = specializationService.getSpecializationByAbbreviation(abbreviation);
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
        else{
            return "redirect:/responsable";
        }

	}

	@RequestMapping("/{abbreviation}/student/{login}")
	public String displayStudent(@PathVariable String login, @PathVariable String abbreviation, Model model, HttpServletRequest request) {
		if (configurationService.isRunning()){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loginRespo = auth.getName();
            if (responsibleService.isResponsibleFor(loginRespo, abbreviation) || responsibleService.isCoResponsibleFor(login, abbreviation)){

                Specialization specialization = specializationService.getSpecializationByAbbreviation(abbreviation) == Specialization.IMPROVEMENT_COURSE ? specializationService
                        .getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
                model.addAttribute("specialization", specialization);
                model.addAttribute("student", studentService.retrieveStudentByLogin(login, request.getSession().getServletContext().getRealPath("/")));
                model.addAttribute("mChoice", studentService.retrieveStudentByLogin(login, request.getSession().getServletContext().getRealPath("/")).getMChoice());
                model.addAttribute("abbFromSuper", specializationService.findImprovementCoursesAbbreviationsFromSuperIc(abbreviation));
                return "responsable/student";
            }
            else{
                return "redirect:/responsable";
            }
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
            if (responsibleService.isResponsibleFor(loginRespo, abbreviation) || responsibleService.isCoResponsibleFor(loginRespo, abbreviation)){
                Specialization specialization = specializationService.getSpecializationByAbbreviation(abbreviation) == Specialization.IMPROVEMENT_COURSE ? specializationService
                        .getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
                model.addAttribute("specialization", specialization);
                model.addAttribute("choiceNumber", choice);
                model.addAttribute("simpleImprovementCourses", studentService.findSimpleIcStats(choice));
                model.addAttribute("simpleJobSectors", studentService.findSimpleJsStats(choice));
                model.addAttribute("allIc", specializationService.findImprovementCourses());
                model.addAttribute("allJs", specializationService.findJobSectors());
                model.addAttribute("abbFromSuper", specializationService.findImprovementCoursesAbbreviationsFromSuperIc(abbreviation));
                return "responsable/run/statistics/choice";
            }
            else{
                return "redirect:/responsable/";
            }
		} else {
			return "redirect:/responsable/";
		}
	}

	@RequestMapping("/run/statistics/{abbreviation}/repartition-other-choice{choice}")
	public String pieChartsForOtherChoice(@PathVariable int choice, @PathVariable String abbreviation, Model model) {
		if (configurationService.isRunning()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loginRespo = auth.getName();
            if (responsibleService.isResponsibleFor(loginRespo, abbreviation) || responsibleService.isCoResponsibleFor(loginRespo, abbreviation)){
                Specialization specialization = specializationService.getSpecializationByAbbreviation(abbreviation) == Specialization.IMPROVEMENT_COURSE ? specializationService
                        .getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
                model.addAttribute("specialization", specialization);
                model.addAttribute("choiceNumber", choice);
                model.addAttribute("specializations", studentService.findChoiceRepartitionKnowingOne(1, choice, specialization));
                model.addAttribute("abbFromSuper", specializationService.findImprovementCoursesAbbreviationsFromSuperIc(abbreviation));
                return "responsable/run/statistics/repartition-other-choice";
            }
            else {
                return "redirect:/responsable/";
            }
		} else {
			return "redirect:/responsable/";
		}
	}

	@RequestMapping("/run/statistics/{abbreviation}/inverse-repartition")
	public String pieChartsForInverseRepartition(@PathVariable String abbreviation, Model model) {
		if (configurationService.isRunning()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String loginRespo = auth.getName();
            if (responsibleService.isResponsibleFor(loginRespo, abbreviation) || responsibleService.isCoResponsibleFor(loginRespo, abbreviation)){
                Specialization specialization = specializationService.getSpecializationByAbbreviation(abbreviation) == Specialization.IMPROVEMENT_COURSE ? specializationService
                        .getImprovementCourseByAbbreviation(abbreviation) : specializationService.getJobSectorByAbbreviation(abbreviation);
                model.addAttribute("specialization", specialization);
                model.addAttribute("inverseSpecializations", studentService.findInverseRepartition(specialization));
                model.addAttribute("abbFromSuper", specializationService.findImprovementCoursesAbbreviationsFromSuperIc(abbreviation));
                return "responsable/run/statistics/inverse-repartition";
            }
            else {
                return "redirect:/responsable/";
            }
		} else {
			return "redirect:/responsable/";
		}
	}

    @RequestMapping("/resume/choice{choice}")
    public String showResumeForChoice(@PathVariable int choice, Model model){
        if (configurationService.isSubmissionAvailable()){

            model.addAttribute("choiceNumber", choice);
            model.addAttribute("simpleImprovementCourses", studentService.findSimpleIcStats(choice));
            model.addAttribute("simpleJobSectors", studentService.findSimpleJsStats(choice));
            model.addAttribute("allIc", specializationService.findImprovementCourses());
            model.addAttribute("allJs", specializationService.findJobSectors());
            model.addAttribute("allM", specializationService.findMasters());

            return "responsable/resume";

        } else{
            return "responsable/noSubmission";
        }

    }
}