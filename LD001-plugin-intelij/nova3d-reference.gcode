; 19.11.17 - X3msnake
; This file was reversed and tested with and for Elfin
; But should work fo other NOVA3D printers
; ---------- USE IT AT YOUR OWN RISK -----------------
; Tipical novamaker file outputs this .gcode file
; this file must be present and properly formatted
; This file controlls the printer motion and speed
; The fist 2 commented blocks controll information
; to display on LCD information only, all the lines
; can be completely deleted and the file will still 
; print but information about layer heigh and speed
; will be shown as 0 on the screen information
; The exception is the ;Number of Slices, the machine
; will fail to parse the file if this line is removed
; the machine apparently uses this to know how many 
; images the file has.

; ---------- USE IT AT YOUR OWN RISK -----------------
;****Build and Slicing Parameters****
;Pix per mm X            = 19.324 
;Pix per mm Y            = 19.324 
;X Resolution            = 1410 
;Y Resolution            = 2531 
;Layer Thickness         = 0.05000 mm 
;Layer Time              = 1000 ms 
;Render Outlines         = False
;Outline Width Inset     = 2
;Outline Width Outset    = 0
;Bottom Layers Time      = 5000 ms 
;Number of Bottom Layers = 3 
;Blanking Layer Time     = 0 ms 
;Build Direction         = Bottom_Up
;Lift Distance           = 5 mm 
;Slide/Tilt Value        = 0
;Anti Aliasing           = True
;Use Mainlift GCode Tab  = False
;Anti Aliasing Value     = 2 
;Z Lift Feed Rate        = 60.0 mm/s 
;Z Bottom Lift Feed Rate = 60.0 mm/s 
;Z Lift Retract Rate     = 200.0 mm/s 
;Flip X                  = True
;Flip Y                  = True
;Number of Slices        = 6

;****Machine Configuration ******
;Platform X Size         = 65.02mm 
;Platform Y Size         = 116mm 
;Platform Z Size         = 130mm 
;Max X Feedrate          = 200mm/s
;Max Y Feedrate          = 200mm/s
;Max Z Feedrate          = 200mm/s
;Machine Type            = UV_LCD 


; ---------- USE IT AT YOUR OWN RISK -----------------
G28
G21 ;Set units to be mm
G91 ;Relative Positioning
M17 ;Enable motors
;<Slice> Blank
M106 S0

;<Slice> 0
M106 S255
;<Delay> 5000
M106 S0
;<Slice> Blank
G1 Z5.000 F60
G1 Z-4 F200
;<Delay> 7500

;<Slice> 1
M106 S255
;<Delay> 5000
M106 S0
;<Slice> Blank
G1 Z5.000 F60
G1 Z-4 F200
;<Delay> 7500

;<Slice> 2
M106 S255
;<Delay> 5000
M106 S0
;<Slice> Blank
G1 Z5.000 F60
G1 Z-4 F200
;<Delay> 7500

;<Slice> 3
M106 S255
;<Delay> 1000
M106 S0
;<Slice> Blank
G1 Z5.000 F60
G1 Z-4 F200
;<Delay> 7500

;<Slice> 4
M106 S255
;<Delay> 1000
M106 S0
;<Slice> Blank
G1 Z5.000 F60
G1 Z-4 F200
;<Delay> 7500

;<Slice> 5
M106 S255
;<Delay> 1000
M106 S0
;<Slice> Blank
G1 Z5.000 F60
G1 Z-10 F200
;<Delay> 7500

M18 ;Disable Motors
M106 S0
G1 Z5
;<Completed>
