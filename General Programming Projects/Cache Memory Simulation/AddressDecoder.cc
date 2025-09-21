#include "AddressDecoder.h"
#include <stdio.h>

//constructor
AddressDecoder::AddressDecoder(int tagBits, int setBits, int offsetBits){
    this->tagBits = tagBits;
    this->setBits = setBits;
    this->offsetBits = offsetBits;
}
//figures out the tag of a given address
unsigned long AddressDecoder::getTag(unsigned long address){
    return address >> (setBits + offsetBits);
}
//figures out the set index of a given address
unsigned long AddressDecoder::getSetIndex(unsigned long address){
    return (address >> offsetBits) & ((1 << setBits) - 1);
}
//figures out the offset of a given address
unsigned long AddressDecoder::getOffset(unsigned long address){
    return (address & ((1 << offsetBits) - 1));
}
//figures out the address of a given address
unsigned long AddressDecoder::getAddress(unsigned long address){
    return (getTag(address) << setBits) | getSetIndex(address);
}