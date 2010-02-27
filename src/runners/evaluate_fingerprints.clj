(ns runners.evaluate-fingerprints
  (:use 
    (molecule-cut
      sdf
      molecule-utils
      fingerprints
      tanimoto)))
			
(defn print-usage []
  (print "
Program for evaluating a fingerprint encoding.
Usage is:
  -first : ligand sdf filename
  -second: decoy sdf filename
  -third : fingerprint file

"))

(defn evaluate-fingerprint [db active? query]
  "Evaluates a single query fingerprint onto the db.
  db items contains a :fingerprint and a :name field.
  active? takes a fingerprint and answers if it is 
  a known active binder."
  (let [pos-neg #(if (active? %) "+" "-")]
    (println
      (:name query) 
      (apply str (map pos-neg (tanimoto-rank db query))))))

(if (= 3 (count *command-line-args*))
  (let [[ligand-filename decoy-filename fingerprints-filename] 
          *command-line-args*
        ligand-names
          (set (map molecule-name (read-sdf-file ligand-filename)))
        ; decoys (read-sdf-file decoy-filename)
        active? #(contains? ligand-names (:name %))
        db (parse-fingerprint-file fingerprints-filename)
        ligand-fingerprints (filter active? db)]
    (dorun 
      (map 
        (partial evaluate-fingerprint db active?)
        ligand-fingerprints)))
  (print-usage))