# CardScheme

CardScheme is the programming lanuage of CardLabs and a Scheme dialect which implements a reduced subset.
Note, that some implementations may also be different or behave in different ways than the official Scheme standard. You can see the documentation of CardScheme below.

## Types

### Booleans

`#f`... False
`#t`... True

### Integers

`<number>`, e.g. `154`

### Floats

`<float-number>`, e.g. `154.3764`

### Characters

`#\x` defines the character `x`

### Strings

`"<string>"`, e.g. `"ab\n"` defines a string with 3 characters

### Lists

`(list <argument1> ...)`, e.g. `(list 1 2 3)` defines a list with 3 elements

### Vectors

`#(obj)`, e.g. `#(0 "CardScheme" #t)` defines a vector with 3 elements

### Symbols

`'red` defines the symbol red

## Variable definitions

### Define

Syntax: `(define <variable> <expression>)`
Example: `(define x 3)` defines a variable `x` which is set to the initial value 3.

### Let

Syntax: `(let ((<variable1> <init1>)...) <body>)`
Example: `(let ((x 3)) (display x))` defines a variable `x` which is set to the initial value 3 and that can be used inside the body.
Any expression outside of the body can not access the corresponding variable definition (or will access an outside one if it exists). So for example, calling `(display x)` outside of our `let` body would not work in this example since `x` is only defined inside.

### Let\*

Syntax: `(let* ((<variable1> <init1>)...) <body>)"`
The `let*` construct is similar to `let` but the bindings are performed left to right which means that the first binding is accessible in the second binding and so on.
Example:

```Scheme
(let ((x 2) (y 3))
    (let* ((x 7)
        (z (+ x y)))
    (* z x)))
```

### Letrec

Syntax: `(letrec ((<variable1> <init1>)...) <body>)"`
In `letrec` the variable(s) are bound to a fresh location. They are then set to the evaluated init(s). Each variable is assigned in some unspecified order. This allows to define mutually recursive procedures.
Example:

```Scheme
(letrec (
    (even? (lambda (n)
        (if (zero? n)
            #t
            (odd? (- n 1)))))
    (odd? (lambda (n)
        (if (zero? n)
            #f
            (even? (- n 1))))))
    (even? 88))
```

### Letrec\*

Syntax: `(letrec* ((<variable1> <init1>)...) <body>)"`
In `letrec*` the variable(s) are bound to a fresh location. They are then set to the evaluated init(s). Each variable is assigned in left-to-right order.
Example:

```Scheme
(letrec* ((p
    (lambda (x)
        (+ 1 (q (- x 1)))))
    (q
        (lambda (y)
            (if (zero? y)
                0
                (+ 1 (p (- y 1))))))
        (x (p 5))
        (y x))
y)
```

## Procedures

The syntax for procedure calls is identical to Scheme:

```
(<procedure> <argument1> <argument2> ...)
```

### Lambdas

The syntax for lambdas is the following:
`(lambda <parameter-list> <body>)`

### Define shorthand

There is syntactic sugar for defining functions and binding them to a name:

```scheme
; Instead of
(define add1 (lambda (a b) (+ a b)))

; You can write
(define (add2 a b) (+ a b))
```

### Arithmetic operations

#### Addition

Syntax: `(+ <argument1> <argument2> ...)`
Example: `(+ 5 3)`

#### Subtraction

Syntax: `(- <argument1> <argument2> ...)`
Example: `(+- 5 3)`

#### Multiplication

Syntax: `(* <argument1> <argument2> ...)`
Example: `(* 5 3)`

#### Division

Syntax: `(/ <argument1> <argument2> ...)`
Example: `(/ 5 3)`

## Predefined functions

All the functions below are standard procedures of scheme and are best described on [this website](https://inst.eecs.berkeley.edu/~cs61a/sp19/articles/scheme-builtins.html).

Math functions: `floor-remainder modulo abs sqrt max min`

Comparison functions: `zero? positive? negative? odd? even? equal?`

Logic functions: `and or not`

List function: `pair? null? list car cdr set-car! set-cdr! cons append length reverse assoc`

Vector functions: `vector vector? make-vector vector-length vector-ref vector-set!`

String functions: `string->number number->string symbol->string string->symbol string-length make-string`

Symbol functions: `symbol? symbol=?`

Higher order functions: `apply map`

## IO capabilities

The only Input/Output capabilities that CardScheme provides is `display`, e.g.

```Scheme
(display  "hello world!\n")
```

This prints a message to a buffer that is safely captured and that can be printed.

## Sources

This guide was built with the help of the following sources:
[1] [TryScheme](https://try.scheme.org/), accessed on 24.01.2024 \
[2] [R7RS Small Edition](https://standards.scheme.org/official/r7rs.pdf), accessed on 24.01.2024
