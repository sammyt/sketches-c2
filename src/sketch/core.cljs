(ns sketch.core
  (:require
    [c2.core :refer [unify]]
    [c2.scale :as scale]
    [c2.ticks :as ticks])
  (:require-macros 
    [c2.util :refer [bind! pp]]))

;; --------------------- 
;; Example 1
;; a simple list
;;

(bind! ".ex1"  
  [:div
   [:ol
     [:li "wibble"]
     [:li "wobble"]
     [:li "woooo"]]])

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
  (let [data [4, 8, 15, 16, 23, 42]
        width 200 height 100
        s (scale/linear :domain [0 (apply max data)] 
                        :range  [0 width])
        h 20]
    [:div [:div.chart
     [:svg {:width width, :height height}
      
      (map-indexed (fn [i d]
        [:rect {:width (s d) :height h, :y (* i h)}]) data)]]]))

;; --------------------- 
;; Example 5
;; SVG chart++
;;
(bind! ".ex5"
(let [data [4, 8, 15, 16, 23, 42] 
      width 440 height 140
      domain [0 (apply max data)]
      s (scale/linear :domain domain 
                      :range  [0 (- width 20)])
      h 20
      ticks (:ticks (ticks/search domain))]

  [:div [:div
   [:svg.chart {:width width, :height height}
    [:g {:transform "translate(10,15)"}

    (map-indexed (fn [i d]
      [:rect {:width (s d) :height h, :y (* i h)}]) data)

    (map-indexed (fn [i d]
      [:text {:x (s d) 
              :y (+ (/ h 2) (* i h))
              :text-anchor "end"
              :dx -3
              :dy ".35em"} d]) data)
   

    (map (fn [t] 
           (let [x (s t)] 
             [:line {:x1 x :x2 x 
                     :y1 0 :y2 120 
                     :stroke "#ccc"}])) 
         ticks)
    
    (map (fn [t] 
           (let[x (s t)] 
             [:text.rule {:x x :y 0 :dy -3 :text-anchor "middle"} t])) 
         ticks)]]]]))


