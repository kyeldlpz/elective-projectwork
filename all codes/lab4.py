import cv2 
import numpy as np 
import matplotlib.pyplot as plt 
from matplotlib.widgets import Slider, RadioButtons 
# --- Load and resize the image --- 
img = cv2.imread("forlab3.jpg")  # Use your image file 

# Resize image for display only (preserve aspect ratio) 
display_width = 600 
scale = display_width / img.shape[1] 
img = cv2.resize(img, (display_width, int(img.shape[0] * scale))) 
img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)  # Convert to RGB for matplotlib 
# Mapping for operations and element shapes 
operation_map = { 
"Erosion": cv2.erode, 
"Dilation": cv2.dilate 
} 
element_map = { 
"Rectangle": cv2.MORPH_RECT, 
"Cross": cv2.MORPH_CROSS, 
"Ellipse": cv2.MORPH_ELLIPSE 
} 

# --- Matplotlib Setup --- 
plt.rcParams['figure.figsize'] = [7, 5]  # Resize display window 
plt.rcParams['axes.titlesize'] = 14 
fig, ax = plt.subplots() 
plt.subplots_adjust(left=0.3, bottom=0.3) 
# Show initial image 
disp = ax.imshow(img) 
ax.set_title("Morphological Result") 
ax.axis("off") 
 
#UI Controls
 
# Slider for kernel size 
ax_slider = plt.axes([0.3, 0.15, 0.6, 0.03]) 
slider = Slider(ax_slider, "Kernel Size", 0, 20, valinit=1, valstep=1) 
 
# Operation radio buttons 
ax_op = plt.axes([0.05, 0.6, 0.2, 0.25]) 
radio_op = RadioButtons(ax_op, ["Erosion", "Dilation"]) 
 
# Element shape radio buttons 
ax_shape = plt.axes([0.05, 0.25, 0.2, 0.25]) 
radio_shape = RadioButtons(ax_shape, ["Rectangle", "Cross", "Ellipse"]) 
 
#Update function
def update(val=None): 
    op_func = operation_map[radio_op.value_selected] 
    element_type = element_map[radio_shape.value_selected] 
    ksize = int(slider.val) 
    kernel = cv2.getStructuringElement(element_type, (2 * ksize + 1, 2 * ksize + 1)) 
    result = op_func(img, kernel) 
    disp.set_data(result) 
    fig.canvas.draw_idle() 
 
# Link all widgets to update() 
slider.on_changed(update) 
radio_op.on_clicked(update) 
radio_shape.on_clicked(update) 
 
# Initial call to update image 
update() 
plt.show() 
