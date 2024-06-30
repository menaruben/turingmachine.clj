(ns tuma.turingmachine-test
  (:require [clojure.test :refer :all]
            [tuma.turingmachine :as tm]
            [tuma.transition :as trans]
            [tuma.messages :as msg]))

(deftest invalid-input-test
  (testing "invalid input turingmachine")
  (let [t1 (trans/new-transition :q1 "0" :q3 "0" :right)
        t2 (trans/new-transition :q3 "0" :q2 "0" :right)
        t3 (trans/new-transition :q2 "1" :q2 "1" :right)
        transitions [t1 t2 t3]
        states [:q1 :q2 :q3]
        input-symbols ["0" "1" "_"]
        tape-symbols ["0" "1" "_"]
        accepted-states [:q2]
        tm (tm/new-turingmachine states input-symbols tape-symbols transitions :q1 "_" accepted-states)
        inputs [0 1 :abc]]
    (doseq [input inputs]
      (is (thrown? AssertionError (tm/emulate-tm tm input)) "input word must be a string!"))))

(deftest invalid-tm-transition-test
  (testing "invalid transition for turingmachine because of invalid symbol (not inside tape-symbols)")
  (let [t1 (trans/new-transition :q1 "0" :q3 "0" :right)
        t2 (trans/new-transition :q3 "0" :q2 "0" :right)
        t3 (trans/new-transition :q2 "a" :q2 "1" :right)
        transitions [t1 t2 t3]
        states [:q1 :q2 :q3]
        input-symbols ["0" "1" "_"]
        tape-symbols ["0" "1" "_"]
        accepted-states [:q2]]
    (is (thrown? AssertionError
                 (tm/new-turingmachine states input-symbols tape-symbols transitions :q1 "_" accepted-states)
                 (msg/format-not-defined "a" "tape symbol")))))

(deftest invalid-tm-test
  (testing "invalid turingmachine")
  (let [t1 (trans/new-transition :q1 "0" :q3 "0" :right)
        t2 (trans/new-transition :q3 "0" :q2 "0" :right)
        t3 (trans/new-transition :q2 "1" :q2 "1" :right)
        transitions [t1 t2 t3]
        states [:q1 :q2 :q3]
        input-symbols ["0" 1 "_"]
        tape-symbols ["0" "1" "_"]
        accepted-states [:q2]]
    (is (thrown? AssertionError
                 (tm/new-turingmachine states input-symbols tape-symbols transitions :q1 "_" accepted-states)
                 (msg/format-not-defined 1 (msg/format-not-expected-type 1 "string"))))))
