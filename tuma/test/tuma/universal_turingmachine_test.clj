(ns tuma.universal-turingmachine-test
  (:require [clojure.test :refer :all]
            [tuma.universal-turingmachine :as utm]))


(deftest binary-add-one-goedel-test
  (let [goedel (str
                "01001010010011"      ; <=> :q1 "1" :q1 "1" :right
                "010101010011"        ; <=> :q1 "0" :q1 "0" :right
                "01000100010001011"   ; <=> :q1 "_" :q3 "_" :left
                "0001001000101011"    ; <=> :q3 "1" :q3 "0" :left
                "0001010010010")      ; <=> :q3 "0" :q2 "1" :left
        utm (utm/new-utm goedel ["0" "1" "_"] ["0" "1" "_"])
        input "10011"
        expected "10100_"
        result (utm/emulate-utm utm input)]
    (is (= expected (:output result)) (str "expected: " expected " but was: " (:output result)))))

