package fr.affectation.domain.specialization;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table

public class Master extends Specialization implements Comparable<Master>{

    private int type = Specialization.MASTER;

    public int getType() { return type; }

    @Override
    public int compareTo(Master master) {
        if ((name != null) && (master.getName() != null)){
            return this.name.compareTo(master.getName());
        }
        else{
            if ((abbreviation != null) && (master.getAbbreviation() != null)){
                return this.abbreviation.compareTo(master.getAbbreviation());
            }
            else{
                return 0;
            }
        }
    }
}
