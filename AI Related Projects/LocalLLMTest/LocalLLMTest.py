from transformers import AutoModelForCausalLM
# Load model directly--note, transformers is from huggingface
from transformers import AutoModelForCausalLM
# Tokenizer class from the transformers library
from transformers import AutoTokenizer


def main():
    model = AutoModelForCausalLM.from_pretrained("apple/OpenELM-270M", trust_remote_code=True)
    # creates a tokenizer, which is the llama tokenizer in this case
    tokenizer = AutoTokenizer.from_pretrained('meta-llama/Llama-2-7b-hf', padding_side='left')

    model_inputs = tokenizer(['List of car parts: '], return_tensors='pt')

    # generate 50 tokens, sample from the output so it's not always the same (creativity!)
    generated_ids = model.generate(**model_inputs, do_sample=True, max_new_tokens=50)

    print(tokenizer.batch_decode(generated_ids, skip_special_tokens=True)[0])
    
# run the main
if __name__ == '__main__':
    main()