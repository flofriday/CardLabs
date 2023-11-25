(define (fib n)
              (if (<= n 2)
                1
                (+ (fib (- n 1)) (fib (- n 2)))))

(do (
      (i 1 (+ i 1)))
  ((> i 24))
  (display (fib i))
  (newline))