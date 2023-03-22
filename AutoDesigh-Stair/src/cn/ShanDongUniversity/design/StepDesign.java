package cn.ShanDongUniversity.design;
import cn.ShanDongUniversity.check.Check;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
public class StepDesign {
    InteractiveInterface interactiveInterface;
    double stepWidth, stepLength, stepHighth, stairShaftWidth, stepNumbers;
    public StepDesign(InteractiveInterface interactiveInterface) {
        this.interactiveInterface=interactiveInterface;
    }
    public void design(){
        Check check = new Check(interactiveInterface);
        check.checkStepLength();
        StepLengthDesign stepLengthDesign = new StepLengthDesign(interactiveInterface);
        stepLengthDesign.stepLengthAndStairShaftWidthDesign();
        stepLength = stepLengthDesign.getStepLength();
        stairShaftWidth = stepLengthDesign.getStairShaftWidth();
        StepHighthAndWidthDesign stepHighthAndWidthDesign = new StepHighthAndWidthDesign(interactiveInterface);
        stepHighthAndWidthDesign.stepHighthAndWidthDesign();
        stepWidth = stepHighthAndWidthDesign.getStepWidth();
        stepHighth = stepHighthAndWidthDesign.getStepHigth();
        stepNumbers = stepHighthAndWidthDesign.getStepNumbers();
    }

    public double getStepLength() {return stepLength;}
    public double getStairShaftWidth() {
        return stairShaftWidth;
    }
    public double getStepWidth() {
        return stepWidth;
    }
    public double getStepHighth() {
        return stepHighth;
    }
    public double getStepNumbers() {
        return stepNumbers;
    }
}
