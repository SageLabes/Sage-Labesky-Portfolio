mallocTestVersion4: mallocTestVersion4.o duMalloc.o
	gcc mallocTestVersion4.o duMalloc.o -o mallocTestVersion4

mallocTestVersion4.o: mallocTestVersion4.c duMalloc.h
	gcc mallocTestVersion4.c -c

duMalloc.o: duMalloc.c duMalloc.h
	gcc duMalloc.c -c

clean:
	rm -f *.o
	rm -f *.o mallocTestVersion4
	

