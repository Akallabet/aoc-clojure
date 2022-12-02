(ns aoc22.day02
  (:require #?(:clj [clojure.java.io :as io]
               :cljs [nbb.core :refer [slurp await]])
            [clojure.string :as str]
            #?(:cljs [promesa.core :as p])))

(def score {:X 1
            :Y 2
            :Z 3})

;; A rock B paper C scissors
;; X rock Y paper Z scissors

(def match-rules
  {:A {:X [1 (:X score)]
       :Y [6 (:Y score)]
       :Z [0 (:Z score)]}
   :B {:X [0 (:X score)]
       :Y [3 (:Y score)]
       :Z [6 (:Z score)]}
   :C {:X [6 (:X score)]
       :Y [0 (:Y score)]
       :Z [3 (:Z score)]}})

;; X lose Y draw Z win

(def match-rules-2
  {:A {:X [0 (:Z score)]
       :Y [3 (:X score)]
       :Z [6 (:Y score)]}
   :B {:X [0 (:X score)]
       :Y [3 (:Y score)]
       :Z [6 (:Z score)]}
   :C {:X [0 (:Y score)]
       :Y [3 (:Z score)]
       :Z [6 (:X score)]}})

(defn rock-paper-scissors
  [scores rules]
  (->> (str/split scores #"\n")
       (map #(str/split % #" "))
       (map #(map keyword %))
       (map (fn [[a b]] (b (a rules))))
       (map (fn [[a b]] (+ a b)))
       (apply +)))

(comment
  (rock-paper-scissors "A Y\nB X\nC Z" match-rules)
  (rock-paper-scissors (slurp (io/resource "aoc22/day02.txt")) match-rules)
  (rock-paper-scissors "A Y\nB X\nC Z" match-rules-2)
  (rock-paper-scissors (slurp (io/resource "aoc22/day02.txt")) match-rules-2))