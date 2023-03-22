package cn.ShanDongUniversity.paint;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Ichnography extends Canvas {
    Graphics2D graphics2D=null;
    InteractiveInterface interactiveInterface;
    int width, length, highth, platformWidth, stepLength, stairShaftWidth, stepWidth, stepHighth, stepNumbers;

    public Ichnography(InteractiveInterface interactiveInterface){
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
    }

    public void paint(Graphics g){
        super.paint(g);
        BasicStroke  stokeLine=new BasicStroke(2.0f   );
        graphics2D=(Graphics2D)g;
        //最外层线
        graphics2D.drawLine((800-width)/2-24,(800-length)/2-24,(800-width)/2-24,length+(800-length)/2);
        graphics2D.drawLine((800-width)/2-24,(800-length)/2-24,width+(800-width)/2+24,(800-length)/2-24);
        graphics2D.drawLine(width+(800-width)/2+24,(800-length)/2-24,width+(800-width)/2+24,length+(800-length)/2);
        graphics2D.drawLine((800-width)/2-24,length+(800-length)/2,(800-width)/2,length+(800-length)/2);
        graphics2D.drawLine(width+(800-width)/2+24,length+(800-length)/2,width+(800-width)/2,length+(800-length)/2);
        //内部空间分割出来
        graphics2D.drawLine((800-width)/2,(800-length)/2,(800-width)/2,length+(800-length)/2);
        graphics2D.drawLine((800-width)/2,(800-length)/2,width+(800-width)/2,(800-length)/2);
        graphics2D.drawLine(width+(800-width)/2,(800-length)/2,width+(800-width)/2,length+(800-length)/2);
        //区分出平台
        graphics2D.drawLine((800-width)/2,platformWidth+(800-length)/2,width+(800-width)/2,platformWidth+(800-length)/2);
        //区分出梯段
        for (int i = 1; i < stepNumbers/2+1; i++) {
            graphics2D.drawLine((800-width)/2,platformWidth+i*stepWidth+(800-length)/2,stepLength+(800-width)/2,platformWidth+i*stepWidth+(800-length)/2);
            graphics2D.drawLine(stepLength+stairShaftWidth+(800-width)/2,platformWidth+i*stepWidth+(800-length)/2,width+(800-width)/2,platformWidth+i*stepWidth+(800-length)/2);
        }
        graphics2D.drawLine(stepLength+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2,stepLength+(800-width)/2,platformWidth+(800-length)/2);
        graphics2D.drawLine(stepLength+stairShaftWidth+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2,stepLength+stairShaftWidth+(800-width)/2,platformWidth+(800-length)/2);
        graphics2D.drawLine(stepLength+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2,stepLength+stairShaftWidth+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2);
        //区分出护栏
        graphics2D.drawLine(stepLength-10+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+10+(800-length)/2,stepLength-10+(800-width)/2,platformWidth-10+(800-length)/2);
        graphics2D.drawLine(stepLength+stairShaftWidth+10+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+10+(800-length)/2,stepLength+stairShaftWidth+10+(800-width)/2,platformWidth-10+(800-length)/2);
        graphics2D.drawLine(stepLength-10+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+10+(800-length)/2,stepLength+stairShaftWidth+10+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+10+(800-length)/2);
        graphics2D.drawLine(stepLength-10+(800-width)/2,platformWidth-10+(800-length)/2,stepLength+stairShaftWidth+10+(800-width)/2,platformWidth-10+(800-length)/2);
        //尺寸线标注1
        graphics2D.setStroke(stokeLine);
        graphics2D.drawLine((800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+40,stepLength+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+40);
        graphics2D.drawLine((800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+20,(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+60);
        graphics2D.drawLine((800-width)/2+width,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+20,(800-width)/2+width,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+60);
        graphics2D.drawLine(stepLength+stairShaftWidth+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+40,width+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+40);
        graphics2D.drawLine(stepLength+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+40,stepLength+stairShaftWidth+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+40);
        graphics2D.drawLine(stepLength+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+20,stepLength+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+60);
        graphics2D.drawLine(stepLength+stairShaftWidth+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+20,stepLength+stairShaftWidth+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+60);
        graphics2D.drawLine(stepLength+(800-width)/2-10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+50,stepLength+(800-width)/2+10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+30);
        graphics2D.drawLine(stepLength+stairShaftWidth+(800-width)/2-10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+50,stepLength+stairShaftWidth+(800-width)/2+10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+30);
        graphics2D.drawLine((800-width)/2-10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+50,(800-width)/2+10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+30);
        graphics2D.drawLine(width+(800-width)/2-10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+50,width+(800-width)/2+10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+30);
        //尺寸线标注2
        graphics2D.drawLine((800-width)/2-60,(800-length)/2,(800-width)/2-60,(800-length)/2+length);
        graphics2D.drawLine((800-width)/2-40,(800-length)/2,(800-width)/2-80,(800-length)/2);
        graphics2D.drawLine((800-width)/2-40,(800-length)/2+length,(800-width)/2-80,(800-length)/2+length);
        graphics2D.drawLine((800-width)/2-40,platformWidth+(800-length)/2,(800-width)/2-80,platformWidth+(800-length)/2);
        graphics2D.drawLine((800-width)/2-40,platformWidth+stepNumbers/2*stepWidth+(800-length)/2,(800-width)/2-80,platformWidth+stepNumbers/2*stepWidth+(800-length)/2);
        graphics2D.drawLine((800-width)/2-70,(800-length)/2+10,(800-width)/2-50,(800-length)/2-10);
        graphics2D.drawLine((800-width)/2-70,platformWidth+(800-length)/2+10,(800-width)/2-50,platformWidth+(800-length)/2-10);
        graphics2D.drawLine((800-width)/2-70,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+10,(800-width)/2-50,platformWidth+stepNumbers/2*stepWidth+(800-length)/2-10);
        graphics2D.drawLine((800-width)/2-70,(800-length)/2+length+10,(800-width)/2-50,(800-length)/2+length-10);
        //尺寸线标注3
        graphics2D.drawLine((800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+70,2*stepLength+stairShaftWidth+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+70);
        graphics2D.drawLine((800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+50,(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+90);
        graphics2D.drawLine(2*stepLength+stairShaftWidth+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+50,2*stepLength+stairShaftWidth+(800-width)/2,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+90);
        graphics2D.drawLine((800-width)/2-10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+80,(800-width)/2+10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+60);
        graphics2D.drawLine((800-width)/2+2*stepLength+stairShaftWidth-10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+80,(800-width)/2+2*stepLength+stairShaftWidth+10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+60);
        //尺寸线标注4
        graphics2D.drawLine((800-width)/2-90,(800-length)/2,(800-width)/2-90,(800-length)/2+length);
        graphics2D.drawLine((800-width)/2-70,(800-length)/2,(800-width)/2-110,(800-length)/2);
        graphics2D.drawLine((800-width)/2-70,(800-length)/2+length,(800-width)/2-110,(800-length)/2+length);
        graphics2D.drawLine((800-width)/2-80,(800-length)/2-10,(800-width)/2-100,(800-length)/2+10);
        graphics2D.drawLine((800-width)/2-80,(800-length)/2+length-10,(800-width)/2-100,(800-length)/2+length+10);

        //标注尺寸
        graphics2D.drawString(String.valueOf(stepLength*10),(800-width)/2+stepLength/2-10,(800-length)/2+platformWidth+stepNumbers/2*stepWidth+60);
        graphics2D.drawString(String.valueOf(stepLength*10),(800-width)/2+stepLength+stairShaftWidth+stepLength/2-10,(800-length)/2+platformWidth+stepNumbers/2*stepWidth+60);
        graphics2D.drawString(String.valueOf(stairShaftWidth*10),(800-width)/2+stepLength,(800-length)/2+platformWidth+stepNumbers/2*stepWidth+60);
        graphics2D.drawString(String.valueOf(width*10),(800-width)/2+width/2-10,platformWidth+stepNumbers/2*stepWidth+(800-length)/2+90);
        Font font = new Font(null, Font.PLAIN, 15);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(270), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        graphics2D.setFont(rotatedFont);
        graphics2D.drawString(String.valueOf(platformWidth*10),(800-width)/2-70,platformWidth/2+(800-length)/2+20);
        graphics2D.drawString(String.valueOf(stepNumbers/2*stepWidth*10),(800-width)/2-70,platformWidth+(800-length)/2+stepNumbers/2*stepWidth/2+20);
        if(length-platformWidth-stepNumbers/2*stepWidth>0){
            graphics2D.drawString(String.valueOf((length-platformWidth-stepNumbers/2*stepWidth)*10),(800-width)/2-70,platformWidth+(800-length)/2+stepNumbers/2*stepWidth+(length-platformWidth-stepNumbers/2*stepWidth)/2+20);

        }
        graphics2D.drawString(String.valueOf(length*10),(800-width)/2-110,(800-length)/2+length/2-10);
    }
}
