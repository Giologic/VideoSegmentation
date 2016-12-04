package mmr;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class VideoData {
    static double[][] histUni;
    static double[][] histMJack;
    static double[][] hist777;
    
    static void init(){
        try {
            BufferedReader brUni = new BufferedReader(new FileReader("UniHistos.txt"));
            BufferedReader brMJack = new BufferedReader(new FileReader("MJackHistos.txt"));
            BufferedReader br777 = new BufferedReader(new FileReader("777Histos.txt"));
            int len = Integer.parseInt(brUni.readLine());
            int index = 0;
            histUni = new double[len][];
            while(true){
                String in = brUni.readLine();
                if("".equals(in) || in==null) break;
                String[] nhRaw = brUni.readLine().split(" ");
                double[] nhData = new double[Image.LUV_MAX];
                for(int i=0; i<Image.LUV_MAX; i++) nhData[i] = Double.parseDouble(nhRaw[i]);
                histUni[index++] = nhData;
            }
            len = Integer.parseInt(brMJack.readLine());
            index = 0;
            histMJack = new double[len][];
            while(true){
                String in = brMJack.readLine();
                if("".equals(in) || in==null) break;
                String[] nhRaw = brMJack.readLine().split(" ");
                double[] nhData = new double[Image.LUV_MAX];
                for(int i=0; i<Image.LUV_MAX; i++) nhData[i] = Double.parseDouble(nhRaw[i]);
                histMJack[index++] =  nhData;
            }
            len = Integer.parseInt(br777.readLine());
            index = 0;
            hist777 = new double[len][];
            while(true){
                String in = br777.readLine();
                if("".equals(in) || in==null) break;
                String[] nhRaw = br777.readLine().split(" ");
                double[] nhData = new double[Image.LUV_MAX];
                for(int i=0; i<Image.LUV_MAX; i++) nhData[i] = Double.parseDouble(nhRaw[i]);
                hist777[index++] = nhData;
            }
        } catch (FileNotFoundException ex) {} 
        catch (IOException ex) {}
        System.out.println("Initialization finished");
    }
    
    static double[][] getHist(int index){
        return index==1 ? histUni : (index==2 ? histMJack : hist777);
    }

}
