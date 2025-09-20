from openai import OpenAI
import weaviate
import weaviate.classes as wvc
from weaviate.classes.query import Filter
import os
import json

"""
Authors: Sage Labesky and Christy Vo 
"""

clientW = weaviate.connect_to_local(
        headers={
            # use your OPENAI_API_KEY here
            "X-OpenAI-Api-Key": os.getenv("OPENAI_API_KEY") 
        }) 

mathInfo = clientW.collections.get("mathInfo")

chatbot_context = [
    {'role': 'system', 'content': """ You are a chatbot designed to help students pick a course in the math department at a university. You should respond with helpful information but you should be sassy.
     In each prompt, you will be provided with a question from a student delimited by '```' and a course that was chosen from a database delimited by '***' that is relevant to the question.
     please recommend that the student takes the course that was provided from the database. You will also keep an internal list of all courses previously discussed with the user.
     Step 1: You are to explain what the subject is and its applications
     Step 2: Output the course's information provided by the database and recommend the course to the student.
     Step 3: Double check and make sure you only include information on the subject and information, if there is extra information on an irrelevant subject, please remove it.
     Step 4: Make sure to add that course to the list of courses previously discussed with the user.
     """}
]

def get_msg_completion(client: OpenAI, message, temperature: float = 0, model: str = 'gpt-3.5-turbo') -> str:
    dangerous = client.moderations.create(input = message[-1]["content"])
    if dangerous.results[0].flagged == True:
        return "Bad"
    response = client.chat.completions.create(model = model, messages = message, temperature = temperature).choices[0].message.content
    dangerous = client.moderations.create(input = response)
    if dangerous.results[0].flagged == True:
        return "Bad"
    
    return response


def collect_messages(client, prompt, data) -> str:
    chatbot_context.append({'role': 'user', 'content': "```" + prompt + "```" + 'provided data: ***' + data + '***'}) 
    response = get_msg_completion(client, chatbot_context)
    print(f'Assistant> {response}')
    chatbot_context.append({'role': 'assistant', 'content': response})
    return prompt

def check_database(client, prompt) -> str: #converts the user prompt into a python array with information to prompt the database
    llmPrompt = [{'role': 'system', 'content': f"""Analyze the following prompt in delimiters  ```{prompt}```, This prompt should be asking for advising for a 
                  mathematics major.
                  Step 1: Search for information that will allow you to fill in the user's preference in the following two areas: 
                  1. desired subject in mathematics 
                  2. desired difficulty of course. 
                  
                  Step 2: If any of this information is missing, return ['none']. Do not progress to any other steps if this information is missing and just return ['none']
                  Step 2.1: If the user is asking for other information, return ['none']
                  
                  Step 3: Create a python list object to return that contains two pieces of information 1:
                  The subject that the user is looking for based on this list of subjects: Intro Mathematics, Calculus 1, Calculus 2, Calculus 3, History of 
                  Mathematics, Computer Science, Intro Linear Algebra, Advanced Linear Algebra, Economics, College Algebra, Intro Statistics, 
                  advanced Statistics, Physics 1, Physics 2, Physics 3, Graduate Algebra, Graduate Calculus, Graduate Statistics, Graduate Mathematics, 
                  Independent Research 2. 
                  The desired difficulty of course from the following list: easy, medium, hard, Graduate. 
                  Step 3.1: if one of those pieces of information was unable to be found or wasn't able to be placed into one of the cateogries provided, return a list object with only one value: 
                  the string 'none' it should look like: ["none"], 
                  Step 4: return only the list object with the two values determined previously. For example, if the user asks to take 
                  intro statistics at a medium difficulty, return ['Intro Statistics', 'medium']. 
                  Step 5: Double check if the user's information doesn't include information on the subject and difficulty. Return ['none'] if it doesn't."""}]
    
    response = get_msg_completion(client, [{'role': 'system', 'content': prompt}])
    if response == "Bad": #checks if the moderation flags the prompt, have to do this seperately since the prompt is so huge it doesn't reach a high enough percentage of innapropriate phrases no matter what the user types
        print("Your prompt was innapropriate. Try again.")
        return
    response = get_msg_completion(client, llmPrompt)
    #uses what was returned by the llm to create a dictionary
    temp = response.split(", ")
    response = []
    for val in temp:
        val = val.strip("[]\"\' ")
        response.append(val)
    try: #checks to see if the response returns information
        if response[0] == 'none' or response[1] == 'none':
            llmPrompt = [{'role': 'system', 'content': f"""
                        Step 1: Decide wether the user's prompt denoted by *** is asking about previously discussed classes ***{prompt}***. For example they might be asking you to list all previously discussed courses. 
                        Step 2: If they are asking about previously dicussed courses, return the word 'list' with no other characters. 
                        Step 3: If they are not asking about previously discussed classes, return the word 'none'"""}]
            response = get_msg_completion(client, llmPrompt)
            if response == 'list': #if the user asks for a list of previous courses it is given to them
                chatbot_context.append({'role': 'user', 'content': "```List previously discussed courses, do not give any other information```"})
                response = get_msg_completion(client, chatbot_context)
                chatbot_context.remove({'role': 'user', 'content': "```List previously discussed courses, do not give any other information```"})
                print(response)
            else: #otherwise without enough information it returns this
                print("Im sorry, you need to include both a course topic and a difficulty level for me to be able to suggest you a course. Clearly you did that wrong. Try again.")
            return
        #queries the database if it has information
        dataBaseResponse = mathInfo.query.fetch_objects(filters = Filter.by_property("subject").equal(response[0]) & Filter.by_property("difficulty").equal(response[1]))
        if len(dataBaseResponse.objects) == 0: #if the database returns nothing we get this message
            print("Im sorry, your requested information is not in the database. It not my fault, its yours. Try again.")
            return
        #turns the database output into a string so it can be easily passed into the LLM
        data = dataBaseResponse.objects[0].properties
        dataStr = ""
        for key in data:
            dataStr += f"{key}: {data[key]}, "
        #prompts the LLM to see if the response is relevant to the query
        llmPrompt = [{'role': 'system', 'content': f"analyze the following prompt in delimiters ```{prompt}``` as well as the information that was pulled from a database for that prompt notated by ***. ***{dataStr}***. If the information in the database is relevant to the prompt, return the word 'relevant'. The data doesn't need to perfectly match the prompt, it just needs to be close. If the information in the database is completely irrelevant to the prompt, return the word 'irrelevant'. Make sure to only return either relevant or irrelevant with no extra characters"}]
        response = get_msg_completion(client, llmPrompt)
        if response == "relevant": #if it is it uses the chatbot context to chat with the user
            prompt = collect_messages(client, prompt, dataStr)
        else: #if it is not relevant this is printed
            print("Im sorry, your requested information is not in the database. You should really do better. Please try again.")
    except: #catches errors that often stem from random inputs
        print("Im sorry, your prompt was insufficient to do literally anything. Please give me more information.")

def main():
    client = OpenAI()
    print("Welcome to the Math Major Advising Chatbot! Please feel free to ask questions for help picking a math class.")
    prompt = input("User> ")
    while prompt != '':
        check_database(client, prompt)
        prompt = input("User> ")


if __name__ == '__main__':
    main()

#easy test for copy-paste
# I would like to take calculus 2 at a medium difficulty level