(define (fib n)
              (if (<= n 2)
                1
                (+ (fib (- n 1)) (fib (- n 2)))))

; TODO: This might be beautiful as a do-loop
(define (printn n) (begin
                     (if (> n 1) (printn (- n 1)))
                     ; TODO: In the future we could also print the index but at the time of writing there are no
                     ; strings
                     (display (fib n))
                     (newline)))

(printn 24)