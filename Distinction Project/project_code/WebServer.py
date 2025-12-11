from flask import Flask, render_template, send_from_directory
import os
"""
WebServer.py

Hosts a webserver via Flask to display images, audio, and text descriptions for photographs taken by a Raspberry Pi camera.

Author: Sage Labesky
Created: 10/24/2025
Modified: 10/26/2025
"""


app = Flask(__name__)
# Directory containing media files
MEDIA_DIR = os.path.join(os.path.dirname(__file__), 'PhotosDescriptions')

@app.route('/')
def index():
    items = []
    for filename in sorted(os.listdir(MEDIA_DIR)):
        if filename.lower().endswith(('.jpg', '.jpeg', '.png')):
            base = os.path.splitext(filename)[0]
            img_file = filename
            mp3_file = f"{base}.mp3"
            txt_file = f"{base}.txt"
            if os.path.exists(os.path.join(MEDIA_DIR, mp3_file)) and os.path.exists(os.path.join(MEDIA_DIR, txt_file)): # Ensure corresponding mp3 and txt exists
                with open(os.path.join(MEDIA_DIR, txt_file), 'r') as f: # txt file is small, so it can be sent to html directly
                    text = f.read()
                items.append((img_file, mp3_file, text))
    return render_template('index.html', items=items) # uses index.html template to display content

@app.route('/media/<path:filename>')
def media(filename):
    return send_from_directory(MEDIA_DIR, filename)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)