(ns tests.test-tanimoto
  (:use 
    clojure.contrib.test-is
    molecule-cut.tanimoto))
		
(deftest test-tanimoto
  (is (= 1 (tanimoto #{1} #{1})))
  (is (= 0 (tanimoto #{1} #{2})))
  (is (= (/ 1 2) (tanimoto #{1} #{1 2})))
  (is (= (/ 2 4) (tanimoto #{1 2 3} #{2 3 4}))))
	
(deftest test-tanimoto-rank
  (is (= 
    '({:fingerprint #{1 2}} {:fingerprint #{1 2 3}}) 
    (tanimoto-rank 
      [{:fingerprint #{1 2 3}} {:fingerprint #{1 2}}]
      {:fingerprint #{1 2}}))))