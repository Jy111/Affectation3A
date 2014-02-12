package fr.affectation.service.responsible;



import java.util.ArrayList;
import java.util.List;

public interface ResponsibleService {
	
	public List<String> findResponsibles();

    public ArrayList<String> whichSpecialization(String login);

    public String forWhichSpecialization(String login);

	public int forWhichSpecializationType(String login);

    public int whichSpecializationType(String login, String abbreviation);
	
	public boolean isResponsible(String login);

}
