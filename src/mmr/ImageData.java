package mmr;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ImageData {
    static HashMap<String, double[]> histUni;
    static HashMap<String, double[]> histMJack;
    static HashMap<String, double[]> hist777;
    
    static void init(){
        histUni = new HashMap<>();
        histMJack = new HashMap<>();
        hist777 = new HashMap<>();
        try {
            BufferedReader brUni = new BufferedReader(new FileReader("UniHistos.txt"));
            BufferedReader brMJack = new BufferedReader(new FileReader("MJackHistos.txt"));
            BufferedReader br777 = new BufferedReader(new FileReader("777Histos.txt"));
            while(true){
                String in = brUni.readLine();
                if("".equals(in) || in==null) break;
                String[] nhRaw = brUni.readLine().split(" ");
                double[] nhData = new double[Image.LUV_MAX];
                for(int i=0; i<Image.LUV_MAX; i++){
                    nhData[i] = Double.parseDouble(nhRaw[i]);
                }
                histUni.put(in, nhData);
            }
            while(true){
                String in = brMJack.readLine();
                if("".equals(in) || in==null) break;
                String[] nhRaw = brMJack.readLine().split(" ");
                double[] nhData = new double[Image.LUV_MAX];
                for(int i=0; i<Image.LUV_MAX; i++){
                    nhData[i] = Double.parseDouble(nhRaw[i]);
                }
                histMJack.put(in, nhData);
            }
            while(true){
                String in = br777.readLine();
                if("".equals(in) || in==null) break;
                String[] nhRaw = br777.readLine().split(" ");
                double[] nhData = new double[Image.LUV_MAX];
                for(int i=0; i<Image.LUV_MAX; i++){
                    nhData[i] = Double.parseDouble(nhRaw[i]);
                }
                hist777.put(in, nhData);
            }
        } catch (FileNotFoundException ex) {} 
        catch (IOException ex) {}
        System.out.println("Initialization finished");
    }
    
    static double[] getNH(String file, int video){
        switch (video) {
            case 1:
                return histUni.get(file);
            case 2:
                return histMJack.get(file);
            default:
                return hist777.get(file);
        }
    }
}
