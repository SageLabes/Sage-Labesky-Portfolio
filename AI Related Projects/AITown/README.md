# AI Town Resident Chatbot
## Collaborators: Sage Labesky, Christy Vo

Our chatbot is meant to simulate a resident of a fictional town after an apocolypse. The point of the chatbot is to answer the user's questions about the town and the surrounding areas. This idea came from a system in a video game where you can ask NPCs about specific things in a text interface. 

We implemented this by breaking up the LLM's response with specific steps. First the LLM should categorize the user's response into a list of categories we have provided. It then was provided information in those cateogries in step two that it could use to formulate a response. We also specified that if it couldn't  answer the question with just the information in that category it could draw a conclusion based off of the other categories. In the third step we had it double check its response to make sure it fit with the information we provided and didn't make up new information. We also had the LLM make sure its vocabulary wasn't too advanced as the character it is playing is meant to exist in a post apocolypse, and we made sure it only returned the response and not the category. Finally the LLM returns the response.

Along the way we provided a set of made up data on the town for the LLM to use to answer questions. We also implemented OpenAI's moderation feature
and worked it into the character. 

### To run this code you will need to libraries found in requirements.txt, a GPT 3.5 turbo API key from OpenAI set as a global variable in your computer, and if on a windows machine you will need to run the program using Docker or a Linux subsystem due to library incompatibilities.