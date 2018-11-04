package de.dirkgasser.heating;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Arraylist of HeatProgramSteps to store all Steps of one day
 * @author Dirk Gasser
 * @version 1.0
 */
public class HeatDay {
    List<HeatProgramStep> listOfSteps = new ArrayList<>();
    private transient Iterator<HeatProgramStep> itStep = listOfSteps.iterator();
    int currentStep;
     
    public HeatDay(HeatProgramStep heatProgramStep) {
        this.addHeatStep(heatProgramStep);
        currentStep = 0;
    }
    public HeatDay() {
       currentStep = -1; 
    }

/**
 * Add new Step at correct position
 * @param heatProgramStep Step for HeatProgramm
 */    
    public void addHeatStep (HeatProgramStep heatProgramStep) {
        int i = 0;
        itStep = listOfSteps.iterator();
        while (itStep.hasNext() &&
                itStep.next().getStartTime().isBefore(heatProgramStep.getStartTime())) {
            i++;
        }
        listOfSteps.add(i, heatProgramStep);
        currentStep = i;
    }
/**
 * remove a head step from day program
 * @param heatProgramStep heat step to be removed 
 */
    public void removeHeatStep (HeatProgramStep heatProgramStep) {
        if (currentStep + 1 < listOfSteps.size()) {
           // OK, next step is shown 
        } else {
                currentStep--;
        } 
        listOfSteps.remove(heatProgramStep);
        
    }
/**
 * move to next heating step and read
 * @return next HeatProgramStep of heating day
 */   
    public HeatProgramStep getNextStep() {
        if (currentStep + 1 < listOfSteps.size()) {
            currentStep++;
            return listOfSteps.get(currentStep);
        } else {
            return null;
        }
    }

/**
 * move to previous heating Step and read
 * @return 
 */    
    public HeatProgramStep getPrevStep() {
        if (currentStep > 0) {
            currentStep--;
            return listOfSteps.get(currentStep); 
         } else {
             return null;
         }
    }
/**
 * get first heating step
 * @return first HeatProgramStep of heating day
 */     
    public HeatProgramStep getFirstStep() {
        currentStep = -1;
        return this.getNextStep();
    }
    /**
 * get curren heating step
 * @return first HeatProgramStep of heating day
 */     
    public HeatProgramStep getCurrentStep() {
         if (currentStep  < listOfSteps.size() && currentStep >= 0) {
            return listOfSteps.get(currentStep); 
         } else {
             return null;
         }
    }

    /**
     * read previous heating step
     * @return HeatProgramStep
     */
    public HeatProgramStep readPrevStep() {
        if (currentStep > 0) {
            return listOfSteps.get(currentStep - 1); 
         } else {
             return null;
         }
    }
/**
 * read next heating step
 * @return HeatProgramStep
 */   
    public HeatProgramStep readNextStep() {
        if (currentStep + 1 < listOfSteps.size()) {
            return listOfSteps.get(currentStep + 1); 
         } else {
             return null;
         }
    }
}
