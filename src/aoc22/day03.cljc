(ns aoc22.day03
  (:require #?(:clj [clojure.java.io :as io]
               :cljs [nbb.core :refer [slurp await]])
            [clojure.string :as str]
            [clojure.set :as sets]
            #?(:cljs [promesa.core :as p])))

(def letters "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

(defn half [vector] (quot (count vector) 2))

(defn split-half [vector]
  [(take (half vector) vector)
   (drop (half vector) vector)])

(defn common-letters [[v1 v2]] (sets/intersection v1 v2))

(defn shared-items
  [rucksacks]
  (->> (str/split rucksacks #"\n")
       (map #(split-half %))
       (map #(map set %))
       (map vec)
       (map common-letters)
       (reduce concat)
       (map #(+ (str/index-of letters %) 1))
       (apply +)))

(def input "vJrwpWtwJgWrhcsFMMfFFhFp\njqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL\nPmmdzqPrVvPwwTWBwg\nwMqvLMZHhHMvwLHjbvcjnnSBnvTQFn\nttgJtRGJQctTZtZT\nCrZsJsPPZsGzwwsLwLmpwMDw")

(comment
  (shared-items input)
  (shared-items (slurp (io/resource "aoc22/day03.txt"))))