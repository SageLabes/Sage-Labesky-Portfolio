# include <stdio.h>
# include <stdlib.h>
# include <string.h>
#define HEAP_SIZE (128*8)
#define FIRST_FIT 0
#define BEST_FIT 1
//memory block header struct
typedef struct memoryBlockHeader {
    int free; // 0 = used, 1 = free
    int size; // size of the reserved block
    int managedIndex; // the unchanging index in the managed array
    int survivalAmt; // the number of times the block has moved between young heaps
    struct memoryBlockHeader* next;
} memoryBlockHeader;
//global variables
memoryBlockHeader* freeListHead[3]; //head of the free linked list

int fit_type = FIRST_FIT; //type of fit
unsigned char heap[3][HEAP_SIZE]; //3 heaps in an array
int activeHeap = 0; //active heap
void* managedList[HEAP_SIZE/8]; //list of managed pointers
int managedSize; //size of managed list



void duInitMalloc(int fitType){
    fit_type = fitType;
    //Stamp header into starting 16 bytes
    memoryBlockHeader* currentBlock = (memoryBlockHeader*)heap[activeHeap];
    memoryBlockHeader* oldBlock = (memoryBlockHeader*)heap[2];
    
    //Set size of free block
    currentBlock->free=1;
    currentBlock->size = HEAP_SIZE - sizeof(memoryBlockHeader);
    currentBlock->next = NULL;
    oldBlock->free=1;
    oldBlock->size = HEAP_SIZE - sizeof(memoryBlockHeader);
    oldBlock->next = NULL;

    //Point list head to the block header
    freeListHead[activeHeap] = currentBlock;
    freeListHead[2] = oldBlock;
}


void duFreeListPrint(int curHeap){
    //prints the contents of the free list
    printf("Memory Dump\nFree List\n");

    memoryBlockHeader* currentBlock = freeListHead[curHeap];

    //Traverse through linked list and print the block's location and size
    while (currentBlock!=NULL){
        printf("Block at %p, size %d\n", currentBlock, currentBlock->size);
        currentBlock = currentBlock->next;
    }
}

void duMemBlockPrint(int curHeap){
    //variables to keep track of the string representation
    char lowlet = 'a';
    char upLet = 'A';
    int upOrLow = 0; //0 for low, 1 for up
    char strRepresentation[HEAP_SIZE/8];
    int stringCounter = 0;

    //get the starting block header
    memoryBlockHeader* currentBlock = (memoryBlockHeader*)&(heap[curHeap][0]);
    //Traverse through list and print the block's location and size with string representation
    
    int totalSize = 0; //variable to keep track of the total size of the blocks so that we don't traverse into the other heap
    while (currentBlock->size>-1 && totalSize<HEAP_SIZE){
        totalSize += currentBlock->size + sizeof(memoryBlockHeader);

        char* freeOrUsed = "Free"; //string to hold free or used for the output
        if(currentBlock->free == 0){
            freeOrUsed = "Used";
        }
        printf("%s Block at %p, size %d, survival %d\n",freeOrUsed, currentBlock, currentBlock->size, currentBlock->survivalAmt);

        //logic for the string representation
        upOrLow = 1-currentBlock->free;
        for(int i=0; i<(currentBlock->size)/8; i++){
            if(upOrLow == 0){
                strRepresentation[stringCounter] = lowlet;
            } else {
                strRepresentation[stringCounter] = upLet;
            }
            stringCounter++; 
       }
       strRepresentation[stringCounter]='\0';
       if(upOrLow == 0){
            lowlet++;
       } else {
            upLet++;
       }
        //move to the next block
        currentBlock = (memoryBlockHeader*)((unsigned char*)currentBlock + currentBlock->size + sizeof(memoryBlockHeader));

        
    }
    printf("%s\n", strRepresentation);
}

void duManagedPrint(int curHeap){
    //prints the managed list
    printf("Managed List\n");
    for(int i=0; i<managedSize; i++){
        printf("ManagedList[%d]= %p\n", i, managedList[i]);
    }

}

void duMemoryDump(){
    //calls the various helper memory dump functions
    printf("Current heap (0/1 young): %d\n",activeHeap);
    printf("Young Heap (only the current one)\n");
    duMemBlockPrint(activeHeap);
    duFreeListPrint(activeHeap);
    printf("Old Heap\n");
    duMemBlockPrint(2);
    duFreeListPrint(2);
    duManagedPrint(activeHeap);
    printf("\n\n\n"); //extra new lines for emphasis
    }

void* duMalloc(int bytes, int heapGen){
    //Check is size is a multiple of 8 and if not then round up to the next multiple of 8
    if(bytes%8 !=0){
        bytes=bytes + (8-(bytes%8));
    }
    //memory block headers to be used to find the block to use for the malloc
    memoryBlockHeader* bitToUse = freeListHead[heapGen];
    memoryBlockHeader* tempBlock=NULL;
    memoryBlockHeader* previousBlock=NULL;
    memoryBlockHeader* currentBlock=freeListHead[heapGen];
    int bestFit = HEAP_SIZE + 1; //variable to keep track of the best fit IS THIS LINE OF CODE NECESSARY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!????????????????

    //traverses the free list to find the first large enough block
    if(fit_type == FIRST_FIT){
        //variable to end the loop once it is found
        int end_loop = 0;
        while(currentBlock != NULL && end_loop == 0){
            if(currentBlock->size >= bytes + sizeof(memoryBlockHeader)){
                bitToUse = currentBlock;
                previousBlock = tempBlock;
                end_loop = 1;
            }
            tempBlock = currentBlock; //need to differentiate between tempblock an previous block since when the loop ends tempblock will be the same as current block which will be the same as bitToUse
            currentBlock = currentBlock->next;
        }
    }
    else{ //code for best fit
        int bestFit = HEAP_SIZE; //variable to keep track of the best fit
       
        while(currentBlock != NULL){
            if(currentBlock->size >= bytes + sizeof(memoryBlockHeader)){ //COULD WE COMBINE THE TWO IF STATEMENTS INTO ONE
                if (currentBlock->size < bestFit){ //if the current block is smaller than the current best fit then update the best fit (it must be larger because of the previous if)
                    bestFit = currentBlock->size;
                    bitToUse = currentBlock;
                    previousBlock = tempBlock;
                }
            }
            tempBlock = currentBlock;
            currentBlock = currentBlock->next;
        
        }
    }
    //returns null if no block is found
    if(bitToUse==NULL){
        return NULL;
    }
    //if the block is large enough then break it up into two blocks
    if (bitToUse->size >= bytes + sizeof(memoryBlockHeader)){
        // breaking up into two blocks and adjusting size then stamping in new free block header
        memoryBlockHeader* newBlock= (memoryBlockHeader*)((unsigned char*)bitToUse + bytes + sizeof(memoryBlockHeader));
        //adjusting the elements of the new block and the old block
        newBlock->next=bitToUse->next;
        newBlock->size= bitToUse->size - bytes - sizeof(memoryBlockHeader);
        bitToUse->free=0;
        bitToUse->size=bytes;
        bitToUse->survivalAmt = 0;
        newBlock->free=1;
        
        bitToUse->next=NULL;

        //special case if new block is the head
        if (previousBlock==NULL){
            freeListHead[heapGen]=newBlock;
        }
        else{
            previousBlock->next=newBlock;
        }
        

        //return mem location of where the data starts
        return ((void*)bitToUse) + sizeof(memoryBlockHeader);
    }
    return NULL;
}

void duFree(void* ptr){
    int workingHeap=0;
    //figure out if the ptr is in the old heap or young ones
    if(ptr<(void*)heap[2]){ 
        workingHeap=activeHeap;
    }
    else{
        workingHeap=2;
    }

    // find start of block header and cast it
    memoryBlockHeader* Header= (memoryBlockHeader*)(ptr-sizeof(memoryBlockHeader));
    Header->free = 1;
    //Get the pointer header into the correct free list position
    if(Header<freeListHead[workingHeap]){//If the pointer header goes at the begining then put it as the free list head
        Header->next= freeListHead[workingHeap];
        freeListHead[workingHeap]= Header;
    }
    else{ //otherwise look for the correct position to insert it into 
        memoryBlockHeader* currentBlock= freeListHead[workingHeap];
        memoryBlockHeader* previousBlock= NULL;
        while(currentBlock!=NULL && currentBlock<Header){
            previousBlock=currentBlock;
            currentBlock=currentBlock->next;
        }

        //Once the correct position is found insert the pointer header 
        Header->next=currentBlock;
        previousBlock->next=Header;
       
    }
}

void duManagedInitMalloc(int searchType){
    //initializes the managed list
    duInitMalloc(searchType);
    managedSize = 0;
    managedList[0] = NULL;

}

void** duManagedMalloc(int size){
    //calls duMalloc and adds that pointer to the managed list
    void* ptr = duMalloc(size, activeHeap);
    if(ptr == NULL){
        return NULL;
    }
    managedList[managedSize] = ptr;
    //sets the managed index of the block header to the managed size
    memoryBlockHeader* temp = (memoryBlockHeader*)(ptr - sizeof(memoryBlockHeader));
    temp->managedIndex = managedSize;

    managedSize++;
    //returns a permanant pointer to the temporary pointer to the allocated space
    return (void**)&(managedList[managedSize-1]);
}

void duManagedFree(void** mptr){
    duFree(*mptr);
    //finds the block header and sets the managed list index to null
    memoryBlockHeader* temp = (memoryBlockHeader*)(*mptr - sizeof(memoryBlockHeader));
    managedList[temp->managedIndex] = NULL;

    *mptr = NULL;
}


void minorCollection(){
    //switches the active heap
    activeHeap = 1 - activeHeap;
    //destination in new heap where old information will go
    unsigned char* destination = heap[activeHeap];

    for(int i=0; i<managedSize; i++){
        //traverses the managed list
        if(managedList[i] != NULL){
            //copies allocated memory to the new heap at the top
            memoryBlockHeader* source = (memoryBlockHeader*)((unsigned char*)managedList[i] - sizeof(memoryBlockHeader));
            source->survivalAmt++;
            if(source->survivalAmt == 3){
                void* temp = duMalloc(source->size, 2);
                temp = temp - sizeof(memoryBlockHeader);
                memcpy(temp, source, source->size + sizeof(memoryBlockHeader));

                managedList[i] = temp + sizeof(memoryBlockHeader);
            } else{
                memcpy(destination, source, source->size + sizeof(memoryBlockHeader));
                
                managedList[i] = destination + sizeof(memoryBlockHeader);
                
                destination += source->size + sizeof(memoryBlockHeader);
            }
        }
    }
    //sets the new free list head
    freeListHead[activeHeap] = (memoryBlockHeader*)destination;
    
    freeListHead[activeHeap]->size = HEAP_SIZE - (destination - heap[activeHeap]) - sizeof(memoryBlockHeader);
    freeListHead[activeHeap]->free = 1;
    freeListHead[activeHeap]->next = NULL;
    freeListHead[activeHeap]->survivalAmt = 0;
}

void majorCollection(){
    memoryBlockHeader* currentBlock = freeListHead[2];
    
    int totalSize = 0; //variable to keep track of the total size of the blocks so that we don't traverse into the other heap
    while (currentBlock->size>-1 && totalSize<HEAP_SIZE){
        totalSize += currentBlock->size + sizeof(memoryBlockHeader);
        memoryBlockHeader* prevBlock = currentBlock;
        currentBlock = (memoryBlockHeader*)((unsigned char*)currentBlock + currentBlock->size + sizeof(memoryBlockHeader));

        //if the previous block and the current block are both free then coallesce them
        if(prevBlock->free == 1 && currentBlock->free == 1){
            prevBlock->size += currentBlock->size + sizeof(memoryBlockHeader);
            prevBlock->next = currentBlock->next;
            currentBlock = prevBlock;
        } else if(prevBlock->free == 1 && currentBlock->free == 0){ // if the previous block is free and the current block is used then swap them
            //holds information we need to give to the next block
            memoryBlockHeader* temp = prevBlock->next;
            int tempSize = prevBlock->size;
            //puts the information from the block that is used into the location of the old block
            memcpy(prevBlock, currentBlock, currentBlock->size + sizeof(memoryBlockHeader));
            //puts the address of the newly moved block into the managed list
            managedList[prevBlock->managedIndex] = (void*)((unsigned char*)prevBlock + sizeof(memoryBlockHeader));
            //creates a new block at the location just after the newly moved used block
            memoryBlockHeader* new = (memoryBlockHeader*)((unsigned char*)prevBlock + prevBlock->size + sizeof(memoryBlockHeader));
            //sets it as a new free block with the correct size
            new->size = tempSize;
            new->free = 1;
            new->next = temp;
            new->survivalAmt = 0;
            //that new free block is the next freelisthead
            freeListHead[2] = new;
            //progresses so we continue starting from the first free block
            currentBlock = new;
        }
    }
}
