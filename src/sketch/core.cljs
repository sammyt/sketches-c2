(ns sketch.core
  (:require
    [c2.core]
    [c2.scale :as scale]
    [c2.ticks :as ticks]
    [goog.dom :as dom]
    [cljs.core.async :refer [>! <! chan timeout alts!]])
  (:require-macros 
    [c2.util :refer [bind! pp]]
    [cljs.core.async.macros :refer [go]]))

(bind! ".ex-basic"
  [:div
   [:ol
     [:li "wibble"]
     [:li "wobble"]
     [:li "woooo"]]])

;; --------------------- 
;; Example 4
;; SVG chart
;;
(bind! ".ex-bars"
  (let [data [4, 8, 15]
        width 200 
        height 60
        bar-height 20
        scale-x (scale/linear 
                  :domain [0 (apply max data)] 
                  :range  [0 width])]

    [:div [:div.chart
     [:svg {:width width, :height height}
     
      (map-indexed (fn [i d]
        [:rect {:width (scale-x d) 
                :height bar-height, 
                :y (* i bar-height)}]) data)]]]))

;; --------------------- 
;; Example 5
;; SVG chart++
;;
(bind! ".ex-full"
  (let [data [4, 8, 15, 16, 23, 42] 
        width 440 
        height 140
        bar-height 20
        domain [0 (apply max data)]
        scale-x (scale/linear 
                  :domain domain 
                  :range  [0 (- width 20)])
        ticks (:ticks (ticks/search domain))]

    [:div [:div
     [:svg.chart {:width width, :height height}
      [:g {:transform "translate(10,15)"}
      (pp ticks) 
       ;; grid line labels 
       (map (fn [tick] 
             [:text.rule {:x (scale-x tick) :dy -3 :text-anchor "middle"} tick]) ticks) 

       ;; grid lines
       (map (fn [tick] 
             (let [x (scale-x tick)] 
               [:line {:x1 x :x2 x :y2 120 :stroke "#ccc"}])) ticks)


       ;; bars
       (map-indexed (fn [i d]
        [:rect {:width (scale-x d) 
                :height bar-height, 
                :y (* i bar-height)}]) data)

       ;; bar labels
       (map-indexed (fn [i d]
        [:text {:x (scale-x d) 
                :y (+ (/ bar-height 2) (* i bar-height))
                :text-anchor "end"
                :dx -3
                :dy ".35em"} d]) data)]]]]))

;; --------------------- 
;; Example 5
;; animation example 
;;
(defn draw-chart! [data]
  (bind! ".ex-moving"
    [:div [:div.chart  
      (let [scale-x (scale/linear :domain [0 30] :range [0 100])]

        (map-indexed 
          (fn [i d] [:div.col 
                     {:style {:height (str (scale-x d) "px")
                              :left (str (* i 21) "px")}}]) data))]]))


(let [seed (range 30)]
  (draw-chart! seed)
  (go 
    (loop [data seed] 
      (<! (timeout 3000))
      (draw-chart! data)
        (recur (vec (cons (last data) (drop-last data)))))))




