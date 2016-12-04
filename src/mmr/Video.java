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
    double sdMean = 0;
    double sdSD = 0;
    double alpha;
    double tb, ts, tsCoeff, tbGradual, tbCoeff=2.5;
    ArrayList<Integer> shotBoundaries;
    ArrayList<Integer> nextFrames;
    ArrayList<Integer> abruptKeyframes;
    boolean[] gtFrames;
    int totalGT = 0;
    ArrayList<Integer> gtStart;
    ArrayList<Integer> gtEnd;
    ArrayList<Integer> gradualKeyframes;
    
    public Video(double[][] hist, double alpha, double ts){
        this.hist = hist;
        this.alpha = alpha;
        this.tsCoeff = ts;
        sd = new double[hist.length-1];
        sdMean = 0;
        sdSD = 0;
        shotBoundaries = new ArrayList<>();
        nextFrames = new ArrayList<>();
        abruptKeyframes = new ArrayList<>();
        gtStart = new ArrayList<>();
        gtEnd = new ArrayList<>();
        gradualKeyframes = new ArrayList<>();
        gtFrames = new boolean[sd.length];
    }
    
    public void twinComparison(){
        for(int i = 1; i < hist.length; i++) {
            for(int j=0; j<LUV_MAX; j++){
                sd[i-1]+=Math.abs(hist[i-1][j]-hist[i][j]);
            }
            sdMean += sd[i-1];
        }
        sdMean/=sd.length;
        for(int i=0; i<sd.length; i++) sdSD+=Math.pow(sd[i]-sdMean, 2);
        sdSD = Math.sqrt(sdSD/sd.length);
        
        tb = sdMean + alpha*sdSD;
        ts = tb * tsCoeff;
        tbGradual = tb*tbCoeff;
        
        double totalDiffGT = 0;
        int allowedDroppedFrames = 3; 
        int currDroppedFrames = 0;
        System.out.println(ts);
        boolean hasStartGT = false;
        boolean hasEndGT = false;
        int pStart = 0, pEnd = 0;
        System.out.println(ts+" "+tb);
        for(int i=0; i<sd.length; i++){
            if(sd[i]>tb) {
                shotBoundaries.add(i); // shot boundary between i and i+1
                nextFrames.add(i+1);
            }
            if (sd[i]>ts && sd[i]<tb) {
                if (!hasStartGT && !hasEndGT){
                    hasStartGT = true;
                    pStart = i;
                    totalDiffGT += sd[i];
                    System.out.println(sd[i]+" hii "+i);
                } else if (hasStartGT && !hasEndGT){
                    System.out.println(sd[i]+" i "+i);
                    currDroppedFrames = 0;
                    totalDiffGT += sd[i];
                    if (totalDiffGT > tbGradual){
                        System.out.println("hiiiiiiiiiiiiiiiiiiiii "+totalDiffGT);
                        pEnd = i;
                        gtFrames[pStart] = true;
                        gtFrames[pEnd] = true;
                        gtStart.add(pStart);
                        gtEnd.add(pEnd);
                        hasStartGT = hasEndGT = false;
                        pStart = 0;
                        pEnd = 0;
                        totalDiffGT = 0;
                    }
                }
            } else if (sd[i]<ts && hasStartGT){
                currDroppedFrames++;
                if (currDroppedFrames > allowedDroppedFrames){
                    hasStartGT = false;
                    hasEndGT = false;
                    pStart = 0;
                    pEnd = 0;
                    totalDiffGT = 0;
                    currDroppedFrames = 0;
                } else {
                    totalDiffGT+=sd[i];
                    System.out.println(+sd[i]+" h "+i);
                }
            }
//            else 
//                System.out.println(sd[i]+" hiii "+i);              
        }
        
//        boolean isStart = true;
//        System.out.println(countTrueGTFrames());
//        for(int i=0; i<gtFrames.length; i++){
//            boolean b = gtFrames[i];
//            if(b){
//                if(isStart) gtStart.add(i);
//                else gtEnd.add(i);
//                isStart = !isStart;
//            }
//        }
//        
        for (int i = 0; i < gtFrames.length; i++){
            if (gtFrames[i]){
                int j = i;
                if ((j+1) < gtFrames.length - 1) j++; 
                while (!gtFrames[j]){
                    gtFrames[j] = true;
                    if ((j+1) < gtFrames.length - 1) j++;                    
                }
                i = j;
            }
        }
        
        avgHistogram();
    }

    private void avgHistogram(){
        shotBoundaries.add(sd.length);
        int shotStart = 0;
        for(Integer shotEnd: shotBoundaries){
            int minDist = Integer.MAX_VALUE;
            int keyframe = shotStart;
            double[] avgHist = new double[LUV_MAX];
            for(int i=shotStart; i<=shotEnd; i++){
                double[] img = hist[i];
                for (int j = 0; j < LUV_MAX; j++) avgHist[j] += img[j];
            }
            for(int i = 0 ; i < LUV_MAX; i++) avgHist[i] /= (shotEnd-shotStart);
            for(int i=shotStart; i<=shotEnd; i++){
                int dist = 0;
                for(int j=0; j<LUV_MAX; j++) dist += Math.abs(hist[i][j]-avgHist[j]);
                if(dist<minDist){
                    minDist = dist;
                    keyframe = i;
                }
            }
            abruptKeyframes.add(keyframe);
            shotStart = shotEnd+1;
        }
        shotBoundaries.remove(shotBoundaries.size()-1);
        
        shotStart = 0;
        gtStart.add(sd.length);
        for(int index=0; index<gtStart.size(); index++){
            int shotEnd = gtStart.get(index)-1;
            int minDist = Integer.MAX_VALUE;
            int keyframe = shotStart;
            double[] avgHist = new double[LUV_MAX];
            for(int i=shotStart; i<=shotEnd; i++){
                double[] img = hist[i];
                for (int j = 0; j < LUV_MAX; j++) avgHist[j] += img[j];
            }
            for(int i = 0 ; i < LUV_MAX; i++) avgHist[i] /= (shotEnd-shotStart);
            for(int i=shotStart; i<=shotEnd; i++){
                int dist = 0;
                for(int j=0; j<LUV_MAX; j++) dist += Math.abs(hist[i][j]-avgHist[j]);
                if(dist<minDist){
                    minDist = dist;
                    keyframe = i;
                }
            }
            gradualKeyframes.add(keyframe);
            if(index!=gtStart.size()-1)
            shotStart = gtEnd.get(index)+1;
        }
        gtStart.remove(gtStart.size()-1);
    }

    private int countTrueGTFrames() {
        int total = 0;
        for(boolean b: gtFrames) total += b ? 1 : 0;
        return total;
    }
}
