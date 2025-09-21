Simulation: Simulation.o Block.o Cache.o Memory.o Set.o AddressDecoder.o PerformanceCounter.o
	g++ Simulation.o Block.o Cache.o Memory.o Set.o AddressDecoder.o PerformanceCounter.o -o Simulation

Simulation.o: Simulation.cc Cache.h Memory.h AddressDecoder.h Set.h Block.h PerformanceCounter.h
	g++ Simulation.cc -c

Cache.o: Cache.cc Cache.h AddressDecoder.h Set.h Block.h Memory.h PerformanceCounter.h
	g++ Cache.cc -c

AddressDecoder.o: AddressDecoder.cc AddressDecoder.h
	g++ AddressDecoder.cc -c

Set.o: Set.cc Set.h Memory.h AddressDecoder.h Block.h PerformanceCounter.h
	g++ Set.cc -c

Block.o: Block.cc Block.h Memory.h
	g++ Block.cc -c

Memory.o: Memory.cc Memory.h
	g++ Memory.cc -c

clean:
	rm -f *.o
	rm -f Simulation
	
