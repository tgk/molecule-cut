(ns molecule-cut.fingerprints
  (:use [clojure.contrib.io :only (read-lines)]))

;; TODO: Read up on clojures STM. Is there something with 
;; conditions?
(defn seen-before-generator []
  "Generates a map that can be used for getting
  ids for items. ids are integers.
  When a seen-before function is called it will
  return the id assigned to the given item.
  Ids are assigned as items are seen."
  (let [ids (ref (iterate inc 0))
        seen-items (ref {})]
    (fn [item]
      (dosync 
        (if (not (@seen-items item))
          (do
            (alter seen-items assoc item (first @ids))
            (alter ids rest))))
      (@seen-items item))))

(defn fingerprints [coll]
  "Generates fingerprints for a collection of collections."
  (let [seen-before (seen-before-generator)]
    (map #(map seen-before %) coll)))

(defn parse-fingerprint [s]
  (set (map #(Integer/parseInt %) (.split s ","))))
	
(defn parse-fingerprint-line [line]
  (let [[name fingerprint-string] (.split line " ")
        fingerprint (parse-fingerprint fingerprint-string)]
    {:name name :fingerprint fingerprint}))

(defn parse-fingerprint-file [filename]
  (map parse-fingerprint-line (read-lines filename)))