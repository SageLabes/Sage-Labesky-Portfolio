import weaviate
import weaviate.classes as wvc
import os
import json
"""
Authors: Sage Labesky and Christy Vo 
"""


client = weaviate.connect_to_local(
        headers={
            # use your OPENAI_API_KEY here
            "X-OpenAI-Api-Key": os.getenv("OPENAI_API_KEY") 
        }) 




if client.collections.exists('courses'):
    client.collections.delete('courses')

articleCollection = client.collections.create(name="mathInfo",properties=[wvc.config.Property(name="course_number",data_type=wvc.config.DataType.TEXT,),wvc.config.Property(name="professor",data_type=wvc.config.DataType.TEXT,),wvc.config.Property(name="subject",data_type=wvc.config.DataType.TEXT,),wvc.config.Property(name="difficulty",data_type=wvc.config.DataType.TEXT,)])


with open("project3/info.json") as f:
    data = json.load(f)

articleCollection.data.insert_many(data)

print("Data inserted successfully!")