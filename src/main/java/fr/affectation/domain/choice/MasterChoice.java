package fr.affectation.domain.choice;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class MasterChoice {

    @Id
    protected String login;

    protected String choice;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice1) {
        this.choice = choice1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MasterChoice other = (MasterChoice) obj;
        if (choice == null){
            if (other.choice != null)
                return false;
            else if (!choice.equals(other.choice))
                return false;
        }
        if (login == null) {
            if (other.login != null)
                return false;
        } else if (!login.equals(other.login))
            return false;
        return true;

    }

    @Override
    public String toString() {
        return "MasterChoice{" +
                "login='" + login + '\'' +
                ", choice='" + choice + '\'' +
                '}';
    }

}
