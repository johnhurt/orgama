/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.cache;

/**
 * Class output by the output cache cleanup method that indicates the number of 
 * objects kept and discarded as well as the remaining read times
 * @author kguthrie
 */
public class CleanupResults {
    int numKept;
    int numDiscarded;
    double remainingTotalReadAge;
    double remainingMinAccessTime;

    public CleanupResults() {
        numKept = 0;
        numDiscarded = 0;
        remainingTotalReadAge = 0;
        remainingMinAccessTime = Double.MAX_VALUE;
    }
    
    
}
