package com.example.projetplanning;


import java.util.Calendar;

public class DateCalendar {

    public static void main(String args[]){

        String mois[] ={"janvier", "février","mars", "avril", "mai", "juin", "juillet",
                "aout", "septembre", "octobre", "novembre", "décembre"};
        String jour[] = {"dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"};

        int jour1;
        int mois1;
        int annee;

        Calendar cal = Calendar.getInstance();

        jour1 = cal.get(Calendar.DAY_OF_MONTH);
        int jourW = cal.get(Calendar.DAY_OF_WEEK);
        mois1 = cal.get(Calendar.MONTH);
        annee = cal.get(Calendar.YEAR);

        System.out.println("Aujourd'hui nous sommes le " + jour[jourW-1] + " " + jour1 + " "  + mois[mois1] + " " + annee );
    }

}