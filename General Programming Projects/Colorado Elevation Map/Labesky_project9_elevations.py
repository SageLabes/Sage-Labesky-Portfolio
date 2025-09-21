"""
    This program displays an elevation map of Colorado and allows the user to select point to see specific elevations
    Filename: Labesky_project9_elevations.py
    Author: Sage Labesky
    Date: 11/17/2022
    Course: COMP 1351
    Assignment: project 9
    Collaborators: None
    Internet Source: None
"""

import dudraw
#Sets up the canvas size and scale to match the dataset
dudraw.set_canvas_size(760,560)
dudraw.set_x_scale(0,760)
dudraw.set_y_scale(0,560)

elevation_list = [] #List to store the elevations
xpos = 0 #x position of each drawn rectangle
ypos = 560 #y position of each drawn rectangle
max_elevation = 0 #Maximum elevation
running = True #variable to run the loop

def find_max(list: list)->int:
    """
        This function finds the highest value in a list
        parameters: A list
        return: the maximum value as an int
    """
    max = 0
    for y in range(len(list)):
        for z in range(len(list[y])): #Traverses each data point in the list
            if int(list[y][z]) > max: #if the point is higher than the current max it becomes the current max
                max = int(list[y][z])
    return max

with open("CO_elevations_feet.txt", "r") as elevations: #This opens the file and adds each line to the list as more lists
    for x in elevations:
        elevation_list.append((x.strip("\n ")).split(" "))


max_elevation = find_max(elevation_list) #max elevation is retrieved and stored for use
print("Max elevation is: " + str(max_elevation))

for y in range(len(elevation_list)):
    for z in range(len(elevation_list[y])): #Traverses every data point in the list
        rgb = int(elevation_list[y][z])
        dudraw.set_pen_color_rgb(int(rgb/max_elevation*255),int(rgb/max_elevation*255),int(rgb/max_elevation*255)) #The pen color is set based on elevation
        dudraw.filled_rectangle(xpos, ypos, 0.1, 0.1) #the rectangles are drawn
        xpos += 1
    ypos -= 1
    xpos = 0

while running: #loop that allows the user to select specific spots on the map
    if dudraw.mouse_is_pressed(): #activates when the mouse is pressed
        y = 0
        if int(dudraw.mouse_y()) > 280: #flips numbers over 280 over the line 280
            y = int(dudraw.mouse_y()) - 280
            y = 280 - y
        else: #flips numbers under 280
            y = 560 - int(dudraw.mouse_y())
        print("The elevation there is: " + str(elevation_list[y][int(dudraw.mouse_x())])) #prints the elevation at the point clicked
    dudraw.show(100)