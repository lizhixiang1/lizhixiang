package cn.ShanDongUniversity.material.u_Bar;

import java.lang.reflect.Field;

public class C {
    double h_22a=220, b_22a=77, d_22a=7.0, t_22a=11.5, Ix_22a=2394, Wx_22a=217.6, p_22a=24.99;
    double h_22b=220, b_22b=79, d_22b=7.0, t_22b=11.5, Ix_22b=2571, Wx_22b=233.8, p_22b=28.45;
    double h_25a=250, b_25a=78, d_25a=7.0, t_25a=12.0, Ix_25a=3359, Wx_25a=268.7, p_25a=27.410;
    double h_25b=250, b_25b=80, d_25b=9.0, t_25b=12.0, Ix_25b=3619, Wx_25b=289.6, p_25b=31.33;
    double h_25c=250, b_25c=82, d_25c=11.0, t_25c=12.0, Ix_25c=3800, Wx_25c=310.4, p_25c=35.25;
    //单位为N/mm2
    double fy=215,fv=125;

    public double getP(String x) {
        Class<C> cClass = C.class;
        String substring = x.substring(1);
        String temp="p_"+substring;
        try {
            Field declaredField = cClass.getDeclaredField(temp);
            try {
                return declaredField.getDouble(new C());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            String[] split = x.split("-");
            double b1=new Double(split[0]);
            double h1=new Double(split[1]);
            return b1/1000*h1/1000*1*7800;
        }
        throw new RuntimeException("检查输入的数据类型是否正确");
    }

    public double getWx(String x) {
        if(x.equals("C22a")){
            return Wx_22a;
        }else if(x.equals("C22b")){
            return Wx_22b;
        }else if(x.equals("C25a")){
            return Wx_25a;
        }else if(x.equals("C25b")){
            return Wx_25b;
        }else if(x.equals("C25c")){
            return Wx_25c;
        }else{
            String[] split = x.split("-");
            double b1=new Double(split[0]);
            double h1=new Double(split[1]);
            return b1*h1*h1/6000;
        }
    }

    public double getIx(String x) {
        if(x.equals("C22a")){
            return Ix_22a;
        }else if(x.equals("C22b")){
            return Ix_22b;
        }else if(x.equals("C25a")){
            return Ix_25a;
        }else if(x.equals("C25b")){
            return Ix_25b;
        }else if(x.equals("C25c")){
            return Ix_25c;
        }else{
            String[] split = x.split("-");
            double b1=new Double(split[0]);
            double h1=new Double(split[1]);
            return b1*h1*h1*h1/120000;
        }
    }

    public double getH(String x) {
        if(x.equals("C22a")){
            return h_22a;
        }else if(x.equals("C22b")){
            return h_22b;
        }else if(x.equals("C25a")){
            return h_25a;
        }else if(x.equals("C25b")){
            return h_25b;
        }else if(x.equals("C25c")){
            return h_25c;
        }else{
            String[] split = x.split("-");
            return new Double(split[1]);

        }

    }

    public double getB(String x) {
        if(x.equals("C22a")){
            return b_22a;
        }else if(x.equals("C22b")){
            return b_22b;
        }else if(x.equals("C25a")){
            return b_25a;
        }else if(x.equals("C25b")){
            return b_25b;
        }else if(x.equals("C25c")){
            return b_25c;
        }else{
            String[] split = x.split("-");
            return new Double(split[0]);

        }

    }

    public double getD(String x) {
        if(x.equals("C22a")){
            return d_22a;
        }else if(x.equals("C22b")){
            return d_22b;
        }else if(x.equals("C25a")){
            return d_25a;
        }else if(x.equals("C25b")){
            return d_25b;
        }else if(x.equals("C25c")){
            return d_25c;
        }else{
            String[] split = x.split("-");
            return new Double(split[0]);
        }

    }

    public double getT(String x) {
        if(x.equals("C22a")){
            return t_22a;
        }else if(x.equals("C22b")){
            return t_22b;
        }else if(x.equals("C25a")){
            return t_25a;
        }else if(x.equals("C25b")){
            return t_25b;
        }else if(x.equals("C25c")){
            return t_25c;
        }
        throw new RuntimeException("⚠️不存在数据");

    }

    public double getFy() {
        return fy;
    }
    public double getFv() {
        return fv;
    }
}
