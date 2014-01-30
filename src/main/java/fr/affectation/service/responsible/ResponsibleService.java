package fr.affectation.service.responsible;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public interface ResponsibleService {
	
	public List<String> findResponsibles();

    @Transactional(readOnly =true)
    ArrayList<String> whichSpecialization(String login);

    public String forWhichSpecialization(String login);

	public int forWhichSpecializationType(String login);
	
	public boolean isResponsible(String login);

}
