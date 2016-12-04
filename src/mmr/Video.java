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
    double threshCamBreaks, threshSpecEffects;
    ArrayList<Integer> shotBoundaries;
    ArrayList<Integer> nextFrames;
    ArrayList<Integer> keyframes;
    boolean[] gradualTransitionFrames;
    
    public Video(double[][] hist, double alpha){
        this.hist = hist;
        this.alpha = alpha;
        sd = new double[hist.length-1];
        sdMean = 0;
        sdSD = 0;
        shotBoundaries = new ArrayList<>();
        nextFrames = new ArrayList<>();
        keyframes = new ArrayList<>();
        gradualTransitionFrames = new boolean[sd.length];
    }

// back up lang to; it's the code before i started meddling with it :))
    
//    public void twinComparison(){
//        //COMPUTE THE DISTANCE BETWEEN EVERY TWO FRAMES 
//        for(int i = 1; i < hist.length; i++) {
//            for(int j=0; j<LUV_MAX; j++){
//                sd[i-1]+=Math.abs(hist[i-1][j]-hist[i][j]);
//            }
//            sdMean += sd[i-1];
//        }
//        sdMean/=sd.length;
//        for(int i=0; i<sd.length; i++) sdSD+=Math.pow(sd[i]-sdMean, 2);
//        sdSD = Math.sqrt(sdSD/sd.length);
//        thresh = sdMean + alpha*sdSD;
//        for(int i=0; i<sd.length; i++)
//            if(sd[i]>thresh) shotBoundaries.add(i); // shot boundary between i and i+1
//        avgHistogram();
//    }
    
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
        
        // original threshold na cinode ni Gio (Marc?) dati
        threshCamBreaks = sdMean + alpha*sdSD;
        // "Usually Ts ~ 8- 10 and Tb = µ + aδ where a = 5 or 6" (PPT)
        threshSpecEffects = 9;
        
        double accumulatedDifferenceGradualTransition = 0;
        // Problem: there could be a minor drop in SDi which will not allow 
        // proper detection of gradual transitions. 
        // This can be fixed by giving a tolerance of 2-3 frames.
        int allowedDroppedFrames = 3; 
        int currDroppedFrames = 0;
        
        for(int i=0; i<sd.length; i++){
            // mark abrupt transition sequences as shot boundaries
            if(sd[i]>threshCamBreaks) {
                shotBoundaries.add(i); // shot boundary between i and i+1
                nextFrames.add(i+1);
            }
            
            // mark potential gradual transition sequences
            boolean theresCurrentlyAStartGradualTransitionFrame = false;
            boolean theresCurrentlyAnEndGradualTransitionFrame = false;
            int potentialStart = 0, potentialEnd = 0;
            
            if (sd[i]>threshSpecEffects) {
                if (theresCurrentlyAStartGradualTransitionFrame == false &&
                    theresCurrentlyAnEndGradualTransitionFrame == false){
                    theresCurrentlyAStartGradualTransitionFrame = true;
                    potentialStart = i;
                    accumulatedDifferenceGradualTransition += sd[i];
                } else if (theresCurrentlyAStartGradualTransitionFrame == true &&
                        theresCurrentlyAnEndGradualTransitionFrame == false){
                    theresCurrentlyAnEndGradualTransitionFrame = true;
                    potentialEnd = i;
                    accumulatedDifferenceGradualTransition += sd[i];
                    if (accumulatedDifferenceGradualTransition > threshSpecEffects){
                        gradualTransitionFrames[potentialStart] = true;
                        gradualTransitionFrames[potentialEnd] = true;
                    }
                } else if (theresCurrentlyAStartGradualTransitionFrame == false &&
                        theresCurrentlyAnEndGradualTransitionFrame == true){
                    theresCurrentlyAStartGradualTransitionFrame = false;
                    theresCurrentlyAnEndGradualTransitionFrame = false;
                    potentialStart = 0;
                    potentialEnd = 0;
                    accumulatedDifferenceGradualTransition = 0;
                }
            } else if (sd[i]<threshSpecEffects && 
                    theresCurrentlyAStartGradualTransitionFrame){
                currDroppedFrames++;
                if (currDroppedFrames > allowedDroppedFrames){
                    theresCurrentlyAStartGradualTransitionFrame = false;
                    theresCurrentlyAnEndGradualTransitionFrame = false;
                    potentialStart = 0;
                    potentialEnd = 0;
                    accumulatedDifferenceGradualTransition = 0;
                }
            }
            
            for (boolean b : gradualTransitionFrames){
                if (b){
                    while (b == false){
                        b = true;
                    }
                }
            }               
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
