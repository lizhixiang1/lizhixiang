package cn.ShanDongUniversity.design;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
public class StepLengthDesign {
    InteractiveInterface interactiveInterface=new InteractiveInterface();
    double stepLength=0;
    double stairShaftWidth=0;
    public StepLengthDesign(InteractiveInterface interactiveInterface) {
        this.interactiveInterface=interactiveInterface;
    }

    public void stepLengthAndStairShaftWidthDesign(){
        double x=interactiveInterface.getVisitorsFolwRate();
        if(x==1){
            stepLengthAndStairShaftWidthDesign1();
        }else if(x==2){
            stepLengthAndStairShaftWidthDesign2();
        }else if(x==3){
            stepLengthAndStairShaftWidthDesign3();
        }else{
            throw new RuntimeException("请输入正确的人流量");
        }
    }

    private void stepLengthAndStairShaftWidthDesign1(){
        double width = interactiveInterface.getWidth();
        stepLength=1050;
        while(true){
            stairShaftWidth=width-2*stepLength;
            if(stairShaftWidth>=100&&stairShaftWidth<=200){
                return;
            }
            stepLength=stepLength+50;
        }

    }

    private void stepLengthAndStairShaftWidthDesign2(){
        double width = interactiveInterface.getWidth();
        stepLength=1100;
        while(true){
            stairShaftWidth=width-2*stepLength;
            if(stairShaftWidth>=100&&stairShaftWidth<=200){
                return;
            }
            stepLength=stepLength+50;
        }
    }

    private void stepLengthAndStairShaftWidthDesign3(){
        double width = interactiveInterface.getWidth();
        stepLength=1650;
        while(stepLength>1650){
            stairShaftWidth=width-2*stepLength;
            if(stairShaftWidth>=100&&stairShaftWidth<=200){
                return;
            }
            stepLength=stepLength+50;
        }
    }

    public double getStairShaftWidth() {
        return stairShaftWidth;
    }
    public double getStepLength() {
        return stepLength;
    }
}
