package cn.ShanDongUniversity.check;

import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
import cn.ShanDongUniversity.material.concrete.C30;
import cn.ShanDongUniversity.material.steelPlate.SteelPlate;
import cn.ShanDongUniversity.material.u_Bar.C;

import java.text.DecimalFormat;

public class MaterialCalculation {

    public void calculation(InteractiveInterface interactiveInterface){
        SteelPlate steelPlate = new SteelPlate();
        C c =new C();
        DecimalFormat decimalFormat = new DecimalFormat("#0.000");
        String kindOfChannleBar = interactiveInterface.getKindOfChannleBar();
        double p = c.getP(kindOfChannleBar);
        double stepLength = interactiveInterface.getStepLength();
        double stepHighth = interactiveInterface.getStepHighth();
        double stepWidth = interactiveInterface.getStepWidth();
        double thicknessOfConcrete = interactiveInterface.getThicknessOfConcrete();
        double thicknessOfLowerPlate = interactiveInterface.getThicknessOfLowerPlate();
        double thicknessOfUpperPlate = interactiveInterface.getThicknessOfUpperPlate();
        double p_upperPlate = steelPlate.getP(thicknessOfUpperPlate);
        double p_lowerPlate = steelPlate.getP(thicknessOfLowerPlate);
        double stepNumbers = interactiveInterface.getStepNumbers();
        double cosx = stepWidth / Math.sqrt(stepWidth * stepWidth + stepHighth * stepHighth);
        double ladderBeamSpan=stepNumbers*stepWidth/2/1000/cosx;
        System.out.println("5.梯段处用料计算（仅代表上梯段/下梯段）");
        double concreteWeight = stepLength / 1000 * (stepWidth/1000+stepHighth/1000) * thicknessOfConcrete / 1000*stepNumbers/2;
        System.out.println("1.混凝土用量： " + stepLength / 1000 + "*(" + stepWidth/1000+"+"+decimalFormat.format(stepHighth/1000)+")*" + thicknessOfConcrete / 1000 + "*" +stepNumbers/2+ "=" + decimalFormat.format(concreteWeight) + "m^3");
        double steelWeight=stepLength / 1000 * (stepWidth/1000+stepHighth/1000) * (p_upperPlate + p_lowerPlate)*stepNumbers/2;
        System.out.println("2.梯段钢板自重：" + stepLength / 1000 + "*(" + stepWidth/1000+"+"+decimalFormat.format(stepHighth/1000)+")" +"*(" + p_upperPlate + "+" + p_lowerPlate + ")*" +stepNumbers/2 + "=" + decimalFormat.format(steelWeight) + "Kg");
        double ladderBeamWeight= p * ladderBeamSpan*2;
        System.out.println("3.梯梁自重( 左右两根梯梁之和）：" + decimalFormat.format(ladderBeamWeight) + "Kg");

    }
}
