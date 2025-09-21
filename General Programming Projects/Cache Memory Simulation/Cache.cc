#include "Cache.h"
#include <cmath>
#include <stdio.h>

//contructor for cache
Cache::Cache(Memory* memPointer, int cacheSize, int blockSize, int setAssoci){
    this->cacheSize = cacheSize;
    this->blockSize = blockSize;
    this->setAssoci = setAssoci;
    this->memPointer = memPointer;

    int numSets = cacheSize/blockSize/setAssoci; //calculates the number of sets in the cache
    perfCounter = new PerformanceCounter();

    int tagBits = (int)log2(memPointer->getSize()) - (int)log2(blockSize) - (int)log2(setAssoci); //figures out the number of bits in the tag
    int indexBits = (int)log2(numSets);
    int offsetBits = (int)log2(blockSize);
    decoder = new AddressDecoder(tagBits, indexBits, offsetBits); //creates the address decoder with that information
    
    sets = new Set*[numSets]; //creates an array of sets

    for(int i = 0; i < numSets; i++){ // populates the array of sets
        sets[i] = new Set(blockSize, setAssoci, memPointer, decoder, perfCounter);
    }
}
//reads memory (calls set read)
unsigned char Cache::read(unsigned long address){
    unsigned long setIndex = decoder->getSetIndex(address);
    return sets[setIndex]->read(address);
}
//writes memory (calls set write)
void Cache::write(unsigned long newAddress, int value){
    unsigned long setIndex = decoder->getSetIndex(newAddress);
   unsigned long blockAddress = decoder->getAddress(newAddress);
    sets[setIndex]->write(newAddress, value);
}
//displays the cache in a specific format
void Cache::display(){
    printf("CACHE:\n");
    for(int i = 0; i < (cacheSize/(blockSize*setAssoci)); i++){
        printf(" Set %d:\n", i);
        sets[i]->display();
    }
    
    perfCounter->display();
}