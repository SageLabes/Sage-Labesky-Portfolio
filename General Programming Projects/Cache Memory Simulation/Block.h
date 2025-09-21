#ifndef BLOCK_H
#define BLOCK_H

#include "Memory.h"

class Block {
    private:
        int tag;
        bool valid;
        bool dirty;
        long timeStamp;
        int size;
        Memory* memPointer;
        unsigned char* data;
    public:
        Block(int size, Memory* memPointer);
        unsigned char* loadFromMemory(int newTag, Memory* memPointer, unsigned long newLocation);
        void saveToMemory(Memory* memPointer, unsigned long newLocation);
        int getTag();
        void setTag(int tag);
        bool getValid();
        void setValid(bool valid);
        bool getDirty();
        void setDirty(bool dirty);
        long getTimeStamp();
        void display();
        unsigned char read(unsigned long offset);
        void write(unsigned long address, unsigned char value);
};

#endif