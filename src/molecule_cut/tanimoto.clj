(ns molecule-cut.tanimoto
  (:use clojure.set))

(defn tanimoto [a, b]
  (/
    (count (intersection a b))
    (count (union a b))))

(defn tanimoto-rank [fingerprints query]
  (reverse 
    (sort-by 
      #(tanimoto (:fingerprint query) (:fingerprint %))
      fingerprints)))