(ns main.tuma.turingmachine
  (:require [clojure.spec.alpha :as spec])
  (:require [main.tuma.transition :as trans])
  (:import [main.tuma.transition Transition]))

(defrecord Turingmachine [states input-symbols tape-symbols transitions initial-state blank-symbol accepted-states])

(defn- valid-word? [word input-symbols]
  (assert (spec/valid? string? word))
  (doseq [s input-symbols] (assert (spec/valid? string? s)))
  (every? #(some #{%} input-symbols) word))

(defn- valid-tm? [tm]
  (assert (= Turingmachine (type tm)))
  (every? #(some #{%} (trans/get-reached-states (:transitions tm))) (:states tm)))

(defn- inside-word? [word position]
  (assert (spec/valid? string? word))
  (assert (= Long (type position)))
  (< -1 position (count word)))

(defn- find-transition [source read-symbol transitions]
  (assert (spec/valid? keyword? source))
  (assert Character (type read-symbol))
  (doseq [t transitions] (assert Transition (type t)))
  (first (filter #(and (= (:source %) source) (= (:reads %) read-symbol)) transitions)))

(defn- write-tape [tape head-position symbol]
  (str (subs tape 0 head-position) symbol (subs tape (+ head-position 1))))

(defn- execute-transitions [tape transitions head-position current-state blank-symbol]
  (let [read-symbol (if (inside-word? tape head-position) (str (nth tape head-position)) blank-symbol)
        transition (find-transition current-state read-symbol transitions)]
    (cond
      (nil? transition)
      {:current-state current-state :tape tape}

      (= head-position (count tape))
      (recur (str tape blank-symbol) transitions head-position current-state blank-symbol)

      (= -1 head-position)
      (recur (str blank-symbol tape) transitions 0 current-state blank-symbol)

      :else
      (let [newTape (write-tape tape head-position (:writes transition))
            newHeadPosition (if (= (:direction transition) :right)
                                (+ head-position 1)
                                (- head-position 1))]
        (recur newTape transitions newHeadPosition (:destination transition) blank-symbol)))))

(defn emulate-tm [tm word]
  (assert (valid-tm? tm) "defined states not matching reached states in transitions!")
  (assert (spec/valid? string? word) "input word must be a string!")
  (cond
    (true? (valid-word? word (:input-symbols tm)))
    {:input word :output :word :endState (:initial-state tm) :verdict :rejected}

    :else
    (let [result (execute-transitions word (:transitions tm) 0 (:initial-state tm) (:blank-symbol tm))]
      (if (contains? (set (:accepted-states tm)) (:current-state result))
        {:input word :output (:tape result) :endState (:current-state result) :verdict :accepted}
        {:input word :output (:tape result) :endState (:current-state result) :verdict :rejected}))))  
