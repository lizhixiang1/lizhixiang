package cn.ShanDongUniversity.design;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
public class StepHighthAndWidthDesign {
    InteractiveInterface interactiveInterface;
    double stepNumbers=2.0d;
    double stepHigth;
    double stepWidth;
    public StepHighthAndWidthDesign(InteractiveInterface interactiveInterface){
        this.interactiveInterface=interactiveInterface;
    }

    public void stepHighthAndWidthDesign(){
        String x=interactiveInterface.getStairsCategory();

        if(x.equals("A")||x.equals("a")){
            stepHighthAndWidthDesignA();
        }else if(x.equals("B")||x.equals("b")){
            stepHighthAndWidthDesignB();
        }else if(x.equals("C")||x.equals("c")){
            stepHighthAndWidthDesignC();
        }else if(x.equals("D")||x.equals("d")){
            stepHighthAndWidthDesignD();
        }else if(x.equals("E")||x.equals("e")){
            stepHighthAndWidthDesignE();
        }else{
            throw new RuntimeException("请输入正确的楼梯类别所对应的英文字母");
        }
    }

    public void stepHighthAndWidthDesignA(){
        double highth = interactiveInterface.getHighth();
        for(;stepHigth>175||stepHigth<140;stepNumbers+=2){
            stepHigth=highth/stepNumbers;
        }
        stepNumbers-=2;
        for(stepWidth=260;stepWidth+2*stepHigth-600>20||stepWidth+2*stepHigth-600<0;stepWidth+=20){
            if(stepWidth>320){
                throw new RuntimeException();
            }
        }
    }

    public void stepHighthAndWidthDesignB(){
        double highth = interactiveInterface.getHighth();
        for(;stepHigth>150||stepHigth<140;stepNumbers+=2){
            stepHigth=highth/stepNumbers;
        }
        stepNumbers-=2;
        for(stepWidth=260;stepWidth+2*stepHigth-600>20||stepWidth+2*stepHigth-600<0;stepWidth+=20){
            if(stepWidth>320){
                throw new RuntimeException();
            }
        }
    }

    public void stepHighthAndWidthDesignC(){
        double highth = interactiveInterface.getHighth();
        for(;stepHigth>160||stepHigth<140;stepNumbers+=2){
            stepHigth=highth/stepNumbers;
        }
        stepNumbers-=2;
        for(stepWidth=280;stepWidth+2*stepHigth-600>20||stepWidth+2*stepHigth-600<0;stepWidth+=20){
            if(stepWidth>320){
                throw new RuntimeException();
            }
        }
    }

    public void stepHighthAndWidthDesignD(){
        double highth = interactiveInterface.getHighth();
        for(;stepHigth>170||stepHigth<140;stepNumbers+=2){
            stepHigth=highth/stepNumbers;
        }
        stepNumbers-=2;
        for(stepWidth=260;stepWidth+2*stepHigth-600>20||stepWidth+2*stepHigth-600<0;stepWidth+=20){
            if(stepWidth>320){
                throw new RuntimeException();
            }
        }
    }

    public void stepHighthAndWidthDesignE(){
        double highth = interactiveInterface.getHighth();
        for(;stepHigth>200||stepHigth<140;stepNumbers+=2){
            stepHigth=highth/stepNumbers;
        }
        stepNumbers-=2;
        for(stepWidth=220;stepWidth+2*stepHigth-600>20||stepWidth+2*stepHigth-600<0;stepWidth+=20){
            if(stepWidth>320){
                throw new RuntimeException();
            }
        }
    }

    public double getStepNumbers() {
        return stepNumbers;
    }
    public double getStepHigth() {
        return stepHigth;
    }
    public double getStepWidth() {
        return stepWidth;
    }
}
