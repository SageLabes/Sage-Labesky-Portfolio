
'''
File: sentiment_analysis.py
Author: Sage Labesky, Christy Vo
Date: 

This provides sentiment analysis functions for processing tweets in particular,
but relies on tweet_processing to handle the cleanup of the tweets. Analysis is
done using Naive Bayes.

'''
import random
import tweet_processor as tp

import numpy as np
import math

from openai import OpenAI

client = OpenAI()
#get an llm response
def get_llm_response(client : OpenAI, prompt : str) -> str:
    """
    function that gets an output from chatGPT
    """
    completion = client.chat.completions.create(
        model = 'gpt-3.5-turbo',
        messages = [
            {'role' : 'user', 'content' : prompt}
        ],
        # temperature is the randomness of your result 
        temperature=0
    )
    return completion.choices[0].message.content

def partition_training_and_test_sets(pos_tweets : list[str],
                                     neg_tweets : list[str], 
                                     split : float = .8) -> tuple[list[str], 
                                                                  np.ndarray[float], 
                                                                  list[str], 
                                                                  np.ndarray[float], 
                                                                  int, int, int, int]:
    '''
    Partition our sets of tweets into positive and negative tweets based
    on a split factor. 

    Parameters: 
        pos_tweets -- list of strings that are positive tweets
        neg_tweets -- list of strings that are negative tweets
        split -- factor to split the training and partition sets into.
            Defaults to .8, or 80% training, 20% testing.

    Returns:
        A list of training tweets
        A list of the same size of training labels, which will be 1 or 0 for positive or negative tweets
        A list of testing tweets
        A list of testing labels, which 
    '''
    if split < 0 or split > 1:
        raise Exception('split must be between 0 and 1')
    
    # multiply the length of the list of tweets by our split factor and convert to an int
    pos_train_size = int(split * len(pos_tweets))
    neg_train_size = int(split * len(neg_tweets))
    
    # split our sets
    pos_x = pos_tweets[:pos_train_size]
    neg_x = neg_tweets[:neg_train_size]
    
    # test sets
    test_pos = pos_tweets[pos_train_size:]
    test_neg = neg_tweets[neg_train_size:]

    # combine the sets for training and testing
    train_x = pos_x + neg_x
    test_x = test_pos + test_neg

    # our labels are 1 for positive, 0 for negative, so we'll create
    # arrays of 1s and 0s for the training and test sets
    train_y = np.append(np.ones(len(pos_x)), np.zeros(len(neg_x)))
    test_y = np.append(np.ones(len(test_pos)), np.zeros(len(test_neg)))

    pos_test_size = len(pos_tweets) - pos_train_size
    neg_test_size = len(neg_tweets) - neg_train_size
    return (train_x, train_y, test_x, test_y, pos_train_size, 
            neg_train_size, pos_test_size, neg_test_size)


# takes a list of tweets        
def build_word_freq_dict(tweets : list[list[str]], labels : np.ndarray[int]) -> dict[(str, int),  int]:
    '''
    Creates a frequency dictionary based on the tweets. The frequency dictionary
    has keys which are (word, label) pairs, for example, ('happi', 1), while the
    value associated with it is the number of times that word was seen in a given
    class. For example, if 'happi' is seen 10 times in positive tweets, then we'd 
    see freqs[('happi', 1)] = 10. If it were seen 3 times in negative tweets, we'd
    see freqs[('happi', 0)] = 3.

    Parameters: 
    tweets -- A list of strings, each a tweet
    labels -- A list of integers either 0 or 1 for negative or positive classes

    Note that the number of tweets and labels must match. 

    Return: 
    A dictionary containing (word, class) keys mapping to the number of 
    times that word in that class appears in the data set
    '''
    dict = {}
    vocab = set()
    
    # create the dictionary and vocabulary here
    
    if len(tweets)!= len(labels):
        raise ValueError("Number of tweets and labels must match")
    # creates the dictionary and vocab set by going through the tweets
    for tweet, label in zip(tweets, labels): 
        for word in tweet: 
            if (word, label) in dict.keys():
                dict[(word, label)] += 1
            else:
                dict[(word, label)] = 1
            vocab.add(word)

    # return the frequency dictionary
    return dict, vocab

def test_word_freq_dict():
    '''
    Simple function that tests some tweets and if your build_word_freq_dict is built correctly
    '''
    tweets = [['i', 'am', 'happi'], ['i', 'am', 'trick'], ['i', 'am', 'sad'], 
              ['i', 'am', 'tire'], ['i', 'am', 'tire']]
    labels = [1, 0, 0, 0, 0]
    print("testing build_word_freq_dict, should get {('i', 1): 1, ('am', 1): 1, ('happi', 1): 1, ('i', 0): 4, ('am', 0): 4, ('trick', 0): 1, ('sad', 0): 1, ('tire', 0): 2}")
    print(f'test of word frequency: {build_word_freq_dict(tweets, labels)}')


def count_pos_neg(freqs : dict[(str, int),  int]) -> tuple[int, int]:
    '''
    Count the number of positive and negative words in the
    frequency dictionary.

    Parameters:
    freqs -- a dictionary of ((str, int), int) pairs, where the key is a
    word and label of 0 or 1 for negative or positive sentiment, and the value
    associated with the key is the number of times it was seen.

    Returns:
    Returns two values, the number of times any positive word was seen (i.e., the
    total number of positive events), and the number of times a negative word was
    seen. 
    '''
    num_pos = num_neg = 0
    # calculate the number of times each word appears in 
    # particular class of positive or negative 
    for word in freqs.keys():
        if word[1] == 1:
            num_pos += freqs[word]
        else:
            num_neg += freqs[word]

    return num_pos, num_neg



def build_loglikelihood_dict(freqs : dict[(str, int),  int], N_pos : int, N_neg : int, vocab : list[str]) -> dict[str, float]:
    '''
    Create a dictionary based on the frequency of each word in each class appearing
    of the probability of that word occuring, using Laplacian smoothing by adding
    1 to each occurrence and the size of the vocabulary. 

    Thus, we'd calculate (freq(w_i, class) + 1) / (N_class + V_size)

    Parameters:
        freqs -- dictionary from (word, class) to occurrence count mapping
        N_pos -- number of positive events for all words
        N_neg -- number of negative events for all words
        vocab -- list vocabulary of words

    Returns:
        A dictionary of words to the ratio of positive and negative usage of the word
    '''
    loglikelihood = {}
    vocab_size = len(vocab)
    # calculate the loglikelihood dictionary from the given parameters
    for word in vocab:
        if (word, 1) in freqs:
            if(word, 0) in freqs: # has four cases to account for wether or not that word exists in the frequency dictionary with positive and/or negative values
                loglikelihood[word] = math.log(((freqs[(word, 1)] + 1) / (N_pos + len(vocab)))/((freqs[(word, 0)] + 1) / (N_neg + len(vocab))))
            else:
                loglikelihood[word] = math.log(((freqs[(word, 1)] + 1) / (N_pos + len(vocab)))/(1 / (N_neg + len(vocab))))
        else:
            if(word, 0) in freqs:
                loglikelihood[word] = math.log((1 / (N_pos + len(vocab)))/((freqs[(word, 0)] + 1) / (N_neg + len(vocab))))
            else:
                loglikelihood[word] = math.log((1 / (N_pos + len(vocab)))/(1 / (N_neg + len(vocab))))
    
    return loglikelihood

def naive_bayes_predict(loglikelihood : dict[str, float], log_pos_neg_ratio : float, tweet : list[str]) -> float:
    '''
    Calculates the prediction based on our dictionary of log-likelihoods of each
    word in a tweet added to the log of the ratio of positive and negative tweets

    Parameters:
        loglikelihood -- a dictionary of words to the ratio of postive/negative probabilities of the words
        log_pos_neg_ratio -- the log of the ratio of total positive to total negative events
        tweet -- a list of tokens (likely from process_tweet)
    '''
    # Return the prediction of a given tweet using the dictionary and ratio
    #initialize score
    score = 0.0

    for word in tweet: 
        if word in loglikelihood: 
            score+= loglikelihood[word]
        else:
            score+=0
    # Have to add the ratio in order for the score to properly orient around 1
    score += log_pos_neg_ratio

    return score


def main():
    # first, set up our samples
    pos_tweets, neg_tweets, stopwords = tp.process_tweets('positive_tweets.json', 'negative_tweets.json', 'english_stopwords.txt')
    
    # you can uncomment the next two lines once your tweet processing is working
    print(f'random positive: {pos_tweets[random.randint(0, len(pos_tweets) - 1)]}')
    print(f'random negative: {neg_tweets[random.randint(0, len(neg_tweets) - 1)]}')

    # defines the partition between training and test sets
    SPLIT = .8
    
    # next, partition the sets into training sets, test sets, and labels
    train_x, train_y, test_x, test_y, N_train_pos, N_train_neg, N_test_pos, N_test_neg = partition_training_and_test_sets(pos_tweets, neg_tweets, SPLIT)
    print(f'N_train_pos = {N_train_pos}, N_train_neg = {N_train_neg}')
    print(f'N_test_pos = {N_test_pos}, N_test_neg = {N_test_neg}')

    # create a frequency dictionary
    freq_train, vocab = build_word_freq_dict(train_x, train_y)
    print(f'freq dictionary size: {len(freq_train)}, vocab size: {len(vocab)}')
    
    # count the number of positive and negative words
    num_pos, num_neg = count_pos_neg(freq_train)
    print(f'Number of positive events: {num_pos}, Number of negative events: {num_neg}')

    # log of the ratio of the total positive and total negative tweets from the training set
    log_pos_neg_ratio = math.log(num_pos / num_neg)
    print(f'log_pos_neg_ratio of the training set = {log_pos_neg_ratio}')

    # now calculate the log likelihood dictionary
    log_likelihood = build_loglikelihood_dict(freq_train, num_pos, num_neg, vocab)
    
    #uncomment the code below once you have everything above working
    #now let's test some predictions
    for i in range(10):
        idx = random.randint(0, N_test_pos + N_test_neg - 1)
        print(f'Tweet: {test_x[idx]}')
        print(f'Label: {test_y[idx]}')
        print(f'Prediction: {naive_bayes_predict(log_likelihood, log_pos_neg_ratio, test_x[idx])}')
        print()

    # now let's see what our error rate is
    # Calculate the error rate and print it out
    # also print out the mislabeled tweets
    successes = 0
    mislabeled = []
    for i in range(N_test_pos + N_test_neg):
        pred = naive_bayes_predict(log_likelihood, log_pos_neg_ratio, test_x[i]) 
        if pred > 1 and test_y[i] == 1:
            successes += 1
        elif pred < 1 and test_y[i] == 0:
            successes += 1
        else:
            mislabeled.append((test_x[i], test_y[i]))
    print(f'Error rate: {len(mislabeled) / (N_test_pos + N_test_neg)}') #error rate is mislabeled divided by total number of tweets tested
    print(f'Mislabeled tweets: {mislabeled}')

    #traverses the mislabeled tweets, feeds them to the GPT model, and prints the prediction
    for tweet in mislabeled:
        prompt = "for the tweet denoted by ```, determine if the sentiment is positive or negative and only answer with either 'Positive' or 'Negative': '''" + f"{tweet}```"
        answer = get_llm_response(client, prompt).strip(" ")
        print(f"Tweet: {tweet}, Prediction: {answer}")
        # Checks to see if GPT predicts differently than the actual human selected answer and asks it for an explanation if it does
        if answer == 'Positive' and tweet[1] == 0:
            print(get_llm_response(client, f"Why do you think the tweet denoted by ``` is positive? please answer in a few sentences and use any word in the english dictionary. ```{tweet[0]}```"))
        elif answer == 'Negative' and tweet[1] == 1:
            print(get_llm_response(client, f"Why do you think the tweet denoted by ``` is negative? please answer in a few sentences and use any word in the english dictionary. ```{tweet[0]}```"))

    #subtle tweet ran through Naive Bayes, fails Bayes but passes GPT
    subtle_tweet = ["I", "am", "not", "happi"]
    print(subtle_tweet)
    print(f'Naive Bayes Prediction: {naive_bayes_predict(log_likelihood, log_pos_neg_ratio, subtle_tweet)}')
    print("GPT prediction: " + get_llm_response(client, f"for the tweet denoted by ```, determine if the sentiment is positive or negative and only answer with either 'Positive' or 'Negative': '''{subtle_tweet}```"))


    


# run the main function if this is where our program was executed from
if __name__ == '__main__':
    main()