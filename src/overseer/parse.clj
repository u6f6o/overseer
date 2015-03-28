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

(defn merge-content [header line]
  (zipmap header line))

(defn transform [lines]
  (let [header (extract-header (first lines))]
    (map (partial merge-content header) (rest lines))))

(defn lines->sheet [lines]
  (->> lines
       filter-empty
       transform))

(defn file->sheet [csv-file]
  (->> csv-file
       file->lines
       lines->sheet))
