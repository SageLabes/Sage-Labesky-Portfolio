# Distinction_Project_Labesky
This project will produce an embedded device with the capability of serving as a way to recieve immediate, on the fly audio descriptions of the visual environment around the user. It will be able to take photographs on random intervals or when specified by the user. The images and text descriptions will be stored in a database on another device and can be viewed by the user. This is intended to serve the purpose of both helping a user understand the environment around them and creating an interesting visual journal that takes minimal effort but can provide interesting points of reflection.
A video demo of the project can be viewed here: https://www.youtube.com/watch?v=hTL25EAKz5Q

## Project overall 
- Small device with a camera 
- Takes photographs of the surroundings at a regular interval or at the push of a button 
- Creates a text and audio description of the image 
- Displays the text and audio descriptions of the images taken as a sort of visual journal 
- Reads the audio description back to the user upon initially taking the picture 

## Technical Description 
- Uses Raspberry Pi Zero W2 as main base https://www.digikey.com/en/product-highlight/r/raspberry-pi/raspberry-pi-zero-2-w 
- Uses 120 degree focal angle spy camera from AdaFruit for Pi Zero https://www.adafruit.com/product/5389#tutorials 
- Uses PiCamZero library 
- Uses simple breadboard, breadboard wires, and button https://www.dfrobot.com/product-612.html?srsltid=AfmBOophQH9a1cujUlpLu03gy4gGwWfXAYPXA6-2G1XCidsZMNfgALdxC2Q 
- Uses RPI.GPIO to handle button presses 
- Code written in Python 
- HTML template used for web server layout 
- Uses GPT 4o mini model to generate image descriptions and text to speech 
- Uses MPG123 to play audio over bluetooth on Raspberry Pi 
- Uses Flask to host webserver 
- Time interval is supplied as command line argument
  
## Problems faced 
- Choosing hardware: Originally wanted to use Arduino board but generating audio and sending it to and Arduino chip is not possible, had to switch to pi Zero, Proceeded to order the wrong peripherals including micro hdmi instead of mini hdmi, trial and error fixed this issue, in the future coming to a full understanding of what is necessary before ordering might be wise. 
- Attempting to code on laptop and send to raspberry pi, wanted to use email but pi isn’t powerful enough to use internet, had to learn FileZilla to use FTP and transfer files directly 
- Trying to come up with a naming system for files (since three have to have the same name) that works even after multiple boots of the program to categorize photos, descriptions, and audio. Used time.time() to get the current time which is always changing 
- Needed to find a way to display photos/text/audio on external device for viewing. Was recommended AWS Buckets by a recruiter. The service immediately went down so I searched for another way. Found out about flask and can run the webserver locally 


## Overall functionality Requirements:
1. User will be able to turn the device on and off easily
2. Device will capture photographs on a set interval
3. Device will capture photographs on user input (button press)
4. Device will send captured photographs to an exterior device functioning as a brain
5. Device will recieve audio descriptions of the photographs
6. Device will output audio descriptions to the user
7. AI analysis should occur on an external device (brain)
8. Brain should store the photographs along with the descriptions
9. Brain should generate audio descriptions which it will send to the embedded device

## Required Physical Components:
1. Arduino prgorammer (uploads code to campers since ESP32 has no usb connectors
   - https://www.digikey.com/en/products/detail/sparkfun-electronics/15096/9817166?gclsrc=aw.ds&gad_source=1&gad_campaignid=20243136172&gbraid=0AAAAADrbLlghsDJIWygJ3vghWcIjA8Iiv&gclid=CjwKCAjw7_DEBhAeEiwAWKiCC8_Fwc5NlhB9kDuRv1M0VokEMeY2BACN866RezG0WgP7wLT0a8vI0hoCBC8QAvD_BwE
3. Small camera (should be connectable to breadboard and compatible with Arduino chip)
   - https://www.digikey.com/en/products/detail/universal-solder-electronics-ltd/ESP32-CAM%2520WIFI%2520BT%2520BLE/14319859
4. Audio ouptut device (should be connectable to breadboard and compatible with arduino chip)
   - Might end up just being played on the device or regular headphones rather than arduino audio (better quality audio, easier to implement real time audio output)
5. Button (should be connectable to breadboard and compatible with arduino chip)
   - https://www.digikey.com/en/products/detail/same-sky-formerly-cui-devices-/TS02-66-60-BK-100-LCR-D/15634327?gclsrc=aw.ds&gad_source=1&gad_campaignid=20243136172&gbraid=0AAAAADrbLlghsDJIWygJ3vghWcIjA8Iiv&gclid=CjwKCAjw7_DEBhAeEiwAWKiCC_DOnEW-3dy0wmrXaEOxe-G7r8vEZVz-m55cWJXIYPEPzcXNQ-  bZkBoChWsQAvD_BwE
7. Wires (should be connectable to breadboard)
   - https://www.amazon.com/California-JOS-Breadboard-Optional-Multicolored/dp/B0BRTHR2RL/ref=pd_lpo_d_sccl_2/133-7567229-1303851?pd_rd_w=72LQl&content-id=amzn1.sym.4c8c52db-06f8-4e42-8e56-912796f2ea6c&pf_rd_p=4c8c52db-06f8-4e42-8e56-912796f2ea6c&pf_rd_r=PZVVFN4XG1KSBJYMNNW4&pd_rd_wg=nYXit&pd_rd_r=d95502be-d935-4f9c-85e9-61638aa3d545&pd_rd_i=B0BRTHR2RL&th=1
9. Breadboard
    - https://www.digikey.com/en/products/detail/universal-solder-electronics-ltd/26058/16819785?gclsrc=aw.ds&gad_source=4&gad_campaignid=20232005509&gbraid=0AAAAADrbLljdSOYlhjxqqn0XzTN55z8qx&gclid=CjwKCAjw7_DEBhAeEiwAWKiCC4gdDU9Xznk2BkFAjjeIc9Da9L0qsVdd5jKPt5G4CrxLmlQv0vDXlhoC-lgQAvD_BwE
