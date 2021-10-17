.globl __start; Экспортирование глобальной переменной (нужно, чтобы вызывающий (ОС) знал, шде начинается программа)

.rodata; Секция данных. Здесь хранятся глобальные переменные
  fmtn: .string "t0 IS:"
  msg: .string "Input number:"
  ext: .string "Exiting"
  nl: .string "\n"

.text; Секция комманд

read_n:;Функция, читает строку, возвращает в a1
  li a0, 4 ;Встроенный вызов: вывести строку на консоль
  la a1, msg; Загрузить строку msg
  ecall; Вызвать системный вызов
  
  li a0, 5; Системный вызов прочитать число
  ecall
  ret

prnt_s:; Функция, печатает строку из регистра a0: printf("%s\n", a0)
  mv a1, a0
  li a0, 4;
  ecall

  ;print \n
  li a0, 4;
  la a1, nl
  ecall
  ret
  
revert:;Переворачивает десятичное представление числа
;  div t2, t1, t0; t2 = t1 / t0  
  mv t1, a0; t1 = a0
  li t6, 10
  
  loop:
    rem t3, t1, t6; t3 = t1 %t6
    ;t1 = t1 / t6
    div t5, t1, t6
    mv t1, t5
    ;print t1
    mv a1, t3
    li a0, 1
    ecall
  bgtz t1, loop
  
  ret

__start:; Точка входа в программу
  call read_n
  call revert
  
  li a0, 10;Завершить программу  exit(0)
  ecall
  ret
  
