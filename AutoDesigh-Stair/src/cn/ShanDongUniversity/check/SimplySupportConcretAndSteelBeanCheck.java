package cn.ShanDongUniversity.check;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
import cn.ShanDongUniversity.material.concrete.C30;
import cn.ShanDongUniversity.material.steelPlate.SteelPlate;
import java.text.DecimalFormat;

public class SimplySupportConcretAndSteelBeanCheck {
    InteractiveInterface interactiveInterface;
    double thicknessOfUpperPlate,thicknessOfLowerPlate,thicknessOfConcrete,p_upperPlate,p_lowerPlate,p_C30,steelWeight,
            concreteWeight,stairBalustradeLineLoad,permanentLoadOfLadderBeam,liveLoadOfLadderBeam,worstCondition1,worstCondition2,
            maxingBendingMoment,Wx,Wenx,F1,F2,V,T,Sx,Ix,v,centralAxis;
    public void check(InteractiveInterface interactiveInterface){
        thicknessOfConcrete = interactiveInterface.getThicknessOfConcrete();
        thicknessOfLowerPlate = interactiveInterface.getThicknessOfLowerPlate();
        thicknessOfUpperPlate = interactiveInterface.getThicknessOfUpperPlate();
        this.interactiveInterface=interactiveInterface;
        calculate();
    }

    public void calculate() {
        SteelPlate steelPlate = new SteelPlate();
        C30 concrete = new C30();
        double fv = steelPlate.getFv();
        double fy = steelPlate.getFy();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        p_upperPlate = steelPlate.getP(thicknessOfUpperPlate);
        p_lowerPlate = steelPlate.getP(thicknessOfLowerPlate);
        p_C30 = concrete.getP();
        System.out.println("1.梯段验算");
        System.out.println("1.1.荷载计算");
        System.out.println("永久荷载：");
        double stepLength = interactiveInterface.getStepLength();
        double stepHighth = interactiveInterface.getStepHighth();
        double stepWidth = interactiveInterface.getStepWidth();
        steelWeight = stepLength / 1000 * (stepWidth/1000+stepHighth/1000) * (p_upperPlate + p_lowerPlate) * 9.8 / (stepLength);
        System.out.println("钢板自重：" + stepLength / 1000 + "*(" + stepWidth/1000+"+"+decimalFormat.format(stepHighth/1000)
                +")" +"*(" + p_upperPlate + "+" + p_lowerPlate + ")*" + "9.8" + "/" + "(" + stepLength / 1000 + "*1000" + ")"
                + "=" + decimalFormat.format(steelWeight) + "kN/m");
        concreteWeight = stepLength / 1000 * (stepWidth/1000+stepHighth/1000) * thicknessOfConcrete / 1000 * p_C30 * 9.8 / (stepLength);
        System.out.println("混凝土自重： " + stepLength / 1000 + "*(" + stepWidth/1000+"+"+decimalFormat.format(stepHighth/1000)
                +")*" + thicknessOfConcrete / 1000 + "*" + p_C30 + "*9.8" + "/" + "(" + stepLength / 1000 + "*1000" + ")"
                + "=" + decimalFormat.format(concreteWeight) + "kN/m");
        stairBalustradeLineLoad=1.2*stepWidth/stepLength;
        System.out.println("楼梯栏杆线荷载："+"1.2*"+stepWidth+"/"+stepLength+"="+decimalFormat.format(stairBalustradeLineLoad)+"kN/m");
        permanentLoadOfLadderBeam = steelWeight + concreteWeight + stairBalustradeLineLoad;
        System.out.println("永久荷载汇集（折算成线荷载）：" + decimalFormat.format(steelWeight) + "+"
                + decimalFormat.format(concreteWeight) +"+"+ decimalFormat.format(stairBalustradeLineLoad)
                + "=" + decimalFormat.format(permanentLoadOfLadderBeam) + "kN/m");
        liveLoadOfLadderBeam = 3.5 * stepWidth/1000;
        System.out.println("活荷载汇集：" + "3.5*" + stepWidth/1000+"=" + decimalFormat.format(liveLoadOfLadderBeam) + "kN/m" + "\n\n");


        System.out.println("1.2.受弯强度验算");
        worstCondition1 = 1.3 * permanentLoadOfLadderBeam + 1.5 * liveLoadOfLadderBeam;
        System.out.println("最不利工况为1.3D+1.5L，即q=" + decimalFormat.format(permanentLoadOfLadderBeam) + "*1.3+"
                + decimalFormat.format(liveLoadOfLadderBeam) + "*1.5=" + decimalFormat.format(worstCondition1) + "kN/m");
        maxingBendingMoment = worstCondition1*stepLength/1000*stepLength/1000/12;
        System.out.println("最大弯矩为：q*L^2/12=" + decimalFormat.format(worstCondition1)+"*"
                + decimalFormat.format(stepLength/1000*stepLength/1000) + "/12="
                + decimalFormat.format(maxingBendingMoment) + "kN*m（支座）");
        System.out.println("截面塑性发展系数：rx=1.05(最不利轴为x轴)");
        double thick=thicknessOfUpperPlate+thicknessOfLowerPlate+thicknessOfConcrete;
        centralAxis=(thick/2*(stepHighth*stepHighth)+(stepWidth-thick)/2*
                (stepHighth*stepHighth-(stepHighth-thick)*(stepHighth-thick)))/(thick*stepHighth+thick*(stepWidth-thick));
        Ix=(thick/10)*(stepHighth/10)*(stepHighth/10)*(stepHighth/10)/3+(stepWidth/10-thick/10)*(stepHighth/10)
                *(stepHighth/10)*(stepHighth/10)/3-(stepWidth/10-thick/10)*(stepHighth/10-thick/10)*(stepHighth/10-thick/10)
                *(stepHighth/10-thick/10)/3-centralAxis/10*centralAxis/10*thick/10*stepHighth/10
                -centralAxis/10*centralAxis/10*thick/10*(stepWidth/10-thick/10);
        System.out.println("Ix="+decimalFormat.format(Ix)+"cm^4");
        System.out.println("中性轴位置："+decimalFormat.format(centralAxis)+"mm");
        if(Ix/(stepHighth/10-centralAxis/10)<=Ix/(centralAxis/10)){
            Wx=Ix/(stepHighth/10-centralAxis/10);
        }else{
            Wx=Ix/(centralAxis/10);
        }
        System.out.println("Wx="+decimalFormat.format(Wx)+"cm^3");
        Wenx=0.98*Wx;
        System.out.println("考虑净截面折减：Wenx=0.98*Wx=" + decimalFormat.format(Wenx) + "cm3");
        F1 = maxingBendingMoment / Wenx / 1.05 * 1000;
        if(F1<fy){
            System.out.println("a1=M/(Wnex*rx)=" + decimalFormat.format(F1) + "N/mm2 < "+fy+"N/mm2");
        }else{
            System.out.println("a1=M/(Wnex*rx)=" + decimalFormat.format(F1) + "N/mm2 >= "+fy+"N/mm2");
            System.out.println("⚠️不合格");
        }
        System.out.println("应力比为："+decimalFormat.format(F1)+"/"+fy+"="+decimalFormat.format(F1/fy)+"\n\n");


        System.out.println("1.3.受剪强度验算");
        System.out.println("最不利工况为1.3D+1.5L，即q=" + decimalFormat.format(permanentLoadOfLadderBeam)
                + "*1.3+" + decimalFormat.format(liveLoadOfLadderBeam) + "*1.5=" + decimalFormat.format(worstCondition1) + "kN/m");
        V = worstCondition1*stepLength/1000/2;
        System.out.println("最大剪力为： V=q*L/2=" + decimalFormat.format(V) + "kN（支座）");
        Sx=stepWidth*thick*(stepHighth-centralAxis-thick/2)/1000+(stepHighth-centralAxis)*thick*(stepHighth-centralAxis)/2000;
        T = V * Sx / Ix / thick * 100;
        if(T<fv){
            System.out.println("剪应力t=V*Sx/Ix*tw=" + decimalFormat.format(V) + "*" + decimalFormat.format(Sx)
                    + "/(" + decimalFormat.format(Ix) + "*" + decimalFormat.format(thick) + ")*100="
                    + decimalFormat.format(T) + "N/mm2 < "+fv+"N/mm2");
        }else{
            System.out.println("剪应力t=V*Sx/Ix*tw=" + decimalFormat.format(V) + "*" + decimalFormat.format(Sx)
                    + "/(" + decimalFormat.format(Ix) + "*" + decimalFormat.format(thick) + ")*100="
                    + decimalFormat.format(T) + "N/mm2 >= "+fv+"N/mm2");
            System.out.println("⚠️不合格");
        }
        System.out.println("应力比为："+decimalFormat.format(T)+"/"+fv+"="+decimalFormat.format(T/fv)+"\n\n");

        System.out.println("1.4.x轴挠度验算");
        worstCondition2=permanentLoadOfLadderBeam+liveLoadOfLadderBeam;
        System.out.println("最不利工况为：D+L(标准组合)，即q="+decimalFormat.format(permanentLoadOfLadderBeam)+"+"
                +decimalFormat.format(liveLoadOfLadderBeam)+"="+decimalFormat.format(worstCondition2)+"kN/m");
        System.out.println("Ix="+decimalFormat.format(Ix)+"cm^4");
        v=5*worstCondition2*stepLength*stepLength*stepLength*stepLength/384/206000/Ix/10000;
        System.out.println("最大挠度：v=5q*L^4/384EIx=5*"+decimalFormat.format(worstCondition2)+"*("+stepLength+")^4/(384"
                +"*2.06^5*"+decimalFormat.format(Ix)+"*10000)="+decimalFormat.format(v)+"mm");
        if(v<=stepLength/250){
            System.out.println(decimalFormat.format(v)+"<=L/250="+decimalFormat.format(stepLength/250)+"mm");
            System.out.println("挠度满足要求");
        }else{
            System.out.println("⚠️关于x轴挠度不符合要求");
            System.out.println(decimalFormat.format(v)+">L/250="+decimalFormat.format(stepLength/250)+"\n\n");
        }
        System.out.println("最不利工况为：L(标准值)，即q="+decimalFormat.format(liveLoadOfLadderBeam)+"kN/m");
        v=5*liveLoadOfLadderBeam*stepLength*stepLength*stepLength*stepLength/384/206000/Ix/10000;
        System.out.println("最大挠度：v=5q*L^4/384EIx=5*"+decimalFormat.format(liveLoadOfLadderBeam)+"*("
                +stepLength+")^4/(384"+"*2.06^5*"+decimalFormat.format(Ix)+"*10000)="+decimalFormat.format(v)+"mm");
        if(v<=stepLength/300){
            System.out.println(decimalFormat.format(v)+"<=L/300="+decimalFormat.format(stepLength/300)+"mm");
            System.out.println("挠度满足要求"+"\n\n");
        }else{
            System.out.println("⚠️关于x轴挠度不符合要求");
            System.out.println(v+">L/*300="+decimalFormat.format(stepLength/300)+"\n\n");
        }
    }
}
