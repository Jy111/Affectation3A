package fr.affectation.domain.student;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class StudentValidation {
	
	@Id
	private String login;
	
	@Column
	private boolean validatedJs = true;
	
	@Column
	private boolean validatedIc = true;

    @Column
    private boolean validatedM = true;
	
	public StudentValidation(String login, boolean validationIc, boolean validationJs, boolean validationM){
		this.login = login;
		this.validatedJs = validationJs;
		this.validatedIc = validationIc;
        this.validatedM = validationM;
	}
	
	public StudentValidation(String login){
		this.login = login;
	}
	
	public StudentValidation(){
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public boolean isValidatedJs() {
		return validatedJs;
	}

	public void setValidatedJs(boolean validatedJs) {
		this.validatedJs = validatedJs;
	}

	public boolean isValidatedIc() {
		return validatedIc;
	}

	public void setValidatedIc(boolean validatedIc) {
		this.validatedIc = validatedIc;
	}

    public boolean isValidatedM() {return validatedM;}

    public void setValidatedM(boolean validatedM) {this.validatedM = validatedM;}
	
}