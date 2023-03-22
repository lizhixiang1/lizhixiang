package cn.ShanDongUniversity.paint;
import cn.ShanDongUniversity.interactiveInterface.InteractiveInterface;
import java.awt.*;

public class Elevation extends Canvas {
    Graphics2D graphics2D=null;
    InteractiveInterface interactiveInterface;
    int width, length=0, highth=0, platformWidth=0, stepLength=0, stairShaftWidth=0, stepWidth=0,
    stepHighth=0, stepNumbers=0, thicknessOfPlatform=0, thicknessOfStep=0;

    public Elevation(InteractiveInterface interactiveInterface){
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

    public void paint(Graphics g){
        super.paint(g);
        graphics2D=(Graphics2D)g;
        BasicStroke  stokeLine=new BasicStroke(2.0f);
        //建筑轮廓线
        graphics2D.drawLine(0,highth+100-highth/2-(stepNumbers/2+5)*stepHighth,length/2+100,highth+100-highth/2-(stepNumbers/2+5)*stepHighth);
        graphics2D.drawLine(length/2+120,highth+100-highth/2-(stepNumbers/2+5)*stepHighth,length+24,highth+100-highth/2-(stepNumbers/2+5)*stepHighth);
        graphics2D.drawLine(length/2+100,highth+100-highth/2-(stepNumbers/2+5)*stepHighth,length/2+110,highth+100-highth/2-(stepNumbers/2+5)*stepHighth-20);
        graphics2D.drawLine(length/2+120,highth+100-highth/2-(stepNumbers/2+5)*stepHighth,length/2+110,highth+100-highth/2-(stepNumbers/2+5)*stepHighth+20);
        graphics2D.drawLine(length/2+110,highth+100-highth/2-(stepNumbers/2+5)*stepHighth-20,length/2+110,highth+100-highth/2-(stepNumbers/2+5)*stepHighth+20);
        graphics2D.drawLine(length,highth+100-highth/2-(stepNumbers/2+5)*stepHighth,length,highth+100);
        graphics2D.drawLine(0,highth+100,length+24,highth+100);
        //平台
        graphics2D.drawLine(length,highth+100-highth/2,length-platformWidth,highth+100-highth/2);
        graphics2D.drawLine(length,highth+100-highth/2+thicknessOfPlatform,length-platformWidth,highth+100-highth/2+thicknessOfPlatform);
        //下梯段
        for (int i = 0; i < stepNumbers/2; i++) {
            graphics2D.drawLine(length-platformWidth-i*stepWidth,highth+100-highth/2+i*stepHighth,length-platformWidth-stepWidth-i*stepWidth,highth+100-highth/2+i*stepHighth);
            graphics2D.drawLine(length-platformWidth-i*stepWidth+thicknessOfStep,highth+100-highth/2+i*stepHighth+thicknessOfStep,length-platformWidth-stepWidth-i*stepWidth+thicknessOfStep,highth+100-highth/2+i*stepHighth+thicknessOfStep);
            graphics2D.drawLine(length-platformWidth-stepWidth-i*stepWidth,highth+100-highth/2+i*stepHighth,length-platformWidth-stepWidth-i*stepWidth,highth+100-highth/2+i*stepHighth+stepHighth);
            graphics2D.drawLine(length-platformWidth-stepWidth-i*stepWidth+thicknessOfStep,highth+100-highth/2+i*stepHighth+thicknessOfStep+stepHighth,length-platformWidth-stepWidth-i*stepWidth+thicknessOfStep,highth+100-highth/2+i*stepHighth+thicknessOfStep);
        }
        //上梯段
        for (int i = 0; i < stepNumbers/2; i++) {
            graphics2D.drawLine(length-platformWidth-i*stepWidth,highth+100-highth/2-i*stepHighth,length-platformWidth-i*stepWidth,highth+100-highth/2-i*stepHighth-stepHighth);
            graphics2D.drawLine(length-platformWidth-i*stepWidth,highth+100-highth/2-i*stepHighth-stepHighth,length-platformWidth-i*stepWidth-stepWidth,highth+100-highth/2-i*stepHighth-stepHighth);
            graphics2D.drawLine(length-platformWidth-i*stepWidth-thicknessOfStep,highth+100-highth/2-i*stepHighth+thicknessOfStep,length-platformWidth-i*stepWidth-thicknessOfStep,highth+100-highth/2-i*stepHighth-stepHighth+thicknessOfStep);
            graphics2D.drawLine(length-platformWidth-i*stepWidth-thicknessOfStep,highth+100-highth/2-i*stepHighth-stepHighth+thicknessOfStep,length-platformWidth-i*stepWidth-stepWidth-thicknessOfStep,highth+100-highth/2-i*stepHighth-stepHighth+thicknessOfStep);
        }
        //擦去一些多余线段
        graphics2D.setColor(Color.lightGray);
        graphics2D.drawLine(length-platformWidth-thicknessOfStep,highth+100-highth/2,length-platformWidth-thicknessOfStep,highth+100-highth/2+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-(stepNumbers/2-1)*stepWidth-stepWidth,highth+100-highth/2-(stepNumbers/2-1)*stepHighth-stepHighth+thicknessOfStep,length-platformWidth-(stepNumbers/2-1)*stepWidth-stepWidth-thicknessOfStep,highth+100-highth/2-(stepNumbers/2-1)*stepHighth-stepHighth+thicknessOfStep);
        //补上一些小空缺
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawLine(length-platformWidth-(stepNumbers/2-1)*stepWidth-stepWidth,highth+100-highth/2-(stepNumbers/2-1)*stepHighth-stepHighth+thicknessOfStep,length-platformWidth-(stepNumbers/2-1)*stepWidth-stepWidth,highth+100-highth/2-(stepNumbers/2-1)*stepHighth-stepHighth);
        graphics2D.drawLine(length-platformWidth-thicknessOfStep,highth+100-highth/2 ,length-platformWidth,highth+100-highth/2);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth,highth+100-highth/2+(stepNumbers/2-1)*stepHighth,length-platformWidth-stepNumbers/2*stepWidth,highth+100);
        graphics2D.drawLine(length-platformWidth,highth+100-highth/2,length-platformWidth,highth+100-highth/2+thicknessOfStep);
        //空闲空间
        graphics2D.drawLine(0,highth+100-highth/2-(stepNumbers/2)*stepHighth,length-platformWidth-(stepNumbers/2-1)*stepWidth,highth+100-highth/2-(stepNumbers/2)*stepHighth);
        graphics2D.drawLine(0,highth+100-highth/2-(stepNumbers/2)*stepHighth+thicknessOfPlatform,length-platformWidth-(stepNumbers/2-1)*stepWidth,highth+100-highth/2-(stepNumbers/2)*stepHighth+thicknessOfPlatform);
        graphics2D.drawLine(length-platformWidth-(stepNumbers/2-1)*stepWidth,highth+100-highth/2-(stepNumbers/2)*stepHighth,length-platformWidth-(stepNumbers/2-1)*stepWidth,highth+100-highth/2-(stepNumbers/2)*stepHighth+thicknessOfPlatform);
        //画梯梁
        graphics2D.drawLine(length-platformWidth+thicknessOfPlatform,highth+100-highth/2+thicknessOfPlatform,length-platformWidth-stepNumbers/2*stepWidth+thicknessOfStep,highth+100);
        graphics2D.drawLine(length-platformWidth-stepWidth,highth+100-highth/2,length-platformWidth-stepNumbers/2*stepWidth-stepWidth-2,highth+100);
        graphics2D.drawLine(length-platformWidth,highth+100-highth/2-stepHighth,length-platformWidth-(stepNumbers/2-1)*stepWidth,highth+100-highth/2-(stepNumbers/2)*stepHighth);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth-thicknessOfStep,highth+100-highth/2-(stepNumbers/2)*stepHighth+thicknessOfStep,length-platformWidth-thicknessOfStep,highth+100-highth/2+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth,100+highth-stepNumbers*stepHighth-stepHighth-thicknessOfStep,length-platformWidth-stepNumbers/2*stepWidth+4*stepWidth,100+highth-stepNumbers*stepHighth-5*stepHighth-thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth+thicknessOfStep,100+highth-stepNumbers*stepHighth,length-platformWidth-stepNumbers/2*stepWidth+5*stepWidth,100+highth-stepNumbers*stepHighth-5*stepHighth);
        //平台梁1
        graphics2D.drawLine(length-platformWidth-7,highth/2+100+30+thicknessOfStep,length-platformWidth+7,highth/2+100+30+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-7,highth/2+100+30+thicknessOfStep,length-platformWidth-7,highth/2+100+27+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth+7,highth/2+100+30+thicknessOfStep,length-platformWidth+7,highth/2+100+27+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-7,highth/2+100+27+thicknessOfStep,length-platformWidth-3,highth/2+100+27+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth+3,highth/2+100+27+thicknessOfStep,length-platformWidth+7,highth/2+100+27+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-3,highth/2+100+27+thicknessOfStep,length-platformWidth-3,highth/2+100+3+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth+3,highth/2+100+27+thicknessOfStep,length-platformWidth+3,highth/2+100+3+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-3,highth/2+100+3+thicknessOfStep,length-platformWidth-7,highth/2+100+3+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth+3,highth/2+100+3+thicknessOfStep,length-platformWidth+7,highth/2+100+3+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-7,highth/2+100+3+thicknessOfStep,length-platformWidth-7,highth/2+100+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth+7,highth/2+100+3+thicknessOfStep,length-platformWidth+7,highth/2+100+thicknessOfStep);
        //平台梁2
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth-7,highth/2+100-stepNumbers/2*stepHighth+thicknessOfStep+30,length-platformWidth-stepNumbers/2*stepWidth+7,highth/2+100-stepNumbers/2*stepHighth+thicknessOfStep+30);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth-7,highth/2+100-stepNumbers/2*stepHighth+30+thicknessOfStep,length-platformWidth-stepNumbers/2*stepWidth-7,highth/2+100-stepNumbers/2*stepHighth+27+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth+7,highth/2+100-stepNumbers/2*stepHighth+30+thicknessOfStep,length-platformWidth-stepNumbers/2*stepWidth+7,highth/2+100-stepNumbers/2*stepHighth+27+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth-7,highth/2+100-stepNumbers/2*stepHighth+27+thicknessOfStep,length-platformWidth-stepNumbers/2*stepWidth-3,highth/2+100-stepNumbers/2*stepHighth+27+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth+3,highth/2+100-stepNumbers/2*stepHighth+27+thicknessOfStep,length-platformWidth-stepNumbers/2*stepWidth+7,highth/2+100-stepNumbers/2*stepHighth+27+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth-3,highth/2+100-stepNumbers/2*stepHighth+27+thicknessOfStep,length-platformWidth-stepNumbers/2*stepWidth-3,highth/2+100-stepNumbers/2*stepHighth+3+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth+3,highth/2+100-stepNumbers/2*stepHighth+27+thicknessOfStep,length-platformWidth-stepNumbers/2*stepWidth+3,highth/2+100-stepNumbers/2*stepHighth+3+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth-3,highth/2+100-stepNumbers/2*stepHighth+3+thicknessOfStep,length-platformWidth-stepNumbers/2*stepWidth-7,highth/2+100-stepNumbers/2*stepHighth+3+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth+3,highth/2+100-stepNumbers/2*stepHighth+3+thicknessOfStep,length-platformWidth-stepNumbers/2*stepWidth+7,highth/2+100-stepNumbers/2*stepHighth+3+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth-7,highth/2+100-stepNumbers/2*stepHighth+3+thicknessOfStep,length-platformWidth-stepNumbers/2*stepWidth-7,highth/2+100-stepNumbers/2*stepHighth+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth+7,highth/2+100-stepNumbers/2*stepHighth+3+thicknessOfStep,length-platformWidth-stepNumbers/2*stepWidth+7,highth/2+100-stepNumbers/2*stepHighth+thicknessOfStep);

        //细节补全
        for (int i = 0; i < 5; i++) {
            graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth+i*stepWidth,highth+100-highth/2-(stepNumbers/2)*stepHighth-i*stepHighth,length-platformWidth-stepNumbers/2*stepWidth+i*stepWidth,highth+100-highth/2-(stepNumbers/2)*stepHighth-(i+1)*stepHighth);
            graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth+i*stepWidth,highth+100-highth/2-(stepNumbers/2)*stepHighth-(i+1)*stepHighth,length-platformWidth-stepNumbers/2*stepWidth+(i+1)*stepWidth,highth+100-highth/2-(stepNumbers/2)*stepHighth-(i+1)*stepHighth);
            graphics2D.drawLine(length-platformWidth-(stepNumbers/2-4)*stepWidth+thicknessOfStep-(i-1)*stepWidth,highth+100-highth/2-(stepNumbers/2+5)*stepHighth+i*stepHighth+thicknessOfStep,length-platformWidth-(stepNumbers/2-4)*stepWidth+thicknessOfStep-i*stepWidth,highth+100-highth/2-(stepNumbers/2+5)*stepHighth+i*stepHighth+thicknessOfStep);
            graphics2D.drawLine(length-platformWidth-(stepNumbers/2-4)*stepWidth+thicknessOfStep-i*stepWidth,highth+100-highth/2-(stepNumbers/2+5)*stepHighth+i*stepHighth+thicknessOfStep,length-platformWidth-(stepNumbers/2-4)*stepWidth+thicknessOfStep-i*stepWidth,highth+100-highth/2-(stepNumbers/2+5)*stepHighth+(i+1)*stepHighth+thicknessOfStep);
        }
        graphics2D.drawLine(length-platformWidth-(stepNumbers/2-5)*stepWidth,highth+100-highth/2-(stepNumbers/2+5)*stepHighth,length-platformWidth-(stepNumbers/2-5)*stepWidth,highth+100-highth/2-(stepNumbers/2+5)*stepHighth+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-(stepNumbers/2-1)*stepWidth,highth+100-highth/2-stepNumbers/2*stepHighth,length-platformWidth-(stepNumbers/2-1)*stepWidth+thicknessOfStep,highth+100-highth/2-stepNumbers/2*stepHighth);
        graphics2D.setColor(Color.lightGray);
        graphics2D.drawLine(length-platformWidth-(stepNumbers/2-1)*stepWidth+thicknessOfStep,highth+100-highth/2-stepNumbers/2*stepHighth,length-platformWidth-(stepNumbers/2-1)*stepWidth+thicknessOfStep,highth+100-highth/2-stepNumbers/2*stepHighth+thicknessOfStep);
        graphics2D.drawLine(length-platformWidth-(stepNumbers/2-5)*stepWidth,highth+100-highth/2-(stepNumbers/2+5)*stepHighth+thicknessOfStep,length-platformWidth-(stepNumbers/2-5)*stepWidth+thicknessOfStep,highth+100-highth/2-(stepNumbers/2+5)*stepHighth+thicknessOfStep);
        graphics2D.setColor(Color.black);
        graphics2D.drawLine(length+24,highth+100-highth/2-(stepNumbers/2+5)*stepHighth,length+24,highth+100);
        //标注
        graphics2D.setStroke(stokeLine);
        graphics2D.drawLine(0,highth+140,length,highth+140);
        graphics2D.drawLine(length,highth+120,length,highth+160);
        graphics2D.drawLine(length-platformWidth,highth+120,length-platformWidth,highth+160);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth,highth+120,length-platformWidth-stepNumbers/2*stepWidth,highth+160);
        graphics2D.drawLine(0,highth+120,0,highth+160);
        graphics2D.drawLine(length-10,highth+150,length+10,highth+130);
        graphics2D.drawLine(length-platformWidth-10,highth+150,length-platformWidth+10,highth+130);
        graphics2D.drawLine(length-platformWidth-stepNumbers/2*stepWidth-10,highth+150,length-platformWidth-stepNumbers/2*stepWidth+10,highth+130);
        graphics2D.drawLine(0,highth+140,10,highth+130);
        graphics2D.drawString(String.valueOf(platformWidth*10),length-platformWidth/2-10,highth+160);
        graphics2D.drawString(String.valueOf(stepNumbers/2*stepWidth*10),length-platformWidth-stepNumbers/2*stepWidth/2-10,highth+160);
        if(length*10-platformWidth*10-stepNumbers/2*stepWidth*10>0){
            graphics2D.drawString(String.valueOf(length*10-platformWidth*10-stepNumbers/2*stepWidth*10),length-platformWidth-stepNumbers/2*stepWidth-(length-platformWidth-stepNumbers/2*stepWidth)/2-10,highth+160);

        }
        graphics2D.drawLine(0,highth+170,length,highth+170);
        graphics2D.drawLine(0,highth+150,0, highth+190);
        graphics2D.drawLine(length,highth+150,length,highth+190);
        graphics2D.drawLine(length-10,highth+180,length+10,highth+160);
        graphics2D.drawLine(0,highth+170,10,highth+160);
        graphics2D.drawString(String.valueOf(length*10),length/2-10,highth+190);
    }
}
