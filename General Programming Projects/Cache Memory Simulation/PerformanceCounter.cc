#include "PerformanceCounter.h"
#include <stdio.h>
//constructor creates the variables to keep track of data on hits, misses, and writebacks
PerformanceCounter::PerformanceCounter(){
    hits = 0;
    misses = 0;
    writebacks = 0;
}

//incrementers for the variables
void PerformanceCounter::incrementHits(){

    hits++;
}
void PerformanceCounter::incrementMisses(){

    misses++;
}
void PerformanceCounter::incrementWritebacks(){

    writebacks++;
}
//getters for the variables
int PerformanceCounter::getHits(){

    return hits;

}
int PerformanceCounter::getMisses(){

    return misses;
};
int PerformanceCounter::getWritebacks(){

    return writebacks;
}
float PerformanceCounter::getMissPerc(){

    return (float)misses/(hits+misses);
}
float PerformanceCounter::getWritebackPerc(){

    return (float)writebacks/(hits+misses);
}
//Displays the preformance calculations
void PerformanceCounter::display(){
    printf("Accesses: %d\n", hits+misses);
    printf("Hits: %d\n", hits);
    printf("Misses: %d\n", misses);
    printf("Writebacks: %d\n", writebacks);
    printf("Miss Percentage: %f\n", getMissPerc()*100);
    printf("Writeback Percentage: %f\n", getWritebackPerc()*100);
}