package cn.ShanDongUniversity.material.h_bar;

import cn.ShanDongUniversity.material.u_Bar.C;

import java.lang.reflect.Field;

public class H {
    double q_HN_300_150_65_9=37.3,Ix_HN_300_150_65_9=7350, Wx_HN_300_150_65_9=490, ix_HN_300_150_65_9=12.4,
            h_HN_300_150_65_9=300, b_HN_300_150_65_9=150, t1_HN_300_150_65_9=6.5, t2_HN_300_150_65_9=9;
    double q_HN_350_175_7_11=50, Ix_HN_350_175_7_11=13700, Wx_HN_350_175_7_11=782, ix_HN_350_175_7_11=14.7,
            h_HN_350_175_7_11=350, b_HN_350_175_7_11=175, t1_HN_350_175_7_11=7, t2_HN_350_175_7_11=11;
    double q_HN_250_125_6_9=29.7, Ix_HN_250_125_6_9=4080, Wx_HN_250_125_6_9=326, ix_HN_250_125_6_9=10.4,
            h_HN_250_125_6_9=250, b_HN_250_125_6_9=125, t1_HN_250_125_6_9=6, t2_HN_250_125_6_9=9;
    double q_HN_200_100_55_8=21.7, Ix_HN_200_100_55_8=1880, Wx_HN_200_100_55_8=188, ix_HN_200_100_55_8=8.25,
            h_HN_200_100_55_8=200, b_HN_200_100_55_8=100, t1_HN_200_100_55_8=5.5, t2_HN_200_100_55_8=8;
    double q_HN_125_60_6_8=13.3, Ix_HN_125_60_6_8=417, Wx_HN_125_60_6_8=66.8, ix_HN_125_60_6_8=4.95,
            h_HN_125_60_6_8=125, b_HN_125_60_6_8=60, t1_HN_125_60_6_8=6, t2_HN_125_60_6_8=8;
    double q_HN_150_75_5_7=14.3, Ix_HN_150_75_5_7=679, Wx_HN_150_75_5_7=90.6, ix_HN_150_75_5_7=6.12,
            h_HN_150_75_5_7=150, b_HN_150_75_5_7=75, t1_HN_150_75_5_7=5, t2_HN_150_75_5_7=7;

    //单位为N/mm2
    double fy=215;
    double fv=125;

    public double getQ(String x){
        if(x.equals("HN300*150*6.5*9")){
            return q_HN_300_150_65_9;
        }else if(x.equals("HN350*175*7*11")){
            return q_HN_350_175_7_11;
        }else if(x.equals("HN250*125*6*9")){
            return q_HN_250_125_6_9;
        }else if(x.equals("HN200*100*5.5*8")){
            return q_HN_200_100_55_8;
        }else if(x.equals("HN125*60*6*8")){
            return q_HN_125_60_6_8;
        }else if(x.equals("HN150*75*5*7")){
            return q_HN_150_75_5_7;
        }
        throw new RuntimeException("不存在这种类型的钢的数据");
    }

    public double getIx(String x){
        if(x.equals("HN300*150*6.5*9")){
            return Ix_HN_300_150_65_9;
        }else if(x.equals("HN350*175*7*11")){
            return Ix_HN_350_175_7_11;
        }else if(x.equals("HN250*125*6*9")){
            return Ix_HN_250_125_6_9;
        }else if(x.equals("HN200*100*5.5*8")){
            return Ix_HN_200_100_55_8;
        }else if(x.equals("HN125*60*6*8")){
            return Ix_HN_125_60_6_8;
        }else if(x.equals("HN150*75*5*7")){
            return Ix_HN_150_75_5_7;
        }
        throw new RuntimeException("不存在这种类型的钢的数据");
    }

    public double getWx(String x){
        if(x.equals("HN300*150*6.5*9")){
            return Wx_HN_300_150_65_9;
        }else if(x.equals("HN350*175*7*11")){
            return Wx_HN_350_175_7_11;
        }else if(x.equals("HN250*125*6*9")){
            return Wx_HN_250_125_6_9;
        }else if(x.equals("HN200*100*5.5*8")){
            return Wx_HN_200_100_55_8;
        }else if(x.equals("HN125*60*6*8")){
            return Wx_HN_125_60_6_8;
        }else if(x.equals("HN150*75*5*7")){
            return Wx_HN_150_75_5_7;
        }
        throw new RuntimeException("不存在这种类型的钢的数据");
    }

    public double getix(String x){
        if(x.equals("HN300*150*6.5*9")){
            return ix_HN_300_150_65_9;
        }else if(x.equals("HN350*175*7*11")){
            return ix_HN_350_175_7_11;
        }else if(x.equals("HN250*125*6*9")){
            return ix_HN_250_125_6_9;
        }else if(x.equals("HN200*100*5.5*8")){
            return ix_HN_200_100_55_8;
        }else if(x.equals("HN125*60*6*8")){
            return ix_HN_125_60_6_8;
        }else if(x.equals("HN150*75*5*7")){
            return ix_HN_150_75_5_7;
        }
        throw new RuntimeException("不存在这种类型的钢的数据");
    }

    public double getH(String x){
        if(x.equals("HN300*150*6.5*9")){
            return h_HN_300_150_65_9;
        }else if(x.equals("HN350*175*7*11")){
            return h_HN_350_175_7_11;
        }else if(x.equals("HN250*125*6*9")){
            return h_HN_250_125_6_9;
        }else if(x.equals("HN200*100*5.5*8")){
            return h_HN_200_100_55_8;
        }else if(x.equals("HN125*60*6*8")){
            return h_HN_125_60_6_8;
        }else if(x.equals("HN150*75*5*7")){
            return h_HN_150_75_5_7;
        }
        throw new RuntimeException("不存在这种类型的钢的数据");
    }

    public double getB(String x){
        if(x.equals("HN300*150*6.5*9")){
            return b_HN_300_150_65_9;
        }else if(x.equals("HN350*175*7*11")){
            return b_HN_350_175_7_11;
        }else if(x.equals("HN250*125*6*9")){
            return b_HN_250_125_6_9;
        }else if(x.equals("HN200*100*5.5*8")){
            return b_HN_200_100_55_8;
        }else if(x.equals("HN125*60*6*8")){
            return b_HN_125_60_6_8;
        }else if(x.equals("HN150*75*5*7")){
            return b_HN_150_75_5_7;
        }
        throw new RuntimeException("不存在这种类型的钢的数据");
    }

    public double getT1(String x){
        if(x.equals("HN300*150*6.5*9")){
            return t1_HN_300_150_65_9;
        }else if(x.equals("HN350*175*7*11")){
            return t1_HN_350_175_7_11;
        }else if(x.equals("HN250*125*6*9")){
            return t1_HN_250_125_6_9;
        }else if(x.equals("HN200*100*5.5*8")) {
            return t1_HN_200_100_55_8;
        }else if(x.equals("HN125*60*6*8")){
            return t1_HN_125_60_6_8;
        }else if(x.equals("HN150*75*5*7")){
            return t1_HN_150_75_5_7;
        }
        throw new RuntimeException("不存在这种类型的钢的数据");
    }

    public double getT2(String x){
        if(x.equals("HN300*150*6.5*9")){
            return t2_HN_300_150_65_9;
        }else if(x.equals("HN350*175*7*11")){
            return t2_HN_350_175_7_11;
        }else if(x.equals("HN250*125*6*9")){
            return t2_HN_250_125_6_9;
        }else if(x.equals("HN200*100*5.5*8")){
            return t2_HN_200_100_55_8;
        }else if(x.equals("HN125*60*6*8")){
            return t2_HN_125_60_6_8;
        }else if(x.equals("HN150*75*5*7")){
            return t2_HN_150_75_5_7;
        }
        throw new RuntimeException("不存在这种类型的钢的数据");
    }

    public double getFy() {
        return fy;
    }

    public double getFv() {
        return fv;
    }
}
