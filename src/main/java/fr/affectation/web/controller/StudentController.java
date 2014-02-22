package fr.affectation.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import fr.affectation.domain.choice.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.affectation.service.choice.ChoiceService;
import fr.affectation.service.configuration.ConfigurationService;
import fr.affectation.service.documents.DocumentService;
import fr.affectation.service.specialization.SpecializationService;
import fr.affectation.service.student.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {

	public static final String noSelectionMessage = "------------------------------------- Pas de choix -------------------------------------";

	@Inject
	private ChoiceService choiceService;

	@Inject
	private SpecializationService specializationService;

	@Inject
	private DocumentService documentService;

	@Inject
	private ConfigurationService configurationService;

	@Inject
	private StudentService studentService;

	@RequestMapping({ "/", "" })
	public String index() {
		if (configurationService.isSubmissionAvailable()) {
			return "redirect:/student/add";
		} else {
			return "student/noSubmission";
		}
	}

	@RequestMapping(value = "/processForm", method = RequestMethod.POST)
	public String processForm(FullChoice fullChoice, Model model, BindingResult bindingResult,
			@RequestParam(value = "resume", required = false) MultipartFile resume, @RequestParam(value = "letterIc", required = false) MultipartFile letterIc,
			@RequestParam(value = "letterJs", required = false) MultipartFile letterJs, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (configurationService.isSubmissionAvailable()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String login = auth.getName();

			ImprovementCourseChoice improvementCourseChoice = fullChoice.getImprovementCourseChoice();
			JobSectorChoice jobSectorChoice = fullChoice.getJobSectorChoice();
            MasterChoice masterChoice = fullChoice.getMasterChoice();

			improvementCourseChoice.setLogin(login);
			jobSectorChoice.setLogin(login);
            masterChoice.setLogin(login);


			if (improvementCourseChoice.getChoice1().equals(noSelectionMessage)) {
				improvementCourseChoice.setChoice1("");
			}
			if (improvementCourseChoice.getChoice2().equals(noSelectionMessage)) {
				improvementCourseChoice.setChoice2("");
			}
			if (improvementCourseChoice.getChoice3().equals(noSelectionMessage)) {
				improvementCourseChoice.setChoice3("");
			}
			if (improvementCourseChoice.getChoice4().equals(noSelectionMessage)) {
				improvementCourseChoice.setChoice4("");
			}
			if (improvementCourseChoice.getChoice5().equals(noSelectionMessage)) {
				improvementCourseChoice.setChoice5("");
			}

			if (jobSectorChoice.getChoice1().equals(noSelectionMessage)) {
				jobSectorChoice.setChoice1("");
			}
			if (jobSectorChoice.getChoice2().equals(noSelectionMessage)) {
				jobSectorChoice.setChoice2("");
			}
			if (jobSectorChoice.getChoice3().equals(noSelectionMessage)) {
				jobSectorChoice.setChoice3("");
			}
			if (jobSectorChoice.getChoice4().equals(noSelectionMessage)) {
				jobSectorChoice.setChoice4("");
			}
			if (jobSectorChoice.getChoice5().equals(noSelectionMessage)) {
				jobSectorChoice.setChoice5("");
			}

            if (masterChoice.getChoice().equals(noSelectionMessage)) {
                masterChoice.setChoice("");
            }

			improvementCourseChoice.setChoice1(specializationService.getAbbreviationFromStringForForm(improvementCourseChoice.getChoice1()));
			improvementCourseChoice.setChoice2(specializationService.getAbbreviationFromStringForForm(improvementCourseChoice.getChoice2()));
			improvementCourseChoice.setChoice3(specializationService.getAbbreviationFromStringForForm(improvementCourseChoice.getChoice3()));
			improvementCourseChoice.setChoice4(specializationService.getAbbreviationFromStringForForm(improvementCourseChoice.getChoice4()));
			improvementCourseChoice.setChoice5(specializationService.getAbbreviationFromStringForForm(improvementCourseChoice.getChoice5()));

			jobSectorChoice.setChoice1(specializationService.getAbbreviationFromStringForForm(jobSectorChoice.getChoice1()));
			jobSectorChoice.setChoice2(specializationService.getAbbreviationFromStringForForm(jobSectorChoice.getChoice2()));
			jobSectorChoice.setChoice3(specializationService.getAbbreviationFromStringForForm(jobSectorChoice.getChoice3()));
			jobSectorChoice.setChoice4(specializationService.getAbbreviationFromStringForForm(jobSectorChoice.getChoice4()));
			jobSectorChoice.setChoice5(specializationService.getAbbreviationFromStringForForm(jobSectorChoice.getChoice5()));

            masterChoice.setChoice(specializationService.getAbbreviationFromStringForForm(masterChoice.getChoice()));

			choiceService.save(improvementCourseChoice);
			choiceService.save(jobSectorChoice);
            choiceService.saveMasterChoice(masterChoice);

			List<Integer> notFilledJs = choiceService.findElementNotFilledJobSector(login);
			List<Integer> notFilledIc = choiceService.findElementNotFilledImprovementCourse(login);
            boolean notFilledM = choiceService.findNotFilledMaster(login);

			String nfJs = "Vous n'avez pas fait de choix ";
			int index = 0;
			for (Integer i : notFilledJs) {
				if (index == 0) {
					nfJs += i;
				} else {
					nfJs += ", " + i;
				}
				index += 1;
			}
			nfJs += ".";

			String nfIc = "Vous n'avez pas fait de choix ";
			index = 0;
			for (Integer i : notFilledIc) {
				if (index == 0) {
					nfIc += i;
				} else {
					nfIc += ", " + i;
				}
				index += 1;
			}
			nfIc += ".";

            String nfM = "Vous n'avez pas rempli le champ Master";

			model.addAttribute("notFilledJs", nfJs);
			model.addAttribute("notFilledJsNumber", notFilledJs);
			model.addAttribute("notFilledIc", nfIc);
			model.addAttribute("notFilledIcNumber", notFilledIc);
            model.addAttribute("notFilledM", nfM);
            model.addAttribute("notFilledMaster", notFilledM);

			model.addAttribute("icChoices", studentService.findIcChoicesFullSpecByLogin(login));
			model.addAttribute("jsChoices", studentService.findJsChoicesFullSpecByLogin(login));
            model.addAttribute("mchoice", studentService.findMChoiceFullSpecByLogin(login));

			String path = request.getSession().getServletContext().getRealPath("/");

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy à HH:mm.");
			String dateEnd = dateFormat.format(configurationService.getWhen().getEndSubmission());
			model.addAttribute("dateEnd", dateEnd);

			boolean filesOk = true;
			String[] fileListName = { "resume", "letterIc", "letterJs" };
			MultipartFile[] fileList = { resume, letterIc, letterJs };
			int indexFile = 0;
			for (String fileName : fileListName) {
				MultipartFile file = fileList[indexFile];
				if (!file.isEmpty()) {
					if (documentService.validatePdf(file)) {
						boolean cond = true;
						if (indexFile == 0) {
							cond = documentService.saveResume(path, login, resume);
						}
						if (indexFile == 1) {
							cond = documentService.saveLetterIc(path, login, letterIc);
						}
						if (indexFile == 2) {
							cond = documentService.saveLetterJs(path, login, letterJs);
						}
						if (!cond) {
							filesOk = false;
							redirectAttributes.addFlashAttribute(fileName + "Error", "Une erreur est survenue lors de la lecture du fichier.");
						}
					} else {
						redirectAttributes.addFlashAttribute(fileName + "Error", "Seuls les fichiers pdf sont acceptés.");
						filesOk = false;
					}
				}
				indexFile += 1;
			}

			if (!filesOk) {
				return "redirect:/student/add";
			}

			model.addAttribute("hasFilledLetterIc", documentService.hasFilledLetterIc(path, login));
			model.addAttribute("hasFilledLetterJs", documentService.hasFilledLetterJs(path, login));
			model.addAttribute("hasFilledResume", documentService.hasFilledResume(path, login));

			return "student/success";
		} else {
			return "student/noSubmission";
		}
	}

	@RequestMapping(value = "/remove-document", method=RequestMethod.GET)
	public @ResponseBody
	String removeDocument(@RequestParam String type, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String path = request.getSession().getServletContext().getRealPath("/");
		String login = auth.getName();
		boolean success = true;
		if (type.equals("resume")) {
			success  = documentService.deleteResume(path, login);
		}
		if (type.equals("letterIc")) {
			success = documentService.deleteLetterIc(path, login);
		}
		if (type.equals("letterJs")) {
			success = documentService.deleteLetterJs(path, login);
		}
		return success ? "true" : "false";
	}

	@RequestMapping("/add")
	public String add(Model model, HttpServletRequest request) {
		if (configurationService.isSubmissionAvailable()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String login = auth.getName();
			String path = request.getSession().getServletContext().getRealPath("/");
			Choice choiceIc = choiceService.findImprovementCourseChoiceByLogin(login);
			Choice choiceJs = choiceService.findJobSectorChoiceByLogin(login);
            MasterChoice masterChoice = choiceService.findMasterChoiceByLogin(login);
			model.addAttribute("hasFilledLetterIc", documentService.hasFilledLetterIc(path, login));
			model.addAttribute("hasFilledLetterJs", documentService.hasFilledLetterJs(path, login));
			model.addAttribute("hasFilledResume", documentService.hasFilledResume(path, login));
			model.addAttribute("choiceIc", choiceIc);
			model.addAttribute("choiceJs", choiceJs);
            model.addAttribute("masterChoice", masterChoice);
			model.addAttribute("fullChoice", new FullChoice());
			model.addAttribute("paAvailable", studentService.findIcAvailableAsListWithSuperIc());
			model.addAttribute("fmAvailable", specializationService.findJobSectors());
			return "student/form";
		} else {
			return "student/noSubmission";
		}
	}
    @RequestMapping("/resume/choice{choice}")
    public String showResumeForChoice(@PathVariable int choice, Model model){
        if (configurationService.isSubmissionAvailable()){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            model.addAttribute("choiceNumber", choice);
            model.addAttribute("simpleImprovementCourses", studentService.findSimpleIcStats(choice));
            model.addAttribute("simpleJobSectors", studentService.findSimpleJsStats(choice));
            model.addAttribute("allIc", specializationService.findImprovementCourses());
            model.addAttribute("allJs", specializationService.findJobSectors());

            return "student/resume";

        } else{
            return "student/noSubmission";
        }

    }

}
