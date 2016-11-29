package mmr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
    Precomputing:
    Writes the normalized histograms of each image in the dataset into a text
    file. Also writes localized histograms to a different text file. Also 
    writes LUV matrices per image.

    Only need to run once tho. Once txt files are created, no need to run again.
*/
public class RunThisFirst {
    public static void main(String[] args) throws IOException {
        String[] dirs = {"videos/uni/", "videos/mjack/", "videos/777/"};
        String[] filenames = {"UniHistos.txt", "MJackHistos.txt", "777Histos.txt"};
        for(int i=0; i<3; i++){
            File dir = new File(dirs[i]);
            File[] directoryListing = dir.listFiles();
            BufferedWriter bw = new BufferedWriter(new FileWriter(filenames[i]));
            bw.write(directoryListing.length+"\r\n");
            for (File child : directoryListing) {
                System.out.println((i+1)+" "+child.getName());
                if(!child.getName().endsWith("jpg")) continue;
                Image img = new Image(dirs[i]+child.getName());
                String toWrite = child.getName();
                bw.write(toWrite+"\r\n");
                bw.write(img.getStringArr(img.hist)+"\r\n");
            }
            bw.close();
        }
    }
}
