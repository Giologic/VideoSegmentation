package mmr;

/**
 * Created by Gio on 11/22/2016.
 *
 * NOTE: NOT YET TESTED
 */
public class Video {

    public final static int LUV_MAX = 159;
    Image[] imgArray;
    int imgArraySize;
    public Video(){}

    public int retrieveKeyFramesViaAverageHistogram(Image[] imgArray, int imgArraySize){
        double[] imgArrayAverage = new double[LUV_MAX];
        double[] imgArrayAverageNH = new double[LUV_MAX];
        double[] imgDistance = new double[imgArraySize];
        double[] imgDistanceNormalized = new double[imgArraySize];
        double[] temp;
        int area = imgArray[0].area;

        //RUN THROUGH HISTOGRAMS AND ADD THEM TO THE ARRAY
        for(int i = 0 ; i < imgArraySize; i++) {
            temp = imgArray[i].hist;
            for(int j = 0 ; j < LUV_MAX; j++) {
                imgArrayAverage[j] += temp[j];
            }
        }
        //GET THE AVERAGE
        for(int i = 0 ; i < LUV_MAX; i++) {
            imgArrayAverage[i] /= imgArraySize;
        }

        //COMPUTE THE DISTANCE BETWEEN TWO FRAMES (FOREACH IMAGE)
        for(int i = 0 ; i < imgArraySize; i++) {
            int accumulator = 0;
            for(int j = 0 ; j < LUV_MAX; j++) {
                accumulator += euclideanDistance(imgArray[i].hist[j], imgArrayAverage[j]);
            }
            imgDistance[i] = accumulator;
        }

        //GET THE FRAME NEAREST TO AVERAGE
        int selected = 0;
        for(int i = 0; i < imgArraySize; i++) {
            if(imgDistance[i] < imgDistance[i+1]) {
                selected = i+1;
            }
        }

        //GET NORMALIZED AVERAGE FREQUENCY
        imgArrayAverageNH = imgArrayAverage;
        for(int i = 0; i < LUV_MAX; i++) {
            imgArrayAverageNH[i] /= area;
        }

        //GET NEAREST DISTANCE NORMALIZED
        for(int i = 0 ; i < imgArraySize; i++) {
            int accumulator = 0;
            for(int j = 0 ; j < LUV_MAX; j++) {
                accumulator += euclideanDistance(imgArray[i].nh[j], imgArrayAverage[j]);
            }
            imgDistanceNormalized[i] = accumulator;
        }

        //GET THE FRAME NEAREST TO AVERAGE
        int selected2 = 0;
        for(int i = 0; i < imgArraySize; i++) {
            if(imgDistanceNormalized[i] < imgDistanceNormalized[i+1]) {
                selected2 = i+1;
            }
        }

        return selected2;
    }

    private double euclideanDistance(double a, double b) {
        return Math.sqrt(Math.pow(a,2) + Math.pow(b,2));
    }
}
