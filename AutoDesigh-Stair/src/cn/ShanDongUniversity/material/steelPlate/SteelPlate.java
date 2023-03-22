package cn.ShanDongUniversity.material.steelPlate;

public class SteelPlate {
    //单位为N/mm2
    double fy=215;
    double fv=125;

    public double getP(double i) {
        return 7850*i/1000;
    }
    public double getFy() {
        return fy;
    }
    public double getFv() {
        return fv;
    }
}
