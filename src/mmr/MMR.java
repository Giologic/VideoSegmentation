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
        System.out.println("1- Uni");
        System.out.println("2- MJack");
        System.out.println("3- 777");
        System.out.print("Enter video: ");
        int index = sc.nextInt();
        Video v = new Video(VideoData.getHist(index));
        v.twinComparison();
        for(Integer shot: v.shotBoundaries){
            System.out.println(shot);
        }
    }
}
