package cn.ShanDongUniversity.paint;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class ElevationAssistance extends Canvas{
    Graphics2D graphics2D=null;
    InteractiveInterface interactiveInterface;
    int width, length, highth, platformWidth, stepLength, stairShaftWidth, stepWidth, stepHighth, stepNumbers, thicknessOfPlatform, thicknessOfStep=0;

    public ElevationAssistance(InteractiveInterface interactiveInterface){
        this.interactiveInterface=interactiveInterface;
        width =(int)interactiveInterface.getWidth()/10;
        length =(int) interactiveInterface.getLength()/10;
        highth =(int) interactiveInterface.getHighth()/10;
        platformWidth =(int) interactiveInterface.getPlatformWidth()/10;
        stepLength=(int) interactiveInterface.getStepLength()/10;
        stairShaftWidth =(int) interactiveInterface.getStairShaftWidth()/10;
        stepWidth =(int) interactiveInterface.getStepWidth()/10;
        stepHighth =(int) interactiveInterface.getStepHighth()/10;
        stepNumbers =(int) interactiveInterface.getStepNumbers();
        thicknessOfPlatform= (int)(interactiveInterface.getThicknessOfPlatformConcrete()+interactiveInterface.getThicknessOfPlatformUpperPlate()+ interactiveInterface.getThicknessOfPlatformLowerPlate())/10;
        thicknessOfStep=(int)(interactiveInterface.getThicknessOfConcrete()+interactiveInterface.getThicknessOfLowerPlate()+interactiveInterface.getThicknessOfUpperPlate())/10;
    }

    public void paint(Graphics g) {
        super.paint(g);
        graphics2D = (Graphics2D) g;
        BasicStroke stokeLine = new BasicStroke(2.0f);
        graphics2D.setStroke(stokeLine);
        graphics2D.drawLine(60,100+highth-stepNumbers*stepHighth,60,100+highth);
        graphics2D.drawLine(40,100+highth-stepNumbers*stepHighth,80,100+highth-stepNumbers*stepHighth);
        graphics2D.drawLine(40,100+highth,80,100+highth);
        graphics2D.drawLine(40,100+highth/2,80,100+highth/2);
        graphics2D.drawLine(50,100+highth-stepNumbers*stepHighth+10,70,100+highth-stepNumbers*stepHighth-10);
        graphics2D.drawLine(50,100+highth+10,70,100+highth-10);
        graphics2D.drawLine(50,100+highth/2+10,70,100+highth/2-10);

        graphics2D.drawLine(30,100+highth-stepNumbers*stepHighth,30,100+highth);
        graphics2D.drawLine(10,100+highth-stepNumbers*stepHighth,50,100+highth-stepNumbers*stepHighth);
        graphics2D.drawLine(10,100+highth,50,100+highth);
        graphics2D.drawLine(20,100+highth-stepNumbers*stepHighth+10,40,100+highth-stepNumbers*stepHighth-10);
        graphics2D.drawLine(20,100+highth+10,40,100+highth-10);

        Font font = new Font(null, Font.PLAIN, 15);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(270), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        graphics2D.setFont(rotatedFont);

        graphics2D.drawString(String.valueOf(highth/2*10),50,100+highth/4+10);
        graphics2D.drawString(String.valueOf(highth/2*10),50,100+highth*3/4+10);
        graphics2D.drawString(String.valueOf(highth*10),20,100+highth/2+10);

        //对Elevation的补充
        graphics2D.setStroke(stokeLine);
        graphics2D.drawLine(100,highth+100+20,100,highth+100+60);
        graphics2D.drawLine(100,highth+100+50,100,highth+100+90);
        graphics2D.drawLine(90,highth+100+50,100,highth+100+40);
        graphics2D.drawLine(90,highth+100+80,100,highth+100+70);
    }
}
