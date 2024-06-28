(load-file "../turingmachine.clj")

(ns turingmachine)

(def t1 (Transition. :q1 "1" :q3 "1" :left))
(def t2 (Transition. :q1 "0" :q3 "0" :left))
(def t3 (Transition. :q3 "_" :q2 "1" :left))
(def transitions (list t1 t2 t3))

(def states (list :q1 :q2 :q3))
(def input-symbols (list "0" "1"))
(def tape-symbols (list "0" "1" "_"))
(def accepted-states (list :q2))

(def utm (Turingmachine. states input-symbols tape-symbols transitions :q1 "_" accepted-states))
(def inputs (list "000" "111" "10111011" "0" "1"))
(doseq [input inputs] (println (emulate-utm utm input)))
