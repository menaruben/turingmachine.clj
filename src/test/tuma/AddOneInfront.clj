(ns test.tuma.AddOneInfront
  (:require [main.tuma.turingmachine :as tm]
            [main.tuma.transition])
  (:import [main.tuma.transition Transition])
  (:import [main.tuma.turingmachine Turingmachine]))

(defn -main []
  (def t1 (Transition. :q1 "1" :q3 "1" :left))
  (def t2 (Transition. :q1 "0" :q3 "0" :left))
  (def t3 (Transition. :q3 "_" :q2 "1" :left))
  (def transitions [t1 t2 t3])

  (def states [:q1 :q2 :q3])
  (def input-symbols ["0" "1"])
  (def tape-symbols ["0" "1" "_"])
  (def accepted-states [:q2])

  (def tm (Turingmachine. states input-symbols tape-symbols transitions :q1 "_" accepted-states))
  (def inputs ["000" "111" "10111011" "0" "1"])
  (doseq [input inputs] (println (tm/emulate-tm tm input))))
