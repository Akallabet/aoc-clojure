(ns aoc22.day03
  (:require #?(:clj [clojure.java.io :as io]
               :cljs [nbb.core :refer [slurp await]])
            [clojure.string :as str]
            [clojure.set]
            #?(:cljs [promesa.core :as p])))

(def letters "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")

(defn split-half [vector]
  [(take (quot (count vector) 2) vector)
   (drop (quot (count vector) 2) vector)])

(defn find-common-letters [list] (apply clojure.set/intersection (map set list)))

(defn group-by-same-letters [rucksacks]
  (let [common-letters (find-common-letters rucksacks)
        grouped (map #(group-by identity %) rucksacks)
        merged (merge-with into (first grouped) (second grouped) (get grouped 2))
        indexes (filter #(contains? common-letters (first %)) merged)]
    indexes))

(defn split-1 [rucksacks]
  (->> (map split-half rucksacks)
       (map #(map set %))
       (map find-common-letters)
       (reduce concat)))

(defn split-2 [rucksacks]
  (->> (partition 3 rucksacks)
       (map group-by-same-letters)
       (map first)
       (map first)))

(defn reorganisation [rucksacks split-fn]
  (->> (str/split-lines rucksacks)
       (split-fn)
       (map #(+ (str/index-of letters %) 1))
       (apply +)))

(def input "vJrwpWtwJgWrhcsFMMfFFhFp\njqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL\nPmmdzqPrVvPwwTWBwg\nwMqvLMZHhHMvwLHjbvcjnnSBnvTQFn\nttgJtRGJQctTZtZT\nCrZsJsPPZsGzwwsLwLmpwMDw")

(comment
  (reorganisation input split-1)
  (reorganisation (slurp (io/resource "aoc22/day03.txt")) split-1)
  (reorganisation input split-2)
  (reorganisation (slurp (io/resource "aoc22/day03.txt")) split-2))