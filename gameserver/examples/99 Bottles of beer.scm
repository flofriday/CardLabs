; Prints the faomous 99-bottles of beers lyrics.
; https://www.99-bottles-of-beer.net/lyrics.html

(define (bottles-of-beer n)
  (if (= n 0)
    "No more bottles of beer on the wall, no more bottles of beer.\n"
    (string-append
      (string-append
        (number->string n)
        " bottle" (if (= n 1) "" "s") " of beer on the wall, ")
      (string-append
        (number->string n)
        " bottle" (if (= n 1) "" "s") " of beer.\n")
      "Take one down and pass it around, "
      (string-append
        (number->string (- n 1))
        " bottle" (if (= (- n 1) 1) "" "s") " of beer on the wall.\n\n"
        (bottles-of-beer (- n 1))))))

(display (bottles-of-beer 99))