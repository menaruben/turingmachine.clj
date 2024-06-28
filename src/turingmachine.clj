(ns turingmachine
  (:require [clojure.spec.alpha :as spec]))

(defrecord Transition [source reads destination writes direction])
(defrecord Turingmachine [states input-symbols tape-symbols transitions initial-state blank-symbol accepted-states])

(defn valid-word? [word input-symbols]
  (every? #(some #{%} input-symbols) word))

(defn get-reached-states [transitions]
  (set (concat (map :source transitions) (map :destination transitions))))

(defn valid-utm? [utm]
  (assert (= Turingmachine (type utm)))
  (every? #(some #{%} (get-reached-states (:transitions utm))) (:states utm)))

(defn find-transition [source read-symbol transitions]
  (first (filter #(and (= (:source %) source) (= (:reads %) read-symbol)) transitions)))

(defn print-transition [transition]
  (println (str
            "source: " (:source transition)
            ", reads: " (:reads transition)
            ", destination: " (:destination transition)
            ", writes: " (:writes transition)
            ", direction: " (:direction transition))))

(defn write-tape [tape head-position symbol]
  (str (subs tape 0 head-position) symbol (subs tape (+ head-position 1))))

(defn execute-transitions [tape transitions head-position current-state blank-symbol]
  (let [read-symbol (if (< -1 head-position (count tape)) (str (nth tape head-position)) blank-symbol)
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

(defn emulate-utm [utm word]
  (assert (valid-utm? utm) "defined states not matching reached states in transitions!")
  (assert (spec/valid? string? word) "input word must be a string!")
  (cond
    (true? (valid-word? word (:input-symbols utm)))
    {:input word :output :word :endState (:initial-state utm) :verdict :rejected}

    :else
    (let [result (execute-transitions word (:transitions utm) 0 (:initial-state utm) (:blank-symbol utm))]
      (if (contains? (set (:accepted-states utm)) (:current-state result))
        {:input word :output (:tape result) :endState (:current-state result) :verdict :accepted}
        {:input word :output (:tape result) :endState (:current-state result) :verdict :rejected}))))
