#include <stdio.h>
#include "Memory.h"


Memory::Memory(int size){
    memSize = size; //sets memSize
    memory = new unsigned char[size]; //creates memory and populates it
    for(int i = 0; i < size; i++){
        memory[i] = i%255;
    }
}

//gets memory at address
unsigned char Memory::getByte(unsigned long address){
    return memory[address];
}

//sets memory at address to the value passed in
void Memory::setByte(unsigned long value, unsigned char address){
    memory[address] = value;
}

//Gets size of memory
int Memory::getSize(){
    return memSize;
}

//Displays memory
void Memory::display(){
    printf("MAIN MEMORY:\n");
    for(int i = 0; i < memSize; i++){
        printf("%02X ", memory[i]);
        
        if(i%16 == 15){
            printf("\n");
        }
    }
    printf("\n");
}