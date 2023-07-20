section .data
    space:  dd 0
    x:  dd 0
    aux_ch:  dd 0
    y:  dd 0
    z:  dd 0
    h:  dd 0
    aux:  dd 0
    k:  dd 0
    printint: db "%d", 10, 0
    printchar: db "%c", 10, 0
    printfloat: db "%f", 10, 0

section .text
    global main

extern printf, exit

main:

MOV EAX, ' '

MOV [space], EAX

MOV EAX, 1
PUSH EAX

MOV EAX, 8
PUSH EAX

MOV EAX, 3
POP EBX
SUB EAX, EBX
PUSH EAX

MOV EAX, 2
POP EBX
ADD EAX, EBX
PUSH EAX

MOV EAX, 9
POP EBX
ADD EAX, EBX
POP EBX
ADD EAX, EBX

MOV [x], EAX

MOV EAX, 'X'

MOV [aux_ch], EAX


push dword [aux_ch]
push dword printchar
call printf
add esp, 8

push dword [x]
push dword printint
call printf
add esp, 8

push dword [space]
push dword printchar
call printf
add esp, 8
MOV EAX, 4
PUSH EAX

MOV EAX, 5
POP EBX
IMUL EAX, EBX

MOV [y], EAX

MOV EAX, 'Y'

MOV [aux_ch], EAX


push dword [aux_ch]
push dword printchar
call printf
add esp, 8

push dword [y]
push dword printint
call printf
add esp, 8

push dword [space]
push dword printchar
call printf
add esp, 8
MOV EAX, [y]
PUSH EAX

MOV EAX, [x]
POP EBX
ADD EAX, EBX

MOV [z], EAX

MOV EAX, 'Z'

MOV [aux_ch], EAX


push dword [aux_ch]
push dword printchar
call printf
add esp, 8

push dword [z]
push dword printint
call printf
add esp, 8

push dword [space]
push dword printchar
call printf
add esp, 8
MOV EAX, 0

MOV [h], EAX

MOV EAX, 'H'

MOV [aux_ch], EAX


push dword [aux_ch]
push dword printchar
call printf
add esp, 8
CMP EAX, 1
JE L1

MOV EAX, 'n'

MOV [aux], EAX


push dword [aux]
push dword printchar
call printf
add esp, 8
JMP L2
L1:

MOV EAX, 's'

MOV [aux], EAX


push dword [aux]
push dword printchar
call printf
add esp, 8
JMP L2
L2:


push dword [space]
push dword printchar
call printf
add esp, 8
MOV EAX, 2

MOV [x], EAX


push dword [x]
push dword printint
call printf
add esp, 8
MOV EAX, [y]

PUSH EAX

MOV EAX, 20

POP EBX

CMP EAX, EBX
JE L3
JE L3

MOV EAX, 3

MOV [x], EAX


push dword [x]
push dword printint
call printf
add esp, 8
JMP L4
L3:

MOV EAX, 5
PUSH EAX

MOV EAX, 2
POP EBX
ADD EAX, EBX

MOV [k], EAX


push dword [k]
push dword printint
call printf
add esp, 8
JMP L4
L4:


push dword [space]
push dword printchar
call printf
add esp, 8
MOV EAX, 'L'

MOV [aux_ch], EAX


push dword [aux_ch]
push dword printchar
call printf
add esp, 8
MOV EAX, 0

MOV [x], EAX
L5:

MOV EAX, [x]

PUSH EAX

MOV EAX, 10

POP EBX

CMP EAX, EBX
JLE L5

MOV EAX, 1
PUSH EAX

MOV EAX, [x]
POP EBX
ADD EAX, EBX

MOV [x], EAX


push dword [x]
push dword printint
call printf
add esp, 8
JMP L5
L6:

MOV EAX, 1
XOR EBX, EBX
INT 0x80
