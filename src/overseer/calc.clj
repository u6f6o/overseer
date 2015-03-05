(ns overseer.calc
  (:use [overseer.parse :only [parse-lines]])
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn sum-up
  ([sheet desc-filter sum-column]
    (->> sheet
         (filter #(desc-filter (:DESC %)))
         (map sum-column)
         (remove str/blank?)
         (map read-string)
         (apply +))))







