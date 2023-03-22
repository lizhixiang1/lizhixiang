package cn.ShanDongUniversity.interactiveInterface;
import cn.ShanDongUniversity.design.StepDesign;
import java.text.DecimalFormat;
import java.util.Scanner;
public class InteractiveInterface {
    double width,length,highth,visitorsFolwRate,platformWidth,stepLength,stairShaftWidth,stepWidth,stepHighth,stepNumbers,
    thicknessOfUpperPlate,thicknessOfLowerPlate,thicknessOfConcrete,thicknessOfPlatformUpperPlate,thicknessOfPlatformLowerPlate,thicknessOfPlatformConcrete;
    String stairsCategory,check,kindOfPlatFormBeam,kindOfChannleBar2,kindOfChannleBar;
    StepDesign stepDesign;
    Scanner scanner=new Scanner(System.in);
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    public void display1(){
        String check;
        System.out.println("本项目仅可用来设计楼梯间为规则矩形的平行双跑楼梯");
        System.out.println("设计过程考虑楼梯间封闭，窗洞的位置需要后期根据需要自行加入或者调整");
        System.out.println("请按照提示输入以下参数");
        try{
            display1_1();
        }catch (Exception e){
            System.out.println("⚠️请输入阿拉伯数字");
            display1_1();
        }

        try{
            display1_2();
        }catch (Exception e){
            System.out.println("⚠️请输入阿拉伯数字");
            display1_2();
        }

        try{
            display1_3();
        }catch (Exception e){
            System.out.println("⚠️请输入阿拉伯数字");
            display1_3();
        }


        try{
            display1_4();
        }catch (Exception e){
            System.out.println("⚠️请输入A/a/B/b/C/c/D/d/E/e");
            display1_4();
        }

        try{
            display1_5();
        }catch (Exception e){
            System.out.println("⚠️请输入阿拉伯数字");
            display1_5();
        }

        System.out.println("是否有数据需要修改，有的话请输入数据行数1-5，没有的话随便输入");
        check = scanner.next();
        while(check.equals("1") || check.equals("2") || check.equals("3") || check.equals("4") || check.equals("5")){
            if(check.equals("1")){
                display1_1();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-5，没有的话随便输入");
                check = scanner.next();
            }

            if(check.equals("2")){
                display1_2();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-5，没有的话随便输入");
                check = scanner.next();
            }

            if(check.equals("3")){
                display1_3();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-5，没有的话随便输入");
                check = scanner.next();
            }

            if(check.equals("4")){
                display1_4();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-5，没有的话随便输入");
                check = scanner.next();
            }

            if(check.equals("5")){
                display1_5();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-5，没有的话随便输入");
                check = scanner.next();
            }
        }
    }

    private void display1_1(){
        System.out.println("（1）楼梯间净开间,单位：mm（毫米）（开间减去两边墙厚度的一半）");
        String width1 = scanner.next();
        width=Double.parseDouble(width1);
    }

    private void display1_2(){
        System.out.println("（2）楼梯间净进深,单位：mm（毫米）（进深减去两边墙厚度的一半）");
        String length1 = scanner.next();
        length=Double.parseDouble(length1);
    }

    private void display1_3(){
        System.out.println("（3）楼梯间层高,单位：mm（毫米)");
        String highth1 = scanner.next();
        highth=Double.parseDouble(highth1);
    }

    private void display1_4(){
        System.out.println("（4）楼梯类别：（只需要填写大写/小写字母即可)");
        System.out.println("A.公用住宅楼梯");
        System.out.println("B.幼儿园，小学等楼梯");
        System.out.println("C.体育馆，剧场，电影院，医院，疗养院等");
        System.out.println("D.其它建筑物楼梯");
        System.out.println("E.专用服务楼梯，住宅户内楼梯");
        stairsCategory = scanner.next();
    }

    private void display1_5(){
        System.out.println("（5）人流量（并排通行人数）");
        System.out.println("请填写1-3中任意数字，一般为2");
        String visitorsFolwRate1= scanner.next();
        visitorsFolwRate=Double.parseDouble(visitorsFolwRate1);
    }

    public void display2(StepDesign stepDesign){
        this.stepDesign=stepDesign;
        stepLength = stepDesign.getStepLength();
        stairShaftWidth = stepDesign.getStairShaftWidth();
        stepWidth = stepDesign.getStepWidth();
        stepHighth =stepDesign.getStepHighth();
        stepNumbers = stepDesign.getStepNumbers();
        System.out.println("对于楼梯计算结果是否有数据需要调整，有的话请输入数据行数1-6，没有的话随便输入");
        check = scanner.next();
        int index=0;
        while(check.equals("1") || check.equals("2") || check.equals("3") || check.equals("4") || check.equals("5")||check.equals("6")){
            index=1;
            if(check.equals("1")){
                display2_1();
                System.out.println("对于楼梯计算结果是否有数据需要调整，有的话请输入数据行数1-6，没有的话随便输入");
                check = scanner.next();
            }

            if(check.equals("2")){
                display2_2();
                System.out.println("对于楼梯计算结果是否有数据需要调整，有的话请输入数据行数1-6，没有的话随便输入");
                check = scanner.next();
            }

            if(check.equals("3")){
                display2_3();
                System.out.println("对于楼梯计算结果是否有数据需要调整，有的话请输入数据行数1-6，没有的话随便输入");
                check = scanner.next();
            }

            if(check.equals("4")){
                display2_4();
                stepLength=(width-stairShaftWidth)/2;
                System.out.println("对于楼梯计算结果是否有数据需要调整，有的话请输入数据行数1-6，没有的话随便输入");
                check = scanner.next();
            }

            if(check.equals("5")){
                display2_5();
                System.out.println("对于楼梯计算结果是否有数据需要调整，有的话请输入数据行数1-6，没有的话随便输入");
                check = scanner.next();
            }

            if(check.equals("6")){
                display2_6();
                System.out.println("对于楼梯计算结果是否有数据需要调整，有的话请输入数据行数1-6，没有的话随便输入");
                check = scanner.next();
            }
        }

        if(index==1){
            System.out.println("\n\n\n");
            System.out.println("（1）踏步宽度："+stepWidth);
            System.out.println("（2）踏步高度："+decimalFormat.format(stepHighth));
            System.out.println("（3）总踏步数："+stepNumbers);
            System.out.println("（4）梯井宽度："+stairShaftWidth);
            System.out.println("（5）梯段宽度："+stepLength);
            System.out.println("（6）中间平台宽度："+platformWidth);
            System.out.println("（7）梯段水平投影长度："+stepWidth*stepNumbers/2);
            System.out.println("（8）中间平台标高："+stepHighth*stepNumbers/2);
            double i=length-stepNumbers/2*stepWidth-platformWidth;
            if(stepHighth*stepNumbers/2<2200){
                System.out.println("⚠️平台净空高度不足，可以设置成储物间，但过往人员需要多加注意");
            }
            if(i<0){
                System.out.println("⚠️楼梯间净进深太短，无法设计出符合要求的规则平行双跑楼梯");
            }else{
                System.out.println("（9）盈余出来的进深："+i);
                System.out.println("可以根据这部分盈余出来的进深，适当加长中间平台宽度\n\n\n");
            }
        }
    }

    public void display2_1(){
        System.out.println("（1）踏步宽度：");
        stepWidth=scanner.nextDouble();
    }

    public void display2_2(){
        System.out.println("（2）踏步高度：");
        stepHighth=scanner.nextDouble();
    }

    public void display2_3(){
        System.out.println("（3）总踏步数：");
        stepNumbers=scanner.nextDouble();
    }

    public void display2_4(){
        System.out.println("（4）梯井宽度：");
        stairShaftWidth=scanner.nextDouble();
    }

    public void display2_5(){
        System.out.println("（5）梯段宽度：");
        stepLength=scanner.nextDouble();
    }

    public void display2_6(){
        System.out.println("（6）中间平台宽度：");
        platformWidth=scanner.nextDouble();
    }

    public void display3(StepDesign stepDesign){
        double stepLength = stepDesign.getStepLength();
        double stairShaftWidth = stepDesign.getStairShaftWidth();
        double stepWidth = stepDesign.getStepWidth();
        double stepHighth =stepDesign.getStepHighth();
        double stepNumbers = stepDesign.getStepNumbers();
        System.out.println("（1）踏步宽度："+stepWidth);
        System.out.println("（2）踏步高度："+decimalFormat.format(stepHighth));
        System.out.println("（3）总踏步数："+stepNumbers);
        System.out.println("（4）梯井宽度："+stairShaftWidth);
        System.out.println("（5）梯段宽度："+stepLength);
        platformWidth=stepLength;
        System.out.println("（6）中间平台宽度："+platformWidth);
        System.out.println("（7）梯段水平投影长度："+stepWidth*stepNumbers/2);
        System.out.println("（8）中间平台标高："+stepHighth*stepNumbers/2);
        double i=length-stepNumbers/2*stepWidth-stepLength;
        if(stepHighth*stepNumbers/2<2200){
            System.out.println("⚠️平台净空高度不足，可以设置成储物间，但过往人员需要多加注意");
        }
        if(i<0){
            System.out.println("⚠️楼梯间净进深太短，无法设计出符合要求的规则平行双跑楼梯");
        }else{
            System.out.println("（9）盈余出来的进深："+i);
            System.out.println("可以根据这部分盈余出来的进深，适当加长中间平台宽度\n\n\n");
        }
    }


    public void display4(){
        try{
            display4_1();
        }catch (Exception e){
            System.out.println("⚠️请输入阿拉伯数字");
            display4_1();
        }

        try{
            display4_2();
        }catch (Exception e){
            System.out.println("⚠️请输入阿拉伯数字");
            display4_2();
        }

        try{
            display4_3();
        }catch (Exception e){
            System.out.println("⚠️请输入阿拉伯数字");
            display4_3();
        }

        display4_4();

        try{
            display4_5();
        }catch (Exception e){
            System.out.println("⚠️请输入阿拉伯数字");
            display4_5();
        }

        try{
            display4_6();
        }catch (Exception e){
            System.out.println("⚠️请输入阿拉伯数字");
            display4_6();
        }

        try{
            display4_7();
        }catch (Exception e){
            System.out.println("⚠️请输入阿拉伯数字");
            display4_7();
        }

        display4_8();

        System.out.println("是否有数据需要修改，有的话请输入数据行数1-8，没有的话随便输入");
        String check = scanner.next();
        while(check.equals("1") || check.equals("2") || check.equals("3") || check.equals("4")
                || check.equals("5") || check.equals("6") || check.equals("7") || check.equals("8")) {
            if (check.equals("1")) {
                display4_1();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-8，没有的话随便输入");
                check = scanner.next();
            }

            if (check.equals("2")) {
                display4_2();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-8，没有的话随便输入");
                check = scanner.next();
            }

            if (check.equals("3")) {
                display4_3();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-8，没有的话随便输入");
                check = scanner.next();
            }

            if (check.equals("4")) {
                display4_4();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-8，没有的话随便输入");
                check = scanner.next();
            }

            if (check.equals("5")) {
                display4_5();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-8，没有的话随便输入");
                check = scanner.next();
            }

            if (check.equals("6")) {
                display4_6();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-8，没有的话随便输入");
                check = scanner.next();
            }

            if (check.equals("7")) {
                display4_7();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-8，没有的话随便输入");
                check = scanner.next();
            }

            if (check.equals("8")) {
                display4_8();
                System.out.println("是否有数据需要修改，有的话请输入数据行数1-8，没有的话随便输入");
                check = scanner.next();
            }
        }
    }

    public void display4_1(){
        System.out.println("（1）请输入梯段上表面花纹钢板厚度，注意单位为mm");
        String thicknessOfUpperPlate1 = scanner.next();
        thicknessOfUpperPlate=Double.parseDouble(thicknessOfUpperPlate1);
    }

    public void display4_2(){
        System.out.println("（2）请输入梯段下表面花纹钢板厚度，注意单位为mm");
        String thicknessOfLowerPlate1 = scanner.next();
        thicknessOfLowerPlate=Double.parseDouble(thicknessOfLowerPlate1);
    }

    public void display4_3(){
        System.out.println("（3）灌入的混凝土为C30细石混凝土，请选择灌入厚度，单位为mm（一般选50）");
        String thicknessOfConcrete1 =scanner.next();
        thicknessOfConcrete=Double.parseDouble(thicknessOfConcrete1);
    }

    public void display4_4(){
        System.out.println("（4）梯梁为C型槽钢，输入时请输入类似于C25a,如果是钢板，请输入类似于6-200（第一个数字是钢板厚度，第二个是钢板高度，单位为mm)");
        kindOfChannleBar= scanner.next();
    }

    public void display4_5(){
        System.out.println("（5）请输入平台上表面花纹钢板厚度，注意单位为mm");
        String thicknessOfPlatformUpperPlate1 = scanner.next();
        thicknessOfPlatformUpperPlate=Double.parseDouble(thicknessOfPlatformUpperPlate1);
    }

    public void display4_6(){
        System.out.println("（6）请输入平台下表面花纹钢板厚度，注意单位为mm");
        String thicknessOfPlatformLowerPlate1 = scanner.next();
        thicknessOfPlatformLowerPlate=Double.parseDouble(thicknessOfPlatformLowerPlate1);
    }

    public void display4_7(){
        System.out.println("（7）灌入的混凝土为C30细石混凝土，请选择灌入厚度，单位为mm（一般选50）");
        String thicknessOfPlatformConcrete1 =scanner.next();
        thicknessOfPlatformConcrete=Double.parseDouble(thicknessOfPlatformConcrete1);
    }

    public void display4_8(){
        System.out.println("（8）梯梁为H型钢，输入时请输入类似于HN150*75*5*7");
        kindOfPlatFormBeam= scanner.next();
    }

    public double getWidth() {
        return width;
    }
    public double getLength() {
        return length;
    }
    public double getHighth() {
        return highth;
    }
    public String getStairsCategory() {
        return stairsCategory;
    }
    public double getVisitorsFolwRate() {
        return visitorsFolwRate;
    }
    public double getThicknessOfUpperPlate() {
        return thicknessOfUpperPlate;
    }
    public double getThicknessOfLowerPlate() {
        return thicknessOfLowerPlate;
    }
    public double getThicknessOfConcrete() {
        return thicknessOfConcrete;
    }
    public String getKindOfChannleBar() {
        return kindOfChannleBar;
    }
    public double getThicknessOfPlatformUpperPlate() {return thicknessOfPlatformUpperPlate;}
    public double getThicknessOfPlatformLowerPlate() {
        return thicknessOfPlatformLowerPlate;
    }
    public double getThicknessOfPlatformConcrete() {
        return thicknessOfPlatformConcrete;
    }
    public String getKindOfPlatFormBeam() {
        return kindOfPlatFormBeam;
    }
    public double getPlatformWidth() {
        return platformWidth;
    }
    public double getStairShaftWidth() {
        return stairShaftWidth;
    }
    public String getKindOfChannleBar2() {
        return kindOfChannleBar2;
    }
    public double getStepLength() {
        return stepLength;
    }
    public double getStepWidth() {
        return stepWidth;
    }
    public double getStepHighth() {
        return stepHighth;
    }
    public double getStepNumbers() {
        return stepNumbers;
    }
}
