(ns tests.test-distance
  (:use 
    clojure.contrib.test-is
    (molecule-cut smiles distance)))

(deftest test-smiles-map
  (is (= 
    {"CCCC" 0, "CCC" 1, "CC" 2, "" 3}
    (smiles-map (molecule-from-smiles "CCCC")))))
		
(deftest test-cut-distance-fn
  (is (=
    1
    ((cut-distance-fn (molecule-from-smiles "CC1CC1")) 
      (molecule-from-smiles "CCCC"))))
  (is (=
    7
    ((cut-distance-fn (molecule-from-smiles "CN(C)C1CCC(O)C1")) 
      (molecule-from-smiles "CN(F)C1CCCCC1O")))))