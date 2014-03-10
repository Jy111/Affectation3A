package fr.affectation.service.responsible;



import java.util.ArrayList;
import java.util.List;

public interface ResponsibleService {
	
	public List<String> findResponsibles();

    public ArrayList<String> whichSpecialization(String login);

    public ArrayList<String> whichImprovementCourse(String login);

    public String forWhichSpecialization(String login);

	public int forWhichSpecializationType(String login);

	public boolean isResponsible(String login);

    public boolean isResponsibleFor(String login, String abbreviation);

    public boolean isCoResponsibleFor(String login, String abbreviation);

}
