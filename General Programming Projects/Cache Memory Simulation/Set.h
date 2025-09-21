#ifndef SET_H
#define SET_H


#include "Memory.h"
#include "AddressDecoder.h"
#include "Block.h"
#include "PerformanceCounter.h"

class Set{
    private:
        Memory* mainMemPointer;
        int blockSize;
        Block** blocks;
        int numBlocks;
        AddressDecoder* decoder;
        PerformanceCounter* perfCounter;
        int preLoaded(unsigned long address);
        int findID(unsigned long address);
        int findOldest();
        int createBlock(unsigned long address);
    public:

    Set(int blockSize, int numBlocks, Memory* memPointer, AddressDecoder* decoder, PerformanceCounter* perfCounter);
    Set();
    void display();
    unsigned char read(unsigned long numericalAddress);
    void write(unsigned long address, unsigned char value);
};

#endif