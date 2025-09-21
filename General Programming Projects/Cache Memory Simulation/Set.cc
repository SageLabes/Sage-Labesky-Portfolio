#include <stdio.h>
#include "Set.h"
#include <cmath>

//constructor that creates the set and the list of blocks
Set::Set(int blockSize, int numBlocks, Memory* memPointer, AddressDecoder* decoder, PerformanceCounter* perfCounter){
    mainMemPointer = memPointer;
    this->blockSize = blockSize;
    this->numBlocks = numBlocks;
    blocks = new Block*[numBlocks];
    this->perfCounter = perfCounter;

    
    for(int i = 0; i < numBlocks; i++){
       blocks[i] = new Block(blockSize, memPointer);

    }
    this->decoder = decoder;
}
//displays set and calls block display
void Set::display(){
    printf("  Blocks\n");
    for(int i = 0; i < numBlocks; i++){
        printf("  %d:\n", i);
        blocks[i]->display();
    }

}
//helper function to check if the block is already loaded
int Set::preLoaded(unsigned long address){
    for(int i = 0; i < numBlocks; i++){
        if(blocks[i]->getValid() && blocks[i]->getTag()==decoder->getTag(address)){
            perfCounter->incrementHits();
            return i;
        }
    }
    return -1;
}
//helper function to find the oldest block
int Set::findOldest(){
    long oldestTime = blocks[0]->getTimeStamp();
    int oldestIndex = 0;
    for(int i = 0; i < numBlocks; i++){
        if(blocks[i]->getTimeStamp() < oldestTime){
            oldestTime = blocks[i]->getTimeStamp();
            oldestIndex = i;
        }
    }
    return oldestIndex;
}
//helper function that loads a new block and saves the old block to memory if the block is dirty
int Set::createBlock(unsigned long address){
    for(int i = 0; i < numBlocks; i++){
        if(!blocks[i]->getValid()){
                //creates address to load from memory
            unsigned long loadAdd = decoder->getTag(address) << 1;
            loadAdd |= decoder->getSetIndex(address);
            loadAdd <<= (int)log2(blockSize);
            blocks[i]->loadFromMemory(decoder->getTag(address), mainMemPointer, loadAdd);
            return i;
        }
    }
    //If all blocks are valid, find the oldest block
    int oldest = findOldest();
    //handles dirty blocks
    if(blocks[oldest]->getDirty()){
        //creates new address to save to memory
        unsigned long addSave = blocks[oldest]->getTag() << 1;
        addSave |= decoder->getSetIndex(address);
        addSave <<= (int)log2(blockSize);

        blocks[oldest]->saveToMemory(mainMemPointer, addSave);
        perfCounter->incrementWritebacks();
    }

    //creates address to load from memory
    unsigned long loadAdd = decoder->getTag(address) << 1;
    loadAdd |= decoder->getSetIndex(address);
    loadAdd <<= (int)log2(blockSize);
    blocks[oldest]->loadFromMemory(decoder->getTag(address), mainMemPointer, loadAdd);
    return oldest;
}
//helper function to call other helper functions
int Set::findID(unsigned long address){
    int id = preLoaded(address);
    if(id == -1){
        perfCounter->incrementMisses();
        id = createBlock(address);
    }
    return id;
}
//reads a block into the set
unsigned char Set::read(unsigned long numericalAddress){
    int index = findID(numericalAddress);

    unsigned long offset = decoder->getOffset(numericalAddress);
    return(blocks[index]->read(offset));

}
//writes a value into a block in the set
void Set::write(unsigned long address, unsigned char value){
    int index = findID(address);
    unsigned long offset = decoder->getOffset(address);
    blocks[index]->write(offset, value);
}