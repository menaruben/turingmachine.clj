(ns tuma.core-test
  (:require [clojure.test :refer :all]
            [tuma.turingmachine :as tm]
            [tuma.transition :as trans]))

(deftest binary-add-by-one
  (testing "binary add by one turingmachine")
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
        expected {:input input :output "10100_" :end-state :q2 :verdict :accepted}
        actual (tm/emulate-tm tm input)]
    (is (= expected actual) (str "actual: " actual))))

(deftest add-one-infront-of-input-test
  (testing "add one infront of input turingmachine")
  (let [t1 (trans/new-transition :q1 "1" :q3 "1" :left)
        t2 (trans/new-transition :q1 "0" :q3 "0" :left)
        t3 (trans/new-transition :q3 "_" :q2 "1" :left)
        transitions [t1 t2 t3]
        states [:q1 :q2 :q3]
        input-symbols ["0" "1"]
        tape-symbols ["0" "1" "_"]
        accepted-states [:q2]
        tm (tm/new-turingmachine states input-symbols tape-symbols transitions :q1 "_" accepted-states)
        inputs ["000" "111" "10111011" "0" "1"]
        expected ["1000" "1111" "110111011" "10" "11"]
        actual (map #(:output (tm/emulate-tm tm %)) inputs)]
    (is (= expected actual) (str "actual: " actual))))

(deftest invert-input-test
  (testing "invert input turingmachine")
  (let [t1 (trans/new-transition :q1 "_" :q1 "_" :right)
        t2 (trans/new-transition :q1 "0" :q2 "1" :right)
        t3 (trans/new-transition :q1 "1" :q2 "0" :right)
        t4 (trans/new-transition :q2 "0" :q2 "1" :right)
        t5 (trans/new-transition :q2 "1" :q2 "0" :right)
        transitions [t1 t2 t3 t4 t5]
        states [:q1 :q2]
        input-symbols ["0" "1" "_"]
        tape-symbols ["0" "1" "_"]
        accepted-states [:q2]
        tm (tm/new-turingmachine states input-symbols tape-symbols transitions :q1 "_" accepted-states)
        inputs ["000" "111" "10111011" "0" "1" "" "abc" "___110001"]
        expected ["111" "000" "01000100" "1" "0" "" "abc" "___001110"]
        actual (map #(:output (tm/emulate-tm tm %)) inputs)]
    (is (= expected actual) (str "actual: " actual))))

(deftest binary-is-even-test
  (testing "binary is even turingmachine")
  (let [t1 (trans/new-transition :q1 "1" :q1 "1" :right)
        t2 (trans/new-transition :q1 "0" :q1 "0" :right)
        t3 (trans/new-transition :q1 "_" :q3 "_" :left)
        t4 (trans/new-transition :q3 "0" :q2 "0" :right)
        transitions [t1 t2 t3 t4]
        states [:q1 :q2 :q3]
        input-symbols ["0" "1"]
        tape-symbols ["0" "1" "_"]
        accepted-states [:q2]
        tm (tm/new-turingmachine states input-symbols tape-symbols transitions :q1 "_" accepted-states)
        inputs ["000" "111" "10111011" "0" "1"]
        exptected [:accepted :rejected :rejected :accepted :rejected]
        actual (map #(:verdict (tm/emulate-tm tm %)) inputs)]
    (is (= exptected actual) (str "actual: " actual))))

(deftest prefix-is-two-zeros-test
  (testing "prefix is two zeros turingmachine")
  (let [t1 (trans/new-transition :q1 "0" :q3 "0" :right)
         t2 (trans/new-transition :q3 "0" :q2 "0" :right)
         t3 (trans/new-transition :q2 "1" :q2 "1" :right)
         transitions [t1 t2 t3]
         states [:q1 :q2 :q3]
         input-symbols ["0" "1" "_"]
         tape-symbols ["0" "1" "_"]
         accepted-states [:q2]
         tm (tm/new-turingmachine states input-symbols tape-symbols transitions :q1 "_" accepted-states)
         inputs ["00111111" "0111" "00"]
         expected [:accepted :rejected :accepted]
         actual (map #(:verdict (tm/emulate-tm tm %)) inputs)]
     (is (= expected actual) (str "actual: " actual))))
