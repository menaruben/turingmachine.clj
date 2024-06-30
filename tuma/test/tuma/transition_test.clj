(ns tuma.transition-test
  (:require [clojure.test :refer :all]
            [tuma.turingmachine :as tm]
            [tuma.transition :as trans]
            [tuma.messages :as msg]))

(deftest undefined-source-in-transition-test
  (testing "undefined source in turingmachine")
  (let [t1 (trans/new-transition :q1 "0" :q3 "0" :right)
        t2 (trans/new-transition :q3 "0" :q2 "0" :right)
        t3 (trans/new-transition :q5 "1" :q2 "1" :right)
        transitions [t1 t2 t3]
        states [:q1 :q2 :q3]
        input-symbols ["0" "1" "_"]
        tape-symbols ["0" "1" "_"]
        accepted-states [:q2]]
    (is (thrown? AssertionError
                 (tm/new-turingmachine states input-symbols tape-symbols transitions :q1 "_" accepted-states)
                 (msg/format-invalid-type-of "Turingmachine")))))

(deftest invalid-transition-test
  (testing "invalid transition in general")
  (is (thrown? AssertionError (trans/new-transition :q1 "0" "q1" "0" :right) (msg/format-not-expected-type "q1" "keyword"))))
