#include <stdio.h>
#include <chrono> // to bring in the chrono library
#include "Block.h"

// to make an instance of a high resolution clock called m_clock
std::chrono::high_resolution_clock m_clock;

Block::Block(int size, Memory* memPointer){ //constructor for block
    this->memPointer = memPointer;
    this->size = size;
    data = new unsigned char[size];
    for(int i = 0; i < size; i++){
        data[i] = 0;
    }
    valid = false;
    dirty = false;
    tag = 0;
    // to get the number of nanoseconds that have elapsed since the epoch (Jan 1, 1970) as a long
    timeStamp = std::chrono::duration_cast<std::chrono::nanoseconds>(m_clock.now().time_since_epoch()).count();
}

unsigned char* Block::loadFromMemory(int newTag, Memory* memPointer, unsigned long newLocation){ //loads a block from memory
    tag = newTag;
    valid = true;
    dirty = false;
    for(int i = 0; i < size; i++){ //Loops through the amount of data that a block can hold and loads it
        data[i] = memPointer->getByte(newLocation + i);
    }
    timeStamp = std::chrono::duration_cast<std::chrono::nanoseconds>(m_clock.now().time_since_epoch()).count();
    return data;
}

void Block::saveToMemory(Memory* memPointer, unsigned long newLocation){ //saves blocks back to memory when they are evicted
    for(int i = 0; i < size; i++){
        memPointer->setByte(data[i], newLocation + i);
    }
    //need to reset all of the blocks variables (tag, valid, time etc)
    valid = false;
    dirty = false;
    tag = 0;
    // to get the number of nanoseconds that have elapsed since the epoch (Jan 1, 1970) as a long
    timeStamp = std::chrono::duration_cast<std::chrono::nanoseconds>(m_clock.now().time_since_epoch()).count();
}
//Lots of getters and setters that we may or may not use throughout the project
int Block::getTag(){
    return tag;
};

void Block::setTag(int tag){
    this->tag = tag;
};

bool Block::getValid(){
    return valid;
};

void Block::setValid(bool valid){
    this->valid = valid;
};

bool Block::getDirty(){
    return dirty;
};

void Block::setDirty(bool dirty){
    this->dirty = dirty;
};

long Block::getTimeStamp(){
    return timeStamp;
};

void Block::display()
{//displays a block in a specific format
    printf("    Valid: %d   Tag: %d   Dirty: %d   TimeStamp: %ld\n", valid, tag, dirty, timeStamp);
    for(int i = 0; i < 4; i++){
        printf("    %02X  ", data[i]);
    }
    printf("\n");
}

unsigned char Block::read(unsigned long offset){
    //directly access requested byte using blockoffset as address
    timeStamp = std::chrono::duration_cast<std::chrono::nanoseconds>(m_clock.now().time_since_epoch()).count();
    return data[offset];
}


void Block::write(unsigned long offset, unsigned char value){
    //directly access requested byte using blockoffset as address
    timeStamp = std::chrono::duration_cast<std::chrono::nanoseconds>(m_clock.now().time_since_epoch()).count();
    this->dirty=true;
    data[offset] = value;
}