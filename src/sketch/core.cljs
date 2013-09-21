(ns sketch.core
  (:require
    [c2.core :refer [unify]]
    [c2.scale :as scale])
  (:require-macros 
    [c2.util :refer [bind! p]]))

;; --------------------- 
;; Example 1
;; a simple list
;;

(bind! ".ex1"  
  [:div
    [:ol (map (fn [d] [:li d]) ["a" "b" "c"])]])

;; --------------------- 
;; Example 2
;; nested data in a table
;;
(def grid-of-nums (map #(range 10) (range 5)))

(defn make-nodes [vals]
  (map (fn [v] [:td v]) vals))

(bind! ".ex2"
  [:div [:table
    (map (fn [vals] 
      [:tr (make-nodes vals)]) grid-of-nums)]])

;; --------------------- 
;; Example 3
;; ins and outs 
;;
(defn ex3 [data]
  (bind! ".ex3"
    [:div
      [:p (map (fn [d] [:span (str "(" d ")")]) data)]]))

(ex3 [1 2 3])
(js/setInterval
  #(ex3 (range (rand 15))) 8000)

;; --------------------- 
;; Example 4
;; SVG chart
;;
(bind! ".ex4"
  (let [data (map-indexed (fn [i d] [i d]) {:a 3, :b 5, :c 6, :d 9})
        width 200 height 100
        s (scale/linear :domain [0 9] :range [0 width])
        h 20]
  [:div [:div.chart
   [:svg {:width width, :height height}
    (map (fn [[i [key d]]]
      [:rect {:width (s d) :height h, :y (* i h)}]) data)
    (map (fn [[i [key d]]]
      [:text {:x (s d) 
              :y (+ (/ h 2) (* i h))
              :text-anchor "end"
              :dx -3
              :dy "0.35em"} d]) data)]]]))

;; --------------------- 
;; Example 5
;; SVG chart++
;;
(bind! ".ex5"
  (let [data (map-indexed (fn [i d] [i d]) {:a 3, :b 5, :c 6, :d 9})
        width 200 height 100
        s (scale/linear :domain [0 9] :range [0 width])
        h 20]
  [:div [:div.chart
   [:svg {:width width, :height height}
    (map (fn [[i [key d]]]
      [:rect {:width (s d) :height h, :y (* i h)}]) data)
    (map (fn [[i [key d]]]
      [:text {:x (s d) 
              :y (+ (/ h 2) (* i h))
              :text-anchor "end"
              :dx -3
              :dy "0.35em"} d]) data)]]]))


