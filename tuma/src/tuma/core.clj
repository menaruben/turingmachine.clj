(ns tuma.core
  (:require [tuma.turingmachine :as tm]
            [tuma.transition :as trans]))

(defn -main []
  ; turingmachine that adds one to a binary number and traces the transitions
  (let [t1 (trans/new-transition :q1 "1" :q1 "1" :right)
        t2 (trans/new-transition :q1 "0" :q1 "0" :right)
        t3 (trans/new-transition :q1 "_" :q3 "_" :left)
        t4 (trans/new-transition :q3 "1" :q3 "0" :left)
        t5 (trans/new-transition :q3 "0" :q2 "1" :left)
        transitions [t1 t2 t3 t4 t5]
        states [:q1 :q2 :q3]
        input-symbols ["0" "1"]
        tape-symbols ["0" "1" "_"]
        accepted-states [:q2]
        tm (tm/new-turingmachine states input-symbols tape-symbols transitions :q1 "_" accepted-states)
        input "10011"
        result (tm/emulate-tm tm input true)]
    (println result)))
