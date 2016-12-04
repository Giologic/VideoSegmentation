package mmr;

import java.awt.GridLayout;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MMR {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        VideoData.init();
        System.out.print("Enter alpha: ");
        double alpha = sc.nextDouble();
        System.out.print("Enter Ts coefficient: ");
        double tsCoeff = sc.nextDouble();
        System.out.println("1- Uni");
        System.out.println("2- MJack");
        System.out.println("3- 777");
        System.out.print("Enter video: ");
        int index = sc.nextInt();
        Video v = new Video(VideoData.getHist(index), alpha, tsCoeff);
        v.twinComparison();
        System.out.println(v.tb);
        
        JFrame abrupt = new JFrame();
        abrupt.setTitle("Abrupt Transitions");
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout(v.shotBoundaries.size(), 1));
        for(int i=0; i<v.shotBoundaries.size(); i++){
            int shot = v.shotBoundaries.get(i);
            int shot2 = v.nextFrames.get(i);
            JPanel p = new JPanel();
            String buff = shot<10 ? "000" : shot<100 ? "00" : shot<1000 ? "0" : "";
            String q = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+shot+".jpg";
            buff = shot2<10 ? "000" : shot2<100 ? "00" : shot2<1000 ? "0" : "";
            String q2 = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+shot2+".jpg";
            JLabel l1 = new JLabel(new ImageIcon(q));
            l1.setText(q);
            l1.setHorizontalTextPosition(JLabel.CENTER);
            l1.setVerticalTextPosition(JLabel.BOTTOM);
            p.add(l1);
            JLabel l2 = new JLabel(new ImageIcon(q2));
            l2.setText(q2);
            l2.setHorizontalTextPosition(JLabel.CENTER);
            l2.setVerticalTextPosition(JLabel.BOTTOM);
            p.add(l2);
            outerPanel.add(p);
        }
        JScrollPane sp = new JScrollPane(outerPanel);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        abrupt.add(sp);
        abrupt.setSize(400, 350);
        abrupt.setResizable(false);
        abrupt.setVisible(true);
        abrupt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JFrame abruptKF = new JFrame();
        abruptKF.setTitle("Abrupt Transitions Keyframes");
        outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout(v.abruptKeyframes.size(), 1));
        for(int i=0; i<v.abruptKeyframes.size(); i++){
            int keyframe = v.abruptKeyframes.get(i);
            JPanel p = new JPanel();
            String buff = keyframe<10 ? "000" : keyframe<100 ? "00" : keyframe<1000 ? "0" : "";
            String q = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+keyframe+".jpg";
            JLabel l1 = new JLabel(new ImageIcon(q));
            l1.setText(q);
            l1.setHorizontalTextPosition(JLabel.CENTER);
            l1.setVerticalTextPosition(JLabel.BOTTOM);
            p.add(l1);
            outerPanel.add(p);
        }
        sp = new JScrollPane(outerPanel);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        abruptKF.add(sp);
        abruptKF.setSize(400, 350);
        abruptKF.setResizable(false);
        abruptKF.setVisible(true);
        abruptKF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JFrame gradual = new JFrame();
        gradual.setTitle("Gradual Transitions");
        outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout(v.gtStart.size(), 1));
        for(int i=0; i<v.gtStart.size(); i++){
            int shot = v.gtStart.get(i);
            int shot2 = v.gtEnd.get(i);
            JPanel p = new JPanel();
            String buff = shot<10 ? "000" : shot<100 ? "00" : shot<1000 ? "0" : "";
            String q = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+shot+".jpg";
            buff = shot2<10 ? "000" : shot2<100 ? "00" : shot2<1000 ? "0" : "";
            String q2 = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+shot2+".jpg";
            JLabel l1 = new JLabel(new ImageIcon(q));
            l1.setText(q);
            l1.setHorizontalTextPosition(JLabel.CENTER);
            l1.setVerticalTextPosition(JLabel.BOTTOM);
            p.add(l1);
            JLabel l2 = new JLabel(new ImageIcon(q2));
            l2.setText(q2);
            l2.setHorizontalTextPosition(JLabel.CENTER);
            l2.setVerticalTextPosition(JLabel.BOTTOM);
            p.add(l2);
            outerPanel.add(p);
        }
        sp = new JScrollPane(outerPanel);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gradual.add(sp);
        gradual.setSize(400, 350);
        gradual.setResizable(false);
        gradual.setVisible(true);
        gradual.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JFrame gradualKF = new JFrame();
        gradualKF.setTitle("Gradual Transitions Keyframes");
        outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout(v.gradualKeyframes.size(), 1));
        for(int i=0; i<v.gradualKeyframes.size(); i++){
            int keyframe = v.gradualKeyframes.get(i);
            JPanel p = new JPanel();
            String buff = keyframe<10 ? "000" : keyframe<100 ? "00" : keyframe<1000 ? "0" : "";
            String q = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+buff+keyframe+".jpg";
            JLabel l1 = new JLabel(new ImageIcon(q));
            l1.setText(q);
            l1.setHorizontalTextPosition(JLabel.CENTER);
            l1.setVerticalTextPosition(JLabel.BOTTOM);
            p.add(l1);
            outerPanel.add(p);
        }
        sp = new JScrollPane(outerPanel);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gradualKF.add(sp);
        gradualKF.setSize(400, 350);
        gradualKF.setResizable(false);
        gradualKF.setVisible(true);
        gradualKF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
