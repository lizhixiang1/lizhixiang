package cn.ShanDongUniversity.check;
import cn.ShanDongUniversity.design.StepDesign;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
import cn.ShanDongUniversity.material.concrete.C30;
import cn.ShanDongUniversity.material.steelPlate.SteelPlate;
import cn.ShanDongUniversity.material.u_Bar.C;

import java.text.DecimalFormat;
public class SimplySupportSteelBeanCheck {
    InteractiveInterface interactiveInterface;
    double thicknessOfUpperPlate, thicknessOfLowerPlate, thicknessOfConcrete, p_upperPlate, p_lowerPlate, p_C30, p, steelWeight,
            concreteWeight, ladderBeamWeight,cosx, permanentLoadOfLadderBeam, liveLoadOfLadderBeam, worstCondition1, worstCondition2,
            maxingBendingMoment, Wenx, F2,F1, V, T, Sx, v, h, b, t, ix, d, fy, fv, ladderBeamSpan;
    String kindOfChannleBar2,kindOfChannleBar;
    public void check(InteractiveInterface interactiveInterface){
        thicknessOfConcrete = interactiveInterface.getThicknessOfConcrete();
        thicknessOfLowerPlate = interactiveInterface.getThicknessOfLowerPlate();
        thicknessOfUpperPlate = interactiveInterface.getThicknessOfUpperPlate();
        kindOfChannleBar = interactiveInterface.getKindOfChannleBar();
        kindOfChannleBar2 = interactiveInterface.getKindOfChannleBar2();
        this.interactiveInterface=interactiveInterface;
        calculate();

    }

    public void calculate() {
        SteelPlate steelPlate = new SteelPlate();
        C30 concrete = new C30();
        C c = new C();
        h = c.getH(kindOfChannleBar);
        b = c.getB(kindOfChannleBar);
        ix = c.getIx(kindOfChannleBar);
        d = c.getD(kindOfChannleBar);
        p = c.getP(kindOfChannleBar);
        fy = c.getFy();
        fv = c.getFv();
        Wenx = 0.98 * c.getWx(kindOfChannleBar);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        p_upperPlate = steelPlate.getP(thicknessOfUpperPlate);
        p_lowerPlate = steelPlate.getP(thicknessOfLowerPlate);
        p_C30 = concrete.getP();
        System.out.println("2.梯梁验算");
        System.out.println("2.1.荷载计算");
        System.out.println("永久荷载");
        double stepLength = interactiveInterface.getStepLength();
        double stepHighth = interactiveInterface.getStepHighth();
        double stepNumbers = interactiveInterface.getStepNumbers();
        double stepWidth = interactiveInterface.getStepWidth();
        steelWeight = stepLength / 1000 * (stepWidth / 1000 + stepHighth / 1000) * stepNumbers / 2
                * (p_upperPlate + p_lowerPlate) * 9.8 / (stepNumbers / 2 * stepWidth / 1000 * 2 * 1000);
        System.out.println("钢板自重：" + stepLength / 1000 + "*(" + stepWidth / 1000 + "+"
                + decimalFormat.format(stepHighth / 1000) + ")*" + stepNumbers / 2
                + "*(" + p_upperPlate + "+" + p_lowerPlate + ")*" + "9.8" + "/" + "(" + stepNumbers / 2 + "*"
                + stepWidth / 1000 + "*1000" + "*2)" + "=" + decimalFormat.format(steelWeight) + "kN/m");
        concreteWeight = stepLength / 1000 * (stepWidth / 1000 + stepHighth / 1000) * stepNumbers / 2
                * thicknessOfConcrete / 1000 * p_C30 * 9.8 / (stepNumbers / 2 * stepWidth / 1000 * 2 * 1000);
        System.out.println("混凝土自重： " + stepLength / 1000 + "*(" + stepWidth / 1000 + "+"
                + decimalFormat.format(stepHighth / 1000) + ")*" + stepNumbers / 2 + "*"
                + thicknessOfConcrete / 1000 + "*" + p_C30 + "*9.8" + "/" + "(" + stepNumbers / 2 + "*"
                + stepWidth / 1000 + "*1000" + "*2)" + "=" + decimalFormat.format(concreteWeight) + "kN/m");
        System.out.println("楼梯栏杆线荷载： 1.2KN/m");
        ladderBeamWeight = p * 9.8 / 1000;
        System.out.println("梯梁自重：" + decimalFormat.format(p) + "*9.8/1000="
                + decimalFormat.format(ladderBeamWeight) + "kN/m");
        permanentLoadOfLadderBeam = steelWeight + concreteWeight + 1.2 + ladderBeamWeight;
        System.out.println("永久荷载汇集（折算成线荷载）：" + decimalFormat.format(steelWeight) + "+"
                + decimalFormat.format(concreteWeight) + "+1.2+" + decimalFormat.format(ladderBeamWeight) + "="
                + decimalFormat.format(permanentLoadOfLadderBeam) + "KN/m");
        liveLoadOfLadderBeam = 3.5 * stepLength / (1000 * 2);
        System.out.println("活荷载汇集：" + "3.5*" + stepLength / 1000 + "/2" + "="
                + decimalFormat.format(liveLoadOfLadderBeam) + "kN/m" + "\n\n");


        System.out.println("2.2受弯强度验算");
        worstCondition1 = 1.3 * permanentLoadOfLadderBeam + 1.5 * liveLoadOfLadderBeam;
        System.out.println("最不利工况为1.3D+1.5L，即q=" + decimalFormat.format(permanentLoadOfLadderBeam)
                + "*1.3+" + decimalFormat.format(liveLoadOfLadderBeam) + "*1.5="
                + decimalFormat.format(worstCondition1) + "KN/m");
        cosx = stepWidth / Math.sqrt(stepWidth * stepWidth + stepHighth * stepHighth);
        ladderBeamSpan=stepNumbers*stepWidth/2/1000/cosx;
        System.out.println("梯梁与水平面的夹角为x，且cosx=" + decimalFormat.format(cosx));
        maxingBendingMoment = worstCondition1 * (stepNumbers / 2) * stepWidth / 1000
                * (stepNumbers / 2) * stepWidth / 1000 / 8 / cosx;
        System.out.println("最大弯矩为：qcosx*(L/cosx)^2/8=" + decimalFormat.format(worstCondition1) + "*"
                + decimalFormat.format(stepNumbers / 2 * stepWidth / 1000 * stepNumbers / 2 * stepWidth / 1000)
                + "/(8*" + decimalFormat.format(cosx) + ")=" + decimalFormat.format(maxingBendingMoment) + "kN*m（跨中）");
        System.out.println("截面塑性发展系数：rx=1.05(最不利轴为x轴)");
        System.out.println("考虑净截面折减：Wenx=0.98*Wx=" + decimalFormat.format(Wenx) + "cm3");
        F1 = maxingBendingMoment / Wenx / 1.05 * 1000;
        F2 = -maxingBendingMoment / Wenx / 1.05 * 1000;
        if(F1<fy){
            System.out.println("a1=M/(Wnex*rx)=" + decimalFormat.format(F1) + "N/mm2 < "+fy+"N/mm2");
        }else{
            System.out.println("a1=M/(Wnex*rx)=" + decimalFormat.format(F1) + "N/mm2 >= "+fy+"N/mm2");
            System.out.println("⚠️不合格");
        }
        if(-F2<fy){
            System.out.println("a2=-M/(Wnex*rx)=" + decimalFormat.format(F2) + "N/mm2 < "+fy+"N/mm2");
        }else{
            System.out.println("a2=-M/(Wnex*rx)=" + decimalFormat.format(F2) + "N/mm2 >= "+fy+"N/mm2");
            System.out.println("⚠️不合格");
        }
        System.out.println("应力比为："+decimalFormat.format(F1)+"/"+fy+"="+decimalFormat.format(F1/fy)+"\n\n");


        System.out.println("2.3.受剪强度验算");
        System.out.println("最不利工况为1.3D+1.5L，即q=" + decimalFormat.format(permanentLoadOfLadderBeam) + "*1.3+"
                + decimalFormat.format(liveLoadOfLadderBeam) + "*1.5=" + decimalFormat.format(worstCondition1) + "kN/m");
        V = worstCondition1 * (stepNumbers / 2) * stepWidth / 1000 / 2;
        System.out.println("最大剪力为： V=qcosx*(L/cosx)/2=" + decimalFormat.format(V) + "kN（支座）");
        try{
            t = c.getT(kindOfChannleBar);
            Sx = (h / 2 - t) * d * (h / 2 - t) / 2000 + t * b * (h / 2 - t / 2) / 1000;
        }catch (Exception e){
            Sx=b*h/2*h/4000;
        }
        T = V * Sx / ix / d * 100;
        if(T<fv){
            System.out.println("剪应力t=V*Sx/Ix*tw=" + decimalFormat.format(V) + "*" + decimalFormat.format(Sx)
                    + "/(" + decimalFormat.format(ix) + "*" + decimalFormat.format(d) + ")*100="
                    + decimalFormat.format(T) + "N/mm2 < "+fv+"N/mm2");
        }else{
            System.out.println("剪应力t=V*Sx/Ix*tw=" + decimalFormat.format(V) + "*" + decimalFormat.format(Sx)
                    + "/(" + decimalFormat.format(ix) + "*" + decimalFormat.format(d) + ")*100="
                    + decimalFormat.format(T) + "N/mm2 >= "+fv+"N/mm2");
            System.out.println("⚠️不合格");
        }
        System.out.println("应力比为："+decimalFormat.format(T)+"/"+fv+"="+decimalFormat.format(T/fv)+"\n\n");


        System.out.println("2.4.x轴挠度验算");
        worstCondition2=permanentLoadOfLadderBeam+liveLoadOfLadderBeam;
        System.out.println("最不利工况为：D+L(标准组合)，即q="+decimalFormat.format(permanentLoadOfLadderBeam)+"+"
                +decimalFormat.format(liveLoadOfLadderBeam)+"="+decimalFormat.format(worstCondition2)+"kN/m");
        v=5*worstCondition2*cosx*(stepNumbers/2*stepWidth/cosx)*(stepNumbers/2*stepWidth/cosx)
                *(stepNumbers/2*stepWidth/cosx)*(stepNumbers/2*stepWidth/cosx)/384/206000/ix/10000;
        System.out.println("最大挠度：v=5qcosx*(L/cosx)^4/384EIx=5*"+decimalFormat.format(worstCondition2)
                +"*"+decimalFormat.format(cosx)+"*("+(stepNumbers / 2) * stepWidth+"/"+decimalFormat.format(cosx)
                +")^4/(384"+"*2.06^5*"+decimalFormat.format(ix)+"*10000)="+decimalFormat.format(v)+"mm");
        if(v<=(stepNumbers/2*stepWidth/cosx)/250){
            System.out.println(decimalFormat.format(v)+"<=L/(cosx*250)="
                    +decimalFormat.format(stepNumbers/2*stepWidth/cosx/250)+"mm");
            System.out.println("挠度满足要求");
        }else{
            System.out.println("⚠️关于x轴挠度不符合要求");
            System.out.println(decimalFormat.format(v)+">L/(cosx*250)="
                    +decimalFormat.format(stepNumbers/2*stepWidth/cosx/250));
        }
        System.out.println("最不利工况为：L(标准值)，即q="+decimalFormat.format(liveLoadOfLadderBeam)+"KN/m");
        v=5*liveLoadOfLadderBeam*cosx*(stepNumbers/2*stepWidth/cosx)*(stepNumbers/2*stepWidth/cosx)*
                (stepNumbers/2*stepWidth/cosx)*(stepNumbers/2*stepWidth/cosx)/384/206000/ix/10000;
        System.out.println("最大挠度：v=5qcosx*(L/cosx)^4/384EIx=5*"+decimalFormat.format(liveLoadOfLadderBeam)
                +"*"+decimalFormat.format(cosx)+"*("+(stepNumbers / 2) * stepWidth+"/"+decimalFormat.format(cosx)
                +")^4/(384"+"*2.06^5*"+decimalFormat.format(ix)+"*10000)="+decimalFormat.format(v)+"mm");
        if(v<=(stepNumbers/2*stepWidth/cosx)/300){
            System.out.println(decimalFormat.format(v)+"<=L/(cosx*300)="
                    +decimalFormat.format(stepNumbers/2*stepWidth/cosx/300)+"mm");
            System.out.println("挠度满足要求\n\n");
        }else{
            System.out.println("⚠️关于x轴挠度不符合要求");
            System.out.println(v+">L/(cosx*3000)="+decimalFormat.format(stepNumbers/2*stepWidth/cosx/300)+"\n\n");
        }
    }

    public double getPermanentLoadOfLadderBeam() {
        return permanentLoadOfLadderBeam;
    }
    public double getLadderBeamSpan() {
        return ladderBeamSpan;
    }
    public double getLiveLoadOfLadderBeam() {
        return liveLoadOfLadderBeam;
    }
}
