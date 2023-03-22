package cn.ShanDongUniversity.paint;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
import javax.swing.*;

public class MyPaint {
    JFrame jFrame1;
    JFrame jFrame2;
    InteractiveInterface interactiveInterface;
    int width, length, highth;
    private void dataIntiview(){
        width =(int)interactiveInterface.getWidth()/10;
        length =(int) interactiveInterface.getLength()/10;
        highth =(int) interactiveInterface.getHighth()/10;
    }

    public void initView(InteractiveInterface interactiveInterface) {
        this.interactiveInterface=interactiveInterface;
        dataIntiview();;

        jFrame1 =new JFrame("平面图");
        jFrame1.setBounds(0,0,800,800);
        jFrame1.setLayout(null);
        Ichnography ichnography=new Ichnography(interactiveInterface);
        ichnography.setBounds(0,0, 800, 800);
        jFrame1.add(ichnography);
        jFrame1.setVisible(true);

        jFrame2 = new JFrame("立面图");
        jFrame2.setBounds(0,0,800,800);
        jFrame2.setLayout(null);
        Elevation elevation = new Elevation(interactiveInterface);
        elevation.setBounds((800-length)/2,(800-highth-100)/2,(800-length)/2+length+100,(800-highth-100)/2+highth+150);
        ElevationAssistance elevationAssistance = new ElevationAssistance(interactiveInterface);
        elevationAssistance.setBounds((800-length)/2-100,(800-highth-100)/2,(800-length)/2,(800-highth-100)/2+highth+150);
        jFrame2.add(elevation);
        jFrame2.add(elevationAssistance);
        jFrame2.setVisible(true);
    }
}
