(ns aoc22.day09
  (:require #?(:clj [clojure.java.io :as io]
               :cljs [nbb.core :refer [slurp await]])
            [clojure.string :as str]
            [clojure.edn :as edn]
            #?(:cljs [promesa.core :as p])))

(defn dist-line [x1 x2]
  (Math/abs (- x2 x1)))
(defn dist-rows [[_ y1] [_ y2]] (dist-line y1 y2))
(defn dist-cols [[x1] [x2]] (dist-line x1 x2))
(defn adj? [h t] (and (= 1 (dist-rows h t)) (= 1 (dist-cols h t))))

(defn same-row? [[_ y1] [_ y2]] (= y1 y2))
(defn same-col? [[x1] [x2]] (= x1 x2))

(defn adj-rows? [h t] (and (same-col? h t) (= (dist-rows h t) 1)))
(defn adj-cols? [h t] (and (same-row? h t) (= (dist-cols h t) 1)))

(defn incr [n] (+ n 1))
(defn decr [n] (- n 1))

(defn op [h t] (if (< h t) (decr t) (incr t)))

(defn move [h t]
  (cond
    (same-row? h t) [(op (h 0) (t 0)) (t 1)]
    (same-col? h t) [(t 0) (op (h 1) (t 1))]
    (and (= 1 (dist-cols h t)) (= 2 (dist-rows h t))) [(op (h 0) (t 0)) (op (h 1) (t 1))]))

(defn parse-input [lines]
  (->> (str/split-lines lines)
       (mapv #(str/split % #" "))
       (mapv (fn [[dir steps]] [dir (edn/read-string steps)]))))

(defn gen-single-head-path
  ([pos instruction] (gen-single-head-path pos instruction [pos]))
  ([pos [dir steps] moves]
   (let [[x y] (last moves)]
     (cond
       (= 0 steps) (drop 1 moves)
       (= "R" dir) (recur pos [dir (decr steps)] (conj moves [(incr x) y]))
       (= "L" dir) (recur pos [dir (decr steps)] (conj moves [(decr x) y]))
       (= "U" dir) (recur pos [dir (decr steps)] (conj moves [x (incr y)]))
       (= "D" dir) (recur pos [dir (decr steps)] (conj moves [x (decr y)]))))))

(defn gen-head-path
  ([instructions] (gen-head-path instructions [[0 0]]))
  ([instructions path]
   (let [head (last path)]
     (cond
       (= 0 (count instructions)) (drop 1 path)
       :else (recur
              (drop 1 instructions)
              (concat path (gen-single-head-path head (first instructions))))))))

(def input "R 4\nU 4\nL 3\nD 1\nR 4\nD 1\nL 5\nR 2")

(comment
  (parse-input input)
  (gen-single-head-path [0 0] ["R" 4])
  (gen-single-head-path [0 0] ["L" 2])
  (gen-head-path [["R" 4] ["L" 2]])
  (gen-head-path (parse-input input))
  (= 3 (dist-line 1 -2))
  (= 2 (dist-rows [1 2] [-2 4]))
  (= 3 (dist-cols [1 2] [-2 4]))
  (= true (same-row? [1 3] [0 3]))
  (= false (same-row? [1 2] [-3 4]))
  (= true (same-col? [-3 1] [-3 5]))
  (= true (adj-rows? [3 1] [3 2]))
  (= false (adj-rows? [3 1] [1 1]))
  (= true (adj-cols? [2 4] [1 4]))
  (= true (adj-cols? [3 4] [1 4]))
  (= -2 (op -1 -3))
  (= -4 (op -5 -3))
  (= -2 (op 1 -3))
  (= 2 (op 1 3))
  (= 4 (op 5 3))
  (= 0 (op -1 1))
  (= -4 (op -1 -5))
  (= [2 2] (move [3 2] [1 2]))
  (= [0 2] (move [-1 2] [1 2]))
  (= [1 2] (move [1 3] [1 1]))
  (= [1 -2] (move [1 -3] [1 -1]))
  (= [2 1] (move [2 2] [1 0])) ;; diagonal up right
  (= [2 1] (move [2 2] [3 0])) ;; diagonal up left
  (= [2 3] (move [2 2] [3 4])) ;; diagonal down left
  (= [2 -1] (move [2 -2] [1 0])) ;; diagonal down right
  (adj? [2 1] [1 0])
  (adj? [0 -1] [1 0])
  ;; (= [0 0] (move [1 1] [0 0]))
  ;; (= [-2 -1] (move [-3 -2] [-2 -1]))
  )
