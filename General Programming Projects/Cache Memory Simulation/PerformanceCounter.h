#ifndef PERFORMANCECOUNTER_H
#define PERFORMANCECOUNTER_H

class PerformanceCounter{
    private:
        int hits;
        int misses;
        int writebacks;

    public:
        PerformanceCounter();
        void incrementHits();
        void incrementMisses();
        void incrementWritebacks();
        int getHits();
        int getMisses();
        int getWritebacks();
        float getMissPerc();
        float getWritebackPerc();
        void display();
};

#endif