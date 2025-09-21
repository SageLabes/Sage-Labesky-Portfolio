#ifndef ADDRESSDECODER_H
#define ADDRESSDECODER_H

class AddressDecoder{
    private:
        int tagBits;
        int setBits;
        int offsetBits;
    public:
    AddressDecoder(int tagBits, int setBits, int offsetBits);
    unsigned long getTag(unsigned long address);
    unsigned long getSetIndex(unsigned long address);
    unsigned long getOffset(unsigned long address);
    unsigned long getAddress(unsigned long address);
};

#endif
