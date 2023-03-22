package cn.ShanDongUniversity.check;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
import cn.ShanDongUniversity.material.concrete.C30;
import cn.ShanDongUniversity.material.steelPlate.SteelPlate;
import java.text.DecimalFormat;

public class PlatformCheck {
    InteractiveInterface interactiveInterface;
    double thicknessOfPlatformUpperPlate,thicknessOfPlatformLowerPlate, thicknessOfPlatformConcrete,p_upperPlate,
            p_lowerPlate,p_C30,steelWeight,concreteWeight,permanentLoadOfLadderBeam,liveLoadOfLadderBeam,worstCondition1,
            worstCondition2,maxingBendingMoment,Wx,Wenx,F1,F2,V,T,Sx,Ix,v;
    public void check(InteractiveInterface interactiveInterface){
        thicknessOfPlatformConcrete = interactiveInterface.getThicknessOfPlatformConcrete();
        thicknessOfPlatformLowerPlate = interactiveInterface.getThicknessOfPlatformLowerPlate();
        thicknessOfPlatformUpperPlate = interactiveInterface.getThicknessOfPlatformUpperPlate();
        this.interactiveInterface=interactiveInterface;
        calculate();
    }

    public void calculate() {
        SteelPlate steelPlate = new SteelPlate();
        C30 concrete = new C30();
        double fv = steelPlate.getFv();
        double fy = steelPlate.getFy();
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        p_upperPlate = steelPlate.getP(thicknessOfPlatformUpperPlate);
        p_lowerPlate = steelPlate.getP(thicknessOfPlatformLowerPlate);
        p_C30 = concrete.getP();
        System.out.println("4.休息平台验算");
        System.out.println("4.1.荷载计算");
        System.out.println("永久荷载：");
        double platformWidth = interactiveInterface.getPlatformWidth();
        double staircaseWidth = interactiveInterface.getWidth();
        steelWeight = platformWidth / 1000 * staircaseWidth / 1000 * (p_lowerPlate + p_upperPlate) * 9.8 / platformWidth;
        System.out.println("钢板自重：" + decimalFormat.format(platformWidth/1000)+ "*"
                + decimalFormat.format(staircaseWidth/1000) + "*(" + decimalFormat.format(p_lowerPlate)
                +"+"+decimalFormat.format(p_upperPlate)+ ")*9.8/1000*" + decimalFormat.format(platformWidth/1000)
                + "=" + decimalFormat.format(steelWeight) + "kN/m");
        concreteWeight = platformWidth / 1000 * staircaseWidth / 1000
                * thicknessOfPlatformConcrete / 1000 * p_C30 * 9.8 / platformWidth;
        System.out.println("混凝土自重： " + decimalFormat.format(platformWidth/1000)+ "*"
                + decimalFormat.format(staircaseWidth/1000) + "*"
                + decimalFormat.format(thicknessOfPlatformConcrete/1000) +"*"+decimalFormat.format(p_C30)
                + "*9.8/1000*" + decimalFormat.format(platformWidth/1000)
                + "=" + decimalFormat.format(concreteWeight) + "kN/m");
        permanentLoadOfLadderBeam=steelWeight+concreteWeight;
        System.out.println("永久荷载汇集（折算成线荷载）：" + decimalFormat.format(steelWeight) + "+"
                + decimalFormat.format(concreteWeight) +  "=" + decimalFormat.format(permanentLoadOfLadderBeam) + "kN/m");
        liveLoadOfLadderBeam = 3.5 * staircaseWidth / 1000;
        System.out.println("活荷载汇集：" + "3.5*" + staircaseWidth / 1000 + "="
                + decimalFormat.format(liveLoadOfLadderBeam) + "KN/m" + "\n\n");


        System.out.println("4.2.受弯强度验算");
        worstCondition1 = 1.3 * permanentLoadOfLadderBeam + 1.5 * liveLoadOfLadderBeam;
        System.out.println("最不利工况为1.3D+1.5L，即q=" + decimalFormat.format(permanentLoadOfLadderBeam)
                + "*1.3+" + decimalFormat.format(liveLoadOfLadderBeam) + "*1.5="
                + decimalFormat.format(worstCondition1) + "kN/m");
        maxingBendingMoment = worstCondition1*platformWidth/1000*platformWidth/1000/8;
        System.out.println("最大弯矩为：q*L^2/8=" + decimalFormat.format(worstCondition1)+"*"
                + decimalFormat.format(platformWidth/1000*platformWidth/1000) + "/8="
                + decimalFormat.format(maxingBendingMoment) + "kN*m（跨中）");
        System.out.println("截面塑性发展系数：rx=1.05(最不利轴为x轴)");
        double thick=thicknessOfPlatformUpperPlate+thicknessOfPlatformLowerPlate+thicknessOfPlatformConcrete;
        Ix=staircaseWidth/10*thick/10*thick/10*thick/10/12;
        System.out.println("Ix="+decimalFormat.format(Ix)+"cm^4");
        Wx=staircaseWidth/10*thick/10*thick/10/6;
        System.out.println("Wx="+decimalFormat.format(Wx)+"cm^3");
        Wenx=0.98*Wx;
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

        System.out.println("4.3.受剪强度验算");
        System.out.println("最不利工况为1.3D+1.5L，即q=" + decimalFormat.format(permanentLoadOfLadderBeam) +
                "*1.3+" + decimalFormat.format(liveLoadOfLadderBeam) + "*1.5="
                + decimalFormat.format(worstCondition1) + "kN/m");
        V = worstCondition1*platformWidth/1000/2;
        System.out.println("最大剪力为： V=q*L/2=" + decimalFormat.format(V) + "kN（支座）");
        Sx=staircaseWidth*thick/2*thick/4000;;
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

        System.out.println("4.4.x轴挠度验算");
        worstCondition2=permanentLoadOfLadderBeam+liveLoadOfLadderBeam;
        System.out.println("最不利工况为：D+L(标准组合)，即q="+decimalFormat.format(permanentLoadOfLadderBeam)
                +"+"+decimalFormat.format(liveLoadOfLadderBeam)+"="+decimalFormat.format(worstCondition2)+"kN/m");
        System.out.println("Ix="+decimalFormat.format(Ix)+"cm^4");
        v=5*worstCondition2*platformWidth*platformWidth*platformWidth*platformWidth/384/206000/Ix/10000;
        System.out.println("最大挠度：v=5q*L^4/384EIx=5*"+decimalFormat.format(worstCondition2)
                +"*("+platformWidth+")^4/(384"+"*2.06^5*"+decimalFormat.format(Ix)+"*10000)="+decimalFormat.format(v)+"mm");
        if(v<=platformWidth/250){
            System.out.println(decimalFormat.format(v)+"<=L/250="+decimalFormat.format(platformWidth/250)+"mm");
            System.out.println("挠度满足要求");
        }else{
            System.out.println("⚠️关于x轴挠度不符合要求");
            System.out.println(decimalFormat.format(v)+">L/250="+decimalFormat.format(platformWidth/250)+"\n\n");
        }
        System.out.println("最不利工况为：L(标准值)，即q="+decimalFormat.format(liveLoadOfLadderBeam)+"KN/m");
        v=5*liveLoadOfLadderBeam*platformWidth*platformWidth*platformWidth*platformWidth/384/206000/Ix/10000;
        System.out.println("最大挠度：v=5q*L^4/384EIx=5*"+decimalFormat.format(liveLoadOfLadderBeam)
                +"*("+platformWidth+")^4/(384"+"*2.06^5*"+decimalFormat.format(Ix)+"*10000)="+decimalFormat.format(v)+"mm");
        if(v<=platformWidth/300){
            System.out.println(decimalFormat.format(v)+"<=L/300="+decimalFormat.format(platformWidth/300)+"mm");
            System.out.println("挠度满足要求"+"\n\n");
        }else{
            System.out.println("⚠️关于x轴挠度不符合要求");
            System.out.println(v+">L/*300="+decimalFormat.format(platformWidth/300)+"\n\n");
        }
    }


}
