package mmr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Image {
    public static final int LUV_MAX = 159;
    
    String filename;
    BufferedImage bi;
    int nRows;
    int nCols;
    int area;
    int[][] luv;
    double[] hist;
    
    public Image(String filename){
        this.filename = filename;
        try {
            File file = new File(filename);
            bi = ImageIO.read(file);
        } catch (IOException ex) {}
        nRows = bi.getHeight();
        nCols = bi.getWidth();
        area = nRows*nCols;
        
        luv = new int[nRows][nCols];
        hist = new double[LUV_MAX];

        for(int i=0; i<nRows; i++)
            for(int j=0; j<nCols; j++){
                Color CM = new Color(bi.getRGB(j, i));
                double R = CM.getRed();
                double G = CM.getGreen();
                double B = CM.getBlue();	
                CieConvert colorCIE = new CieConvert();
                colorCIE.setValues(R/255.0, G/255.0, B/255.0);
                luv[i][j] = colorCIE.IndexOf();
                hist[colorCIE.IndexOf()]++;
            }
    }
    
    public Image(double[] nh){
        this.hist = nh;
    }
    
    public Image(int[][] luv){
        this.luv = luv;
        this.nRows = luv.length;
        this.nCols = luv[0].length;
        this.area = nRows*nCols;
    }
    
    String getStringArr(double[] arr) {
        StringBuilder sb = new StringBuilder(arr[0]+"");
        for(int i=1; i<arr.length; i++) sb.append(" ").append(arr[i]);
        return sb.toString();
    }
    
    String getStringLuv(){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<nRows; i++){    
            sb.append(luv[i][0]);
            for(int j=0; j<nCols; j++) sb.append(" ").append(luv[i][j]);
            if(!(i==nRows-1)) sb.append("\n");
        }
        return sb.toString();
    }
}
