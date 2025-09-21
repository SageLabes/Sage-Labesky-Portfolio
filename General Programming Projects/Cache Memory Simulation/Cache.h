#ifndef CACHE_H
#define CACHE_H


#include "Set.h"
#include "AddressDecoder.h"
#include "Memory.h"
#include "PerformanceCounter.h"

class Cache{
    private:
        int cacheSize;
        int blockSize;
        int setAssoci;
        Memory* memPointer;
        AddressDecoder* decoder;
        PerformanceCounter* perfCounter;
        Set** sets;
    public:
        Cache(Memory* memPointer, int cacheSize, int blockSize, int setAssoci);
        unsigned char read(unsigned long address);
        void write(unsigned long newAddress,int value);
        void display();    
};

#endif