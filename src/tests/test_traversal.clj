(ns tests.test-traversal
	(:use 
		molecule-cut.traversal
		clojure.contrib.test-is))
		
(deftest test-traversal-without-visit-once
	(is (= 
		[:a :b] 
		(bfs-traversal (initial-queue :a) {:a [:b]})))
	(is (= 
		[:a :b :c] 
		(bfs-traversal 
			(initial-queue :a)
			{:a [:b] :b [:c]})))
	(is (= 
		[:a :b :c :c] 
		(bfs-traversal 
			(initial-queue :a)
			{:a [:b :c] :b [:c]})))
	(is (= 
		[:a :b :b] 
		(bfs-traversal 
			(initial-queue :a)
			{:a [:b :b]})))
	(is (= 
		[:a :b :a :b :a] 
		(take 5
			(bfs-traversal 
				(initial-queue :a)
				{:a [:b] :b [:a]})))))
				
(deftest test-create-seen?
	(let [seen? (create-seen?)]
		(is (not (seen? :a)))
		(is (seen? :a))
		(is (not (seen? :b)))
		(is (seen? :b))
		(is (seen? :a)))
	(let [seen? (create-seen?)]
		(is (not (seen? :a)))
		(is (seen? :a))
		(is (not (seen? :b)))
		(is (seen? :b))
		(is (seen? :a))))
		
(deftest test-traversal-with-visit-once
	(is (= 
		[:a :b] 
		(bfs-traversal 
			(initial-queue :a) 
			(visit-children-once-wrapper {:a [:b]} :a))))
	(is (= 
		[:a :b :c] 
		(bfs-traversal 
			(initial-queue :a)
			(visit-children-once-wrapper {:a [:b] :b [:c]} :a))))
	(is (= 
		[:a :b :c] 
		(bfs-traversal 
			(initial-queue :a)
			(visit-children-once-wrapper {:a [:b :c] :b [:c]} :a))))
	(is (= 
		[:a :b] 
		(bfs-traversal 
			(initial-queue :a)
			(visit-children-once-wrapper {:a [:b :b]} :a))))
	(is (= 
		[:a :b] 
		(bfs-traversal 
			(initial-queue :a)
			(visit-children-once-wrapper {:a [:b] :b [:a]} :a)))))