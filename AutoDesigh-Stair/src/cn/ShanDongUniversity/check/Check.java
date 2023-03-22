package cn.ShanDongUniversity.check;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
public class Check {
    InteractiveInterface interactiveInterface;
    public Check(InteractiveInterface interactiveInterface) {
        this.interactiveInterface=interactiveInterface;
    }

    public void checkStepLength(){
        double width = interactiveInterface.getWidth();
        double visitorsFolwRate = interactiveInterface.getVisitorsFolwRate();
        if(visitorsFolwRate==1){
            if(width<1050*2+100){
                throw new RuntimeException("净开间过小，无法设计出单个踏步长度符合人流量的平行双跑楼梯");
            }
        }

        if(visitorsFolwRate==2){
            if(width<1100*2+100){
                throw new RuntimeException("净开间过小，无法设计出单个踏步长度符合人流量的平行双跑楼梯");
            }
        }

        if(visitorsFolwRate==3){
            if(width<1650*2+100){
                throw new RuntimeException("净开间过小，无法设计出单个踏步长度符合人流量的平行双跑楼梯");
            }
        }
    }
}
