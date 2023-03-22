package cn.ShanDongUniversity;
import cn.ShanDongUniversity.check.*;
import cn.ShanDongUniversity.design.StepDesign;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
import cn.ShanDongUniversity.paint.MyPaint;

public class Main {
    public static void main(String[] args) {
        InteractiveInterface interactiveInterface = new InteractiveInterface();
        interactiveInterface.display1();
        StepDesign stepDesign=new StepDesign(interactiveInterface);
        stepDesign.design();
        interactiveInterface.display3(stepDesign);
        interactiveInterface.display2(stepDesign);
        interactiveInterface.display4();
        SimplySupportConcretAndSteelBeanCheck simplySupportConcretAndSteelBeanCheck = new SimplySupportConcretAndSteelBeanCheck();
        simplySupportConcretAndSteelBeanCheck.check(interactiveInterface);
        SimplySupportSteelBeanCheck simplySupportSteelBeanCheck = new SimplySupportSteelBeanCheck();
        simplySupportSteelBeanCheck.check(interactiveInterface);
        SimplySupportPlatformBeamCheck simplySupportPlatformBeamCheck = new SimplySupportPlatformBeamCheck();
        simplySupportPlatformBeamCheck.check(interactiveInterface,simplySupportSteelBeanCheck);
        PlatformCheck platformCheck = new PlatformCheck();
        platformCheck.check(interactiveInterface);
        MaterialCalculation materialCalculation = new MaterialCalculation();
        materialCalculation.calculation(interactiveInterface);
        MyPaint mypaint1 = new MyPaint();
        mypaint1.initView(interactiveInterface);
    }
}
