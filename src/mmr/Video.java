package mmr;

import java.util.ArrayList;

/**
 * Created by Gio on 11/22/2016.
 *
 * NOTE: NOT YET TESTED
 */
public class Video {
    public final static int LUV_MAX = 159;
    double[][] hist;
    double[] sd;
    double[] avgHist = new double[LUV_MAX];
    double sdMean = 0;
    double sdSD = 0;
    double alpha;
    double thresh;
    ArrayList<Integer> shotBoundaries;
    ArrayList<Integer> nextFrames;
    ArrayList<Integer> keyframes;
    
    public Video(double[][] hist, double alpha){
        this.hist = hist;
        this.alpha = alpha;
        sd = new double[hist.length-1];
        sdMean = 0;
        sdSD = 0;
        shotBoundaries = new ArrayList<>();
        nextFrames = new ArrayList<>();
        keyframes = new ArrayList<>();
    }

    public void twinComparison(){
        //COMPUTE THE DISTANCE BETWEEN EVERY TWO FRAMES 
        for(int i = 1; i < hist.length; i++) {
            for(int j=0; j<LUV_MAX; j++){
                sd[i-1]+=Math.abs(hist[i-1][j]-hist[i][j]);
            }
            sdMean += sd[i-1];
        }
        sdMean/=sd.length;
        for(int i=0; i<sd.length; i++) sdSD+=Math.pow(sd[i]-sdMean, 2);
        sdSD = Math.sqrt(sdSD/sd.length);
        thresh = sdMean + alpha*sdSD;
        for(int i=0; i<sd.length; i++)
            if(sd[i]>thresh){ // shot boundary between i and i+1
                shotBoundaries.add(i);
                nextFrames.add(i+1);
            } 
        avgHistogram();
    }

    private void avgHistogram(){
        for (double[] img: hist) 
            for (int j = 0; j < LUV_MAX; j++) avgHist[j] += img[j];
        //GET THE AVERAGE
        for(int i = 0 ; i < LUV_MAX; i++) avgHist[i] /= hist.length;
        
        int shotStart = 0;
        for(Integer shotEnd: shotBoundaries){
            int minDist = Integer.MAX_VALUE;
            int keyframe = shotStart;
            for(int i=shotStart; i<=shotEnd; i++){
                int dist = 0;
                for(int j=0; j<LUV_MAX; j++) dist += Math.abs(hist[i][j]-avgHist[j]);
                if(dist<minDist){
                    minDist = dist;
                    keyframe = i;
                }
            }
            keyframes.add(keyframe);
            shotStart = shotEnd+1;
        }
        
    }
}
