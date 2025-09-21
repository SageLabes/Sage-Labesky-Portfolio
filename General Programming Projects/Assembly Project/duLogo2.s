.data

grid:
 .word 0xFFFFFC00
 .word 0xFFFFFE00
 .word 0xFFFFFF00
 .word 0x3FFFFF00

 .word 0x3F003F00
 .word 0x3F003F00
 .word 0x3F3F3F3F
 .word 0x3F3F3F3F

 .word 0x3F3F3F3F
 .word 0x3F3F3F3F
 .word 0x3F3F3F3F
 .word 0x3F3F3F3F

 .word 0x3F3F3F3F
 .word 0x3F3F3F3F
 .word 0x3F3F3F3F
 .word 0x3F3F3F3F

 .word 0x3F3F3F3F
 .word 0x3F3F3F3F
 .word 0x3F003F3F
 .word 0x3F003F3F

 .word 0x3FFFFF3F
 .word 0xFFFFFF3F
 .word 0xFFFFFE3F
 .word 0xFFFFFC3F

 .word 0x0000007F
 .word 0x000000FF
 .word 0x003FFFFF
 .word 0x003FFFFF

 .word 0x001FFFFE
 .word 0x000FFFFC


.text

main:
    jal drawBackground
    # ecall exit
    li a0, 10 # exit
    ecall


drawBackground:
    addi sp, sp, -12
    sw ra, 0(sp)
    sw s1, 4(sp)
    sw s0, 8(sp)
   


    li t2, 32 #number of bits per word
    li s1, 0 #outer loop
    li t4, 30 #number of words

    outer_loop:
      li s0, 0 #inner

        inner_loop:

            
            #set x,y location
            add a0, zero, s0 
            add a1, zero, s1

            jal lightLED

            addi s0, s0, 1
            blt s0, t2, inner_loop


        
        addi s1, s1, 1
        blt s1,t4, outer_loop

    lw ra, 0(sp)

    addi sp, sp, 12

    jr ra
    

lightLED:
    addi sp, sp, -12
    sw ra, 0(sp)
    sw a0, 4(sp)
    sw a1, 8(sp)

    jal lightColor

    mv a2,a0

    lw ra, 0(sp)
    lw a0, 4(sp)
    lw a1, 8(sp)
    addi sp, sp, 12

    slli t5, a0, 16
    or t5, t5, a1

    li a0, 0x100


    mv a1, t5

    ecall

    ret

lightColor:
    la t3, grid
    li t1, 0
    
    add t1, t1, a1
    slli t1, t1, 2
    add t3, t3, t1


    lw t3, 0(t3) 
    li t6, 32



    # get shift

    sub t6, t6, a0
    addi t6, t6 -1

    #perform shift 
    srl t3, t3, t6
    andi t3, t3, 1

     bne t3, zero, else
                li a0, 0xA89968 #crimson
                
                beq zero, zero, end
            
            else:
                li a0, 0xBA0C2F #otherwise gold
            end:
            ret


nop