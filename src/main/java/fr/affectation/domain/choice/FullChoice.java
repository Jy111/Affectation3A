package fr.affectation.domain.choice;

public class FullChoice {

	private ImprovementCourseChoice improvementCourseChoice = new ImprovementCourseChoice();
	
	private JobSectorChoice jobSectorChoice = new JobSectorChoice();

	private MasterChoice masterChoice = new MasterChoice();

    public ImprovementCourseChoice getImprovementCourseChoice() {
		return improvementCourseChoice;
	}

	public void setImprovementCourseChoice(
			ImprovementCourseChoice improvementCourseChoice) {
		this.improvementCourseChoice = improvementCourseChoice;
	}

	public JobSectorChoice getJobSectorChoice() {
		return jobSectorChoice;
	}

	public void setJobSectorChoice(JobSectorChoice jobSectorChoice) {
		this.jobSectorChoice = jobSectorChoice;
	}

    public MasterChoice getMasterChoice() { return masterChoice; }

    public void setMasterChoice(MasterChoice MasterChoice) { this.masterChoice = masterChoice; }

}
