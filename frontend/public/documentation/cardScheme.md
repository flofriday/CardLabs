# CardScheme

CardScheme is a Scheme based language. CardScheme is a Scheme based language that provides you with some (but not all) Scheme language features. We only provide you with the necessary features to allow you to write competitive card game bots in order to make it easy to get started and to enable you to focus on what is important. Note, that some implementations may also be different or behave in different ways than the official Scheme standard. You can see the documentation of CardScheme below.

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

### Letrec

Syntax: `(letrec ((<variable1> <init1>)...) <body>)"`  
In `letrec` the variable(s) are bound to a fresh location. They are then set to the evaluated init(s). Each variable is assigned in some unspecified order. This allows to define mutually recursive procedures.

### Letrec\*

Syntax: `(letrec* ((<variable1> <init1>)...) <body>)"`  
In `letrec*` the variable(s) are bound to a fresh location. They are then set to the evaluated init(s). Each variable is assigned in left-to-right order.

## Procedures

The syntax for procedure calls is identical to Scheme:

```
(<procedure> <argument1> <argument2> ...)
```

### Lambdas

The syntax for lambdas is the following:
`(lambda <parameter-list> <body>)`

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

### Arithmetic functions

#### modulo

Syntax: `(modulo <argument1> <argument2>)`  
Example: `(modulo  5 3)`

#### abs

Syntax: `(abs <argument1>)`  
Example: `(abs -10)`

#### sqrt

Syntax: `(sqrt <argument1>)`  
Example: `(sqrt 16)`

## IO capabilities

The only Input/Output capabilities that CardScheme provides is `display`, e.g.

```Scheme
(display  "hello world!\n")
```

This prints a message to a buffer that is safely captured and that can be printed.

## Sources

This guide was built with the help of the following sources:  
[1] TryScheme (https://try.scheme.org/), accessed on 13.12.2023  
[2] R7RS Small Edition (https://standards.scheme.org/), accessed on 13.12.2023
