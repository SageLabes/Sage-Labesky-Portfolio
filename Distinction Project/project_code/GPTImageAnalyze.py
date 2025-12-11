from openai import OpenAI
import time
import mpg123
import subprocess
import io
from picamzero import Camera
import RPi.GPIO as GPIO
import os
import time
import sys      
"""
GPTImageAnalyze.py

Main logic for taking pictures with a Raspberry Pi camera, sending them to OpenAI's GPT-4o-mini model for analysis, generating text descriptions, converting those descriptions to speech, and saving all media files.

Author: Sage Labesky
Created: 9/12/2025
Modified: 10/26/2025
"""

MEDIA_DIR = "PhotosDescriptions/"

# api key goes here, must be removed before sharing code
client = OpenAI(api_key= )
def create_file(file_path):
  """
  uses Files API to upload image and get file ID
  """
  with open(file_path, "rb") as file_content:
    result = client.files.create(
        file=file_content,
        purpose="vision",
    )
    return result.id

def analyze_image(file_id):
    """
    uses GPT-4o-mini to analyze image and return text description
    """
    response = client.responses.create(
        model="gpt-4o-mini", # uses gpt-4o-mini as cost effective vision model
        input=[{
            "role": "user",
            "content": [
                {"type": "input_text", "text": "describe the image in three sentences. Focus on the foreground and the main focuses of the image for the first two sentences. For the third sentence, describe the background or location."},
                {
                    "type": "input_image",
                    "file_id": file_id,
                },
            ],
        }],
    )
    return response

def text_to_speech(text, time):
    """
    uses GPT-4o-mini-tts to convert text description to speech and save as mp3
    """
    speech = client.audio.speech.create(
        model="gpt-4o-mini-tts", # uses gpt-4o-mini-tts for text-to-speech
        voice="alloy",
        input=text
    )
    print(f"Text passing to tts: {text}") # prints text being converted to speech for debugging purposes

    with open(f"{MEDIA_DIR}{time}.mp3", "wb") as f:
        f.write(speech.read())
    subprocess.run(["mpg123", f"{MEDIA_DIR}{time}.mp3"])

def handle_process():
    """
    handles the full process of taking a photo, analyzing it, generating text, converting to speech, and saving files
    """
    current_time = time.time() # use timestamp as unique identifier for files
    cam.take_photo(f"{MEDIA_DIR}{current_time}.jpg")
    time.sleep(10) # program rests for 10 seconds to ensure photo is saved before proceeding

    file_id = create_file(f"{MEDIA_DIR}{current_time}.jpg")
    response = analyze_image(file_id)
    print(response.output_text)
    with open(f"{MEDIA_DIR}{current_time}.txt", "w") as f: # save text description to txt file
        f.write(response.output_text)
    text_to_speech(response.output_text, current_time)
    time.sleep(10) # program rests for 10 seconds to ensure all files are saved and the camera is ready for use before next operation

# set up python main function
if __name__ == "__main__":
    print("Main is running")
    GPIO.setwarnings(False) # gpio setup for button input
    GPIO.setmode(GPIO.BOARD)
    GPIO.setup(10, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
    if len(sys.argv) > 1:
        # get time between pictures from command line argument if provided
        time_between_pics = float(sys.argv[1]) * 60
        take_pics_at_interval = True
    else:
        # no command line argument, do not take pictures at interval
        take_pics_at_interval = False
        time_between_pics = 0
    print(f"Taking pics at Interval: {take_pics_at_interval}, Interval: {time_between_pics}")

    last_picture = time.time()
    highOrLow = 0

    cam = Camera()

    while True: # runtime loop
        if GPIO.input(10) == GPIO.HIGH: # handle button press to take picture
            print("Button Pressed")
            handle_process()
        if take_pics_at_interval: # handle taking pictures at set intervals
            if last_picture + time_between_pics < time.time():
                print("Taking picture at interval")
                handle_process()
                last_picture = time.time()
                
            
