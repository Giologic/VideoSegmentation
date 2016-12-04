package mmr;

import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MMR {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        VideoData.init();
        System.out.print("Enter alpha: ");
        double alpha = sc.nextDouble();
        System.out.println("1- Uni");
        System.out.println("2- MJack");
        System.out.println("3- 777");
        System.out.print("Enter video: ");
        int index = sc.nextInt();
        Video v = new Video(VideoData.getHist(index), alpha);
        v.twinComparison();
        
        JFrame frame = new JFrame();
        frame.setTitle("Abrupt Transitions");
        frame.getContentPane().setLayout(new GridLayout(v.shotBoundaries.size(), 1));
        for(int i=0; i<v.shotBoundaries.size(); i++){
            int shot = v.shotBoundaries.get(i);
            int shot2 = v.nextFrames.get(i);
            System.out.println(shot);
            JPanel p = new JPanel();
            String q = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+"0"+shot+".jpg";
            String q2 = "videos/"+(index==1 ? "uni/" : index==2 ? "mjack/" : "777/")+"0"+shot2+".jpg";
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
            frame.add(p);
        }
        frame.setSize(400, 350);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
