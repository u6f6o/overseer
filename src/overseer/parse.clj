(ns overseer.parse
  (:require [clojure.string :as str]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn file->lines [file]
  (with-open [in-file (io/reader file)]
    (doall
      (csv/read-csv in-file :separator \;))))

(defn filter-empty [lines]
  (remove (partial every? str/blank?) lines))

(defn extract-header [line]
  (map (comp keyword #(str/replace % "_" "-") str/lower-case) line))

(defn extract-content [header line]
  (zipmap header line))

(defn lines->sheet [coll]
  (let [header (extract-header (first coll))]
    (map (partial extract-content header) (rest coll))))

(defn file->sheet [csv-file]
  (let [lines (file->lines csv-file)]
    (->> lines
         filter-empty
         lines->sheet)))
