(ns overseer.core
  (:use [overseer.parse :only [file->sheet]]
        [overseer.util])
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def sheets
  {:acc-stmts
   (->> "account.csv"
        io/resource
        io/file
        file->sheet)
   :expenses
   (->> "distribution.csv"
        io/resource
        io/file
        file->sheet)
   :income
   ()})

(defn filter-matching [acc-stmts expense]
  (let [tokens (str/split (:tokens expense) #",")]
    (filter #(matches-ignore-case? (:desc %) tokens) acc-stmts)))

(defn sum-column [acc-stmts column]
  (->> (remove (comp str/blank? column) acc-stmts)
       (map (comp parse-number column))
       (apply +)))

(defn sum-in [acc-stmts]
  (sum-column acc-stmts :in))

(defn sum-out [acc-stmts]
  (sum-column acc-stmts :out))

(defn calc-out-vs-in [acc-stmts]
  (- (sum-out acc-stmts) (sum-in acc-stmts)))

(defn calc-share-1 [acc-stmts ratio]
  (* (calc-out-vs-in acc-stmts) ratio))

(defn calc-share-2 [acc-stmts ratio]
  (- (calc-out-vs-in acc-stmts) (calc-share-1 acc-stmts ratio)))

(defn summarize-expense [acc-stmts expense]
  (let [matching-stmts (filter-matching acc-stmts expense)
        ratio (parse-number (:ratio expense))]
    {:sum-in  (sum-in matching-stmts)
     :sum-out (sum-out matching-stmts)
     :ratio   (eval ratio)
     :share-1 (calc-share-1 matching-stmts ratio)
     :share-2 (calc-share-2 matching-stmts ratio)}))

(defn summarize-expenses [sheets]
  (let [acc-stmts (:acc-stmts sheets)
        expenses (:expenses sheets)]
    (map #(merge % (summarize-expense acc-stmts %)) expenses)))

(defn summarize-income [sheets acc-holder-1 acc-holder-2]
  (let [acc-stmts (:acc-stmts sheets)]
    {:total (sum-in acc-stmts)}))

(defn summarize [sheets acc-holder-1 acc-holder-2]
  (assoc sheets
         :expenses (summarize-expenses sheets)
         :income (summarize-income sheets)))

(summarize sheets)

