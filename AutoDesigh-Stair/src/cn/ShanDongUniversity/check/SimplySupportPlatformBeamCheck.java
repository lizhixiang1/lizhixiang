package cn.ShanDongUniversity.check;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
import cn.ShanDongUniversity.material.concrete.C30;
import cn.ShanDongUniversity.material.h_bar.H;
import cn.ShanDongUniversity.material.steelPlate.SteelPlate;
import java.text.DecimalFormat;

public class SimplySupportPlatformBeamCheck {
    double thicknessOfPlatformUpperPlate,thicknessOfPlatformLowerPlate,thicknessOfPlatformConcrete,p_upperPlate, p_lowerPlate, p_C30, platformSelfWeight, platformBeamSelfWeight,
            permanentLineLoadOfPlatformBeam, permanentConcentrationLoadFromLadderBeam, liveLoadOfPlatform, liveLoadFormLadderBeam, platfromWidth,
            staircaseWidth, platformBeamSpan, worstCondition1,worstCondition2, combincedValoeOfLineLoadInPlatform, combincedValoeOfConcentrationLoadInPlatform, maxMoment,
            V, T, Ix, Wx, h1, b, t1, t2, Sx, fy, fv, v, Wenx, F1, F2;
    String kindOfPlatFormBeam;
    InteractiveInterface interactiveInterface;
    SimplySupportSteelBeanCheck simplySupportSteelBeanCheck;

    public void check(InteractiveInterface interactiveInterface, SimplySupportSteelBeanCheck simplySupportSteelBeanCheck){
        thicknessOfPlatformConcrete = interactiveInterface.getThicknessOfPlatformConcrete();
        thicknessOfPlatformUpperPlate = interactiveInterface.getThicknessOfPlatformUpperPlate();
        thicknessOfPlatformLowerPlate = interactiveInterface.getThicknessOfPlatformLowerPlate();
        kindOfPlatFormBeam = interactiveInterface.getKindOfPlatFormBeam();
        this.interactiveInterface=interactiveInterface;
        this.simplySupportSteelBeanCheck=simplySupportSteelBeanCheck;
        calculate();
    }

    public void calculate(){
        SteelPlate steelPlate = new SteelPlate();
        C30 concrete = new C30();
        H h = new H();
        Ix = h.getIx(kindOfPlatFormBeam);
        Wx = h.getWx(kindOfPlatFormBeam);
        fv = h.getFv();
        fy = h.getFy();
        h1 = h.getH(kindOfPlatFormBeam);
        b = h.getB(kindOfPlatFormBeam);
        t1 = h.getT1(kindOfPlatFormBeam);
        t2 = h.getT2(kindOfPlatFormBeam);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        try {
            p_upperPlate = steelPlate.getP(thicknessOfPlatformUpperPlate);
            p_lowerPlate = steelPlate.getP(thicknessOfPlatformLowerPlate);
            p_C30 = concrete.getP();

        } catch (Exception e) {
            System.out.println("\n\n\n上述输入的数据有不符合规范之处请重新输入");
            interactiveInterface.display4();
        }

        System.out.println("3.平台梁验算");
        System.out.println("3.1.荷载计算");
        System.out.println("永久荷载");
        platfromWidth = interactiveInterface.getPlatformWidth();
        staircaseWidth = interactiveInterface.getWidth();
        platformBeamSpan=staircaseWidth+240;
        platformSelfWeight=(platfromWidth/1000*staircaseWidth/1000*(p_lowerPlate+p_upperPlate) +platfromWidth/1000
                *staircaseWidth/1000*thicknessOfPlatformConcrete/1000*p_C30)*9.8/2/staircaseWidth;
        System.out.println("平台板面自重线荷载： 上下层钢板以及混凝土的总重力/（2*楼梯间净开间）="
                +decimalFormat.format(platformSelfWeight)+"kN/m");
        double q = h.getQ(kindOfPlatFormBeam);
        platformBeamSelfWeight=q*9.8/1000;
        System.out.println("平台梁自重："+decimalFormat.format(platformBeamSelfWeight)+"kN/m");
        permanentLineLoadOfPlatformBeam=platformSelfWeight+platformBeamSelfWeight;
        System.out.println("永久均布荷载汇集："+decimalFormat.format(platformBeamSelfWeight)+"+"
                +decimalFormat.format(platformSelfWeight)+"="+decimalFormat.format(permanentLineLoadOfPlatformBeam)+"kN/m");
        double ladderBeamSpan = simplySupportSteelBeanCheck.getLadderBeamSpan();
        double liveLoadOfLadderBeam = simplySupportSteelBeanCheck.getLiveLoadOfLadderBeam();
        double permanentLoadOfLadderBeam = simplySupportSteelBeanCheck.getPermanentLoadOfLadderBeam();
        permanentConcentrationLoadFromLadderBeam=permanentLoadOfLadderBeam*ladderBeamSpan/2;
        System.out.println("梯梁传来的集中力: "+decimalFormat.format(permanentLoadOfLadderBeam) +"*"
                +decimalFormat.format(ladderBeamSpan)+"/2="+decimalFormat.format(permanentConcentrationLoadFromLadderBeam)+"kN");
        System.out.println("活荷载");
        liveLoadOfPlatform=3.5*platfromWidth/1000/2;
        System.out.println("平台板活荷载："+3.5+"*"+decimalFormat.format(platfromWidth/1000)
                +"/2="+decimalFormat.format(liveLoadOfPlatform)+"KN/m");
        liveLoadFormLadderBeam=liveLoadOfLadderBeam*ladderBeamSpan/2;
        System.out.println("梯梁传来的集中力活荷载："+decimalFormat.format(liveLoadOfLadderBeam)
                +"*"+decimalFormat.format(ladderBeamSpan)+"/2="+decimalFormat.format(liveLoadFormLadderBeam)+"kN\n\n");

        System.out.println("3.2.受弯强度验算");
        System.out.println("最不利工况为1.35D+1.4*0.7*L");
        combincedValoeOfLineLoadInPlatform=1.3*permanentLineLoadOfPlatformBeam+1.4*0.7*liveLoadOfPlatform;
        System.out.println("均布荷载组合值：1.35*"+decimalFormat.format(permanentLineLoadOfPlatformBeam)+"+1.4*0.7*"
                +decimalFormat.format(liveLoadOfPlatform)+"="+decimalFormat.format(combincedValoeOfLineLoadInPlatform)+"kN/m");
        combincedValoeOfConcentrationLoadInPlatform=1.3*permanentConcentrationLoadFromLadderBeam+1.4*0.7*liveLoadFormLadderBeam;
        System.out.println("集中荷载组合值：1.35*"+decimalFormat.format(permanentConcentrationLoadFromLadderBeam)+"+1.4*0.7*"
                +decimalFormat.format(liveLoadFormLadderBeam)+"="+decimalFormat.format(combincedValoeOfConcentrationLoadInPlatform)+"kN");
        maxMoment=combincedValoeOfLineLoadInPlatform*platformBeamSpan/1000*platformBeamSpan/1000/8
                +combincedValoeOfConcentrationLoadInPlatform*150/1000+combincedValoeOfConcentrationLoadInPlatform
                *(platformBeamSpan-interactiveInterface.getStairShaftWidth())/2/1000;
        System.out.println("跨中处最大弯矩为："+decimalFormat.format(maxMoment)+"KN*m");
        System.out.println("截面塑性发展系数：rx=1.05(最不利轴为x轴)");
        Wenx = 0.98 * h.getWx(kindOfPlatFormBeam);
        System.out.println("考虑净截面折减：Wenx=0.98*Wx=" + decimalFormat.format(Wenx) + "cm3");
        F1 = maxMoment / Wenx / 1.05 * 1000;
        F2 = -maxMoment / Wenx / 1.05 * 1000;
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

        System.out.println("3.3.受剪强度验算");
        worstCondition1=1.3*permanentConcentrationLoadFromLadderBeam*4+1.3*permanentLineLoadOfPlatformBeam*staircaseWidth/1000
                +1.5*liveLoadFormLadderBeam*4+1.5*liveLoadOfPlatform*staircaseWidth/1000;
        System.out.println("最不利工况为1.3D+1.5L，即q=" + decimalFormat.format(permanentConcentrationLoadFromLadderBeam)
                + "*1.3*4+" + decimalFormat.format(permanentLineLoadOfPlatformBeam)+"*"+staircaseWidth/1000+"*1.3+"+ "1.5*4*"
                +decimalFormat.format(liveLoadFormLadderBeam)+"+1.5*"+staircaseWidth/1000+"*"+decimalFormat.format(liveLoadOfPlatform)
                +"="+decimalFormat.format(worstCondition1) + "kN");
        V = worstCondition1/2;
        System.out.println("最大剪力为： V=qL/2=" + decimalFormat.format(V) + "kN（支座）");
        Sx = t2*b*(h1/2-t2/2)/1000+t1*(h1/2-t2)*(h1/2-t2)/2/1000;
        T = V * Sx / Ix / t1 * 100;
        if(T< fv){
            System.out.println("剪应力t=V*Sx/Ix*tw=" + decimalFormat.format(V) + "*" + decimalFormat.format(Sx)
                    + "/(" + decimalFormat.format(Ix) + "*" + decimalFormat.format(t1) + ")*100=" + decimalFormat.format(T)
                    + "N/mm2 < "+ this.fv +"N/mm2");
        }else{
            System.out.println("剪应力t=V*Sx/Ix*tw=" + decimalFormat.format(V) + "*" + decimalFormat.format(Sx)
                    + "/(" + decimalFormat.format(Ix) + "*" + decimalFormat.format(t1) + ")*100=" + decimalFormat.format(T)
                    + "N/mm2 >= "+ this.fv +"N/mm2");
            System.out.println("⚠️不合格");
        }
        System.out.println("应力比为："+decimalFormat.format(T)+"/"+ this.fv +"="+decimalFormat.format(T/ this.fv)+"\n\n");


        System.out.println("3.4.x轴挠度验算");
        worstCondition2=permanentLineLoadOfPlatformBeam+liveLoadOfPlatform;
        System.out.println("最不利工况为：D+L(标准组合)，即q="+decimalFormat.format(permanentLineLoadOfPlatformBeam)
                +"+"+decimalFormat.format(liveLoadOfPlatform)+"="+decimalFormat.format(worstCondition2)+"kN/m");
        v=5*worstCondition2*platformBeamSpan*platformBeamSpan*platformBeamSpan*platformBeamSpan/384/206000/Ix/10000;
        System.out.println("最大挠度：v=5qL^4/384EIx=5*"+decimalFormat.format(worstCondition2)+"*"
                +decimalFormat.format(platformBeamSpan)+"^4/(384"+"*2.06^5*"+Ix+"*10000)="+decimalFormat.format(v)+"mm(跨中)");
        System.out.println("根据结l构力学精算手册，两侧梯梁传来的集中力作用下跨中挠度");
        worstCondition2=permanentConcentrationLoadFromLadderBeam+liveLoadFormLadderBeam;
        System.out.println("集中荷载："+decimalFormat.format(permanentConcentrationLoadFromLadderBeam)+"+"
                +decimalFormat.format(liveLoadFormLadderBeam)+"="+decimalFormat.format(worstCondition2)+"kN");
        double v1=worstCondition2*1000*120*(3*platformBeamSpan*platformBeamSpan-4*120*120)/(24*206000*Ix*10000);
        System.out.println("最大挠度：v1=qa(3l^2-4a^2)/24EI="+decimalFormat.format(worstCondition2*1000)
                +"*120*(3*"+decimalFormat.format(platformBeamSpan)
                +"^2-4*120^2)"+"/(24"+"*2.06^5*"+Ix+"*10000)="+decimalFormat.format(v1)+"mm(跨中)");
        double x=(platformBeamSpan/2-interactiveInterface.getStairShaftWidth()/2);
        double v2=worstCondition2*1000*x*(3*platformBeamSpan*platformBeamSpan-4*x*x)/(24*206000*Ix*10000);
        System.out.println("最大挠度：v2=qa(3l^2-4a^2)/24EI="+decimalFormat.format(worstCondition2*1000)
                +"*"+x+"*(3*"+decimalFormat.format(platformBeamSpan)+"^2-4*"+x+"^2)"
                +"/(24"+"*2.06^5*"+Ix+"*10000)="+decimalFormat.format(v2)+"mm(跨中)");
        v=v+v1+v2;
        if(v<=platformBeamSpan/250){
            System.out.println(decimalFormat.format(v)+"<=L/250="+decimalFormat.format(platformBeamSpan/250)+"mm");
            System.out.println("挠度满足要求");
        }else{
            System.out.println("⚠️关于x轴挠度不符合要求");
            System.out.println(decimalFormat.format(v)+">L/250="+decimalFormat.format(platformBeamSpan/250));
        }
        worstCondition2=liveLoadOfPlatform;
        System.out.println("最不利工况为：L(标准组合)，即q="+"="+decimalFormat.format(worstCondition2)+"kN/m");
        v=5*worstCondition2*platformBeamSpan*platformBeamSpan*platformBeamSpan*platformBeamSpan/384/206000/Ix/10000;
        System.out.println("最大挠度：v=5qL^4/384EIx=5*"+decimalFormat.format(worstCondition2)+"*"+decimalFormat.format(platformBeamSpan)+"^4/(384"+"*2.06^5*"+Ix+"*10000)="+decimalFormat.format(v)+"mm(跨中)");
        System.out.println("根据结l构力学精算手册，两侧梯梁传来的集中力作用下跨中挠度");
        worstCondition2=liveLoadFormLadderBeam;
        System.out.println("集中荷载："+decimalFormat.format(permanentConcentrationLoadFromLadderBeam)+"+"+decimalFormat.format(liveLoadFormLadderBeam)+"="+decimalFormat.format(worstCondition2)+"kN");
        v1=worstCondition2*1000*120*(3*platformBeamSpan*platformBeamSpan-4*120*120)/(24*206000*Ix*10000);
        System.out.println("最大挠度：v1=qa(3l^2-4a^2)/24EI="+decimalFormat.format(worstCondition2*1000)+"*120*(3*"+decimalFormat.format(platformBeamSpan)+"^2-4*120^2)"+"/(24"+"*2.06^5*"+Ix+"*10000)="+decimalFormat.format(v1)+"mm(跨中)");
        x=(platformBeamSpan/2-interactiveInterface.getStairShaftWidth()/2);
        v2=worstCondition2*1000*x*(3*platformBeamSpan*platformBeamSpan-4*x*x)/(24*206000*Ix*10000);
        System.out.println("最大挠度：v2=qa(3l^2-4a^2)/24EI="+decimalFormat.format(worstCondition2*1000)+"*"+x+"*(3*"+decimalFormat.format(platformBeamSpan)+"^2-4*"+x+"^2)"+"/(24"+"*2.06^5*"+Ix+"*10000)="+decimalFormat.format(v2)+"mm(跨中)");
        v=v+v1+v2;
        if(v<=platformBeamSpan/300){
            System.out.println(decimalFormat.format(v)+"<=L/250="+decimalFormat.format(platformBeamSpan/300)+"mm");
            System.out.println("挠度满足要求");
        }else{
            System.out.println("⚠️关于x轴挠度不符合要求");
            System.out.println(decimalFormat.format(v)+">L/250="+decimalFormat.format(platformBeamSpan/300));
        }
    }
}
