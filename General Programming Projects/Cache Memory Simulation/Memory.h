#ifndef MEMORY_H
#define MEMORY_H


class Memory{
    private:
        int memSize;
        unsigned char* memory;
    public:
        Memory(int size);
        unsigned char getByte(unsigned long address);
        void setByte(unsigned long address, unsigned char value);
        int getSize();
        void display();
};

#endif